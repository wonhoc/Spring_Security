package com.example.spring_security.config.security.jwt.exception;

import com.example.spring_security.config.security.jwt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @package     com.example.spring_security.config.security.jwt.exception
 * @class        AccessDeniedHandlerImpl
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final JwtService jwtService;

    /**
     * @brief   인가 오류시 실행되는 method
     * @param   request
     * @param   response
     * @return  accessDeniedException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("권한 오류");
        jwtService.setResponseMessage(false, response, accessDeniedException.getMessage());
    }
}
