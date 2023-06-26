package com.example.spring_security.config.security.jwt;

/**
 * @package     com.example.spring_security.config.security.jwt
 * @interface   JwtProperties
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
public interface JwtProperties {

    String ACCESS_TOKEN = "AccessToken";
    String REFRESH_TOKEN = "RefreshToken";
    String EMAIL = "EMAIL";
    int EXPIRATION_TIME = 864000000; // 10일 (1/1000초)
    String TOKEN_PREFIX = "Bearer "; // 반드시 공백 포함
    String ACCESS_TOKEN_HEADER = "Authorization";
    String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    String Exception = "EXCEPTION";
}
