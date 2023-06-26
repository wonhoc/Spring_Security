package com.example.spring_security.config.security.jwt.Service;

import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
}
