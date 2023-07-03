package com.example.spring_security.config.security.jwt.filter;

import com.example.spring_security.config.security.auth.PrincipalDetails;
import com.example.spring_security.config.security.jwt.JwtProperties;
import com.example.spring_security.config.security.jwt.Service.JwtService;
import com.example.spring_security.config.security.jwt.dto.LoginDto;
import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @package com.amount.amount.config.security.jwt
 * @class   JwtAuthorizationFilter
 * @brief   인가
 * @details 권한이나 인증이 필요한 특정 주소를 요청했을 때 BasicAuthenticationFilter를 탄다.
 *          권한이나 인증이 필요하지 않타면 로직 활용 x
 * @author  최원호
 * @date    2023.05.02
 * version  1.0
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    /**
     * @brief  인가
     * @param req
     * @param res
     * @param chain
     * @detail 인증이나 권한이 필요한 주소 요청 시 필터로 이동
     */
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {

            //올바르지 않은 헤더 재로그인
            if (jwtService.isValidHeaderOrThrow(req)) {

                String accessToken = jwtService.replaceAccessToken(req);
                String refreshToken = jwtService.replaceRefreshToken(req);


                // 만료된 리프레쉬 토큰은 재로그인
                if (jwtService.isNotExpiredToken(refreshToken)) {

                    LoginResponseDto userDto = jwtService.selectByRefreshToken(refreshToken);
                    log.info(userDto.getEmail());

                    // 리프레쉬 토큰이 10일 이내 만료일 경우 새로 발급
                    if (jwtService.checkTokenIsMadeInTendays(refreshToken)) {

                        refreshToken = jwtService.updateRefreshToken(userDto.getEmail(), refreshToken);
                        jwtService.setRefreshTokenToHeader(res, refreshToken);
                    }

                    // 엑세스 토큰이 만료된 경우 새로 발급
                    if (jwtService.checkValidToken(accessToken)) {

                        String reissuedAccessToken = jwtService.createAccessToken(userDto.getEmail());
                        jwtService.setAccessTokenToHeader(res, reissuedAccessToken);
                    }

                    PrincipalDetails principal = new PrincipalDetails(userDto);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } else {
                log.info("[LOGIN] > 재 로그인 필요");
            }
        } catch (AuthenticationException e){
            log.info("******************************************************************");
            log.info("인가 에러>>>>>>>>>>>>>>>>>>"+e.getMessage());
            log.info("******************************************************************");
            req.setAttribute(JwtProperties.Exception, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(req, res);
    }
}
