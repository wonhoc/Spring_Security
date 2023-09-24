package com.example.spring_security.config.security.jwt.Service;

import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @package     com.example.spring_security.config.security.jwt.Service
 * @interface   JwtService
 * @brief       jwt 비즈니스로직 인터페이스
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
public interface JwtService {

    /* 토큰 생성 */
    String createAccessToken(String email);

    /* refresh 토큰 생성 */
    String createRefreshToken();

    /* email로 user 조회 */
    LoginResponseDto retrieveByEmail(String email);

    /* refreshToken 재설정 */
    void setRefreshToken(long userId, String refreshToken);

    /* response Header영역에 accessToken 설정 */
    void setAccessTokenToHeader(HttpServletResponse response, String accessToken);

    /* response Header영역에 refreshToken 설정 */
    void setRefreshTokenToHeader(HttpServletResponse response, String refreshToken);

    /* responseMessage설정 */
    void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException;

    /* Header에 정상적인 토큰여부 확인 */
    boolean isValidHeaderOrThrow(HttpServletRequest req) throws UnsupportedEncodingException;

    /* Access Token 인증 타입(Bearer) 제거 */
    String replaceAccessToken(HttpServletRequest req) throws UnsupportedEncodingException;

    /* Refresh Token 인증 타입(Bearer) 제거 */
    String replaceRefreshToken(HttpServletRequest req) throws UnsupportedEncodingException;

    /* 토큰을 해석하여 정상 여부 확인 */
    boolean isNotExpiredRefreshToken(String refreshToken);

    /* Token 확인 */
    boolean isNotExpiredAccessToken(String accessToken);

    /* Token으로 User객체 조회 */
    LoginResponseDto selectByRefreshToken(String refreshToken);

    /* Token 발급일로부터 10일 경과 여부 확인 */
    boolean checkTokenIsMadeInTendays(String refreshToken);

    /* Token 재설정 */
    String updateRefreshToken(String email, String refreshToken);

}
