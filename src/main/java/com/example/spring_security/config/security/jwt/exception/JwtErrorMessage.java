package com.example.spring_security.config.security.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @package     com.example.spring_security.config.security.jwt.exception
 * @enum        JwtErrorMessage
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
@Getter
@RequiredArgsConstructor
public enum JwtErrorMessage {
    JWT_TOKEN_IS_VALID("유효한 토큰입니다.")
    , JWT_ACCESS_IS_NOT_VALID("access 토큰이 유효하지 않습니다.")
    , JWT_REFRESH_IS_NOT_VALID("refresh 토큰이 유효하지 않습니다.")
    , JWT_ACCESS_IS_EXPIRED("access 토큰이 만료되었습니다.")
    , JWT_REFRESH_IS_EXPIRED("refresh 토큰이 만료되었습니다. 다시 로그인 해주세요.")
    , JWT_IS_NOT_VALID("토큰이 유효하지 않습니다.")
    , JWT_HEADER_IS_NOT_VALID("헤더가 유효하지 않습니다.")
    , JWT_MEMBER_IS_NOT_FOUND_TOKEN("회원정보가 존재하지 않습니다.")
    , JWT_MEMBER_IS_NOT_FOUND_EMAIL("email이 존재하지 않습니다.")
    ;

    private final String message;
}
