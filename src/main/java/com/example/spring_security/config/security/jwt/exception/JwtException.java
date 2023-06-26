package com.example.spring_security.config.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {

    public JwtException(JwtErrorMessage message) {
        super(message.getMessage());
    }
}
