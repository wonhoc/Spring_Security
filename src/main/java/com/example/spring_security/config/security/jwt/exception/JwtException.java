package com.example.spring_security.config.security.jwt.exception;

import org.springframework.security.access.AuthorizationServiceException;


/**
 * @package     com.example.spring_security.config.security.jwt.exception
 * @class       JwtException
 * @author      최원호
 * @date        2023.06.22
 * @version     1.0
 */
public class JwtException extends AuthorizationServiceException {

    public JwtException(JwtErrorMessage message) {
        super(message.getMessage());
    }
}
