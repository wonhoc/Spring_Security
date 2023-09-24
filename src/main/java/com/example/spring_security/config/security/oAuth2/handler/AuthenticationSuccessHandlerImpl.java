package com.example.spring_security.config.security.oAuth2.handler;

import com.example.spring_security.config.security.jwt.JwtProperties;
import com.example.spring_security.config.security.jwt.Service.JwtService;
import com.example.spring_security.config.security.oAuth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User cusOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if("ROLE_USER".equals(cusOAuth2User.getUserRole())) {

                String accessToken = jwtService.createAccessToken(cusOAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();

                jwtService.setAccessTokenToHeader(res, accessToken);
                jwtService.setRefreshTokenToHeader(res, refreshToken);

                jwtService.updateRefreshToken(cusOAuth2User.getEmail(), refreshToken);
            }

        } catch (AuthenticationException e) {
            log.info("******************************************************************");
            log.info("인가 에러>>>>>>>>>>>>>>>>>>"+e.getMessage());
            log.info("******************************************************************");
            req.setAttribute(JwtProperties.Exception, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
