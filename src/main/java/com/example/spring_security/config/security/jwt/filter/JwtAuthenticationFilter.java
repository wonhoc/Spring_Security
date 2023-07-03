package com.example.spring_security.config.security.jwt.filter;

import com.example.spring_security.config.security.auth.PrincipalDetails;

import com.example.spring_security.config.security.jwt.Service.JwtService;
import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginRequestDto;
import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @package com.example.spring_security.config.security.jwt.filter
 * @class   JwtAuthenticationFilter
 * @brief   인증 필터
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * @brief   인증 요청시에 실행되는 함수 (/login 요청 시 실행됨)
     * @param   req
     * @param   res
     * @return  Authentication               회원가입 결과
     */
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        Authentication authentication = null;

        try {

            /* 1. username(email)과 password를 받는다 */
            LoginRequestDto loginDto = om.readValue(req.getInputStream(), LoginRequestDto.class);

            log.info("**************" +loginDto.getEmail() + " is  trying to login **************");


            /* 2. email과 password를 이용하여 token 발급 */
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            log.info("********************************************************");
            log.info("*             success to create token                  *");
            log.info("********************************************************");

            /* 3. 정상적인 로그인 여부 확인 */
            /* authenticate(토큰) 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
               loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
               UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
               UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
               Authentication 객체를 만들어서 필터체인으로 리턴해준다. */
            authentication = authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 4. authentication 반환 */
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("**************************************************************");
        log.info("*                  success to authentication                 *");
        log.info("**************************************************************");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtService.createAccessToken(principalDetails.getUsername());
        String refreshToken = jwtService.createRefreshToken();

        LoginResponseDto user = jwtService.retrieveByEmail(principalDetails.getUsername());
        jwtService.setRefreshToken(user.getUserId(), refreshToken);

        jwtService.setAccessTokenToHeader(response, accessToken);
        jwtService.setRefreshTokenToHeader(response, refreshToken);
        jwtService.setResponseMessage(true, response, "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("**************************************************************");
        log.info("*                  fail to authentication                    *");
        log.info("**************************************************************");

        jwtService.setResponseMessage(false, response, failed.getMessage());
    }
}
