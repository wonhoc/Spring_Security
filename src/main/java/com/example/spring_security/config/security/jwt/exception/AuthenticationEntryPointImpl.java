package com.example.spring_security.config.security.jwt.exception;

import com.example.spring_security.config.security.jwt.JwtProperties;
import com.example.spring_security.config.security.jwt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @package com.example.spring_security.config.security.jwt.exception
 * @class   AuthenticationEntryPointImpl
 * @brief   인증 실패시 실행
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final JwtService jwtService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String errorMessage = (String) request.getAttribute(JwtProperties.Exception);
        log.error("authException message: " + authException.getMessage());
        jwtService.setResponseMessage(false, response, errorMessage);
    }
}
