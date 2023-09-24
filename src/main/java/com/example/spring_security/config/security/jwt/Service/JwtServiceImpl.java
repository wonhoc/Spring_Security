package com.example.spring_security.config.security.jwt.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.spring_security.config.security.jwt.JwtProperties;
import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;
import com.example.spring_security.config.security.jwt.exception.JwtErrorMessage;
import com.example.spring_security.config.security.jwt.exception.JwtException;
import com.example.spring_security.user.domain.User;
import com.example.spring_security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @package     com.example.spring_security.config.security.jwt.Service
 * @class   JwtServiceImpl
 * @brief       jwt 비즈니스로직 구현 클래스
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    /**
     * @brief   토큰 생성
     * @param   email
     * @return  accessToken
     */
    @Override
    public String createAccessToken(String email) {

        return JWT.create()
                .withSubject(JwtProperties.ACCESS_TOKEN)
                .withClaim(JwtProperties.EMAIL, email)
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    /**
     * @brief   refresh 토큰 생성
     * @return  refreshToken
     */
    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(JwtProperties.REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    /**
     * @brief   email을 통한 유저 entity 조회
     * @param   email
     * @return  LoginResponseDto
     */
    @Override
    public LoginResponseDto retrieveByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow();

        return LoginResponseDto.builder().userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickName(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .refreshToken(user.getRefreshToken())
                .userRole(user.getUserRole())
                .socialType(user.getSocialType())
                .build();
    }

    /**
     * @brief   refreshToken 수정
     * @param   userId
     * @param   refreshToken
     */
    @Override
    @Transactional
    public void setRefreshToken(long userId, String refreshToken) {

        userRepository.findById(userId).orElseThrow().updateRefreshToken(refreshToken);
    }

    /**
     * @brief   accessToken Header에 설정
     * @param   response
     * @param   accessToken
     */
    @Override
    public void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(JwtProperties.ACCESS_TOKEN_HEADER,JwtProperties.TOKEN_PREFIX + accessToken);
    }

    /**
     * @brief   refreshToken Header에 설정
     * @param   response
     * @param   refreshToken
     */
    @Override
    public void setRefreshTokenToHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(JwtProperties.REFRESH_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + refreshToken);
    }

    /**
     * @brief   결과 메시지 구현
     * @param   result
     * @param   response
     * @param   message
     */
    @Override
    public void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException {

        JSONObject jObject = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");

        if(result){
            response.setStatus(HttpServletResponse.SC_OK);
            jObject.put("success", true);
            jObject.put("code", 200);
            jObject.put("message", message);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jObject.put("success", false);
            jObject.put("code", 999);
            jObject.put("message", message);
        }
        response.getWriter().print(jObject);
    }

    /**
     * @brief   Header에 정상적인 토큰 여부 확인
     * @param   req
     * @return  true
     */
    @Override
    public boolean isValidHeaderOrThrow(HttpServletRequest req) {

        log.info("** Check whether there is token in header **");

        String accessToken = req.getHeader(JwtProperties.ACCESS_TOKEN_HEADER);
        String refreshToken = req.getHeader(JwtProperties.REFRESH_TOKEN_HEADER);

        log.info(accessToken + " IS accessToken");
        log.info(refreshToken + " IS refreshToken");

        if(accessToken != null
                && refreshToken != null
                && accessToken.startsWith(JwtProperties.TOKEN_PREFIX)
                && refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)){

            return true;
        } else {
            throw new JwtException(JwtErrorMessage.JWT_HEADER_IS_NOT_VALID);
        }
    }

    /**
     * @brief   Access Token 추출
     * @param   req
     * @return  accessToken
     */
    @Override
    public String replaceAccessToken(HttpServletRequest req) {

        return req.getHeader(JwtProperties.ACCESS_TOKEN_HEADER).replace(JwtProperties.TOKEN_PREFIX,"");
    }

    /**
     * @brief   Refresh Token 추출
     * @param   req
     * @return  refreshToken
     */
    @Override
    public String replaceRefreshToken(HttpServletRequest req) {

        return req.getHeader(JwtProperties.REFRESH_TOKEN_HEADER).replace(JwtProperties.TOKEN_PREFIX,"");
    }

    /**
     * @brief   토큰을 해석하여 정상 여부 확인
     * @param   refreshToken
     * @return  true
     */
    @Override
    public boolean isNotExpiredRefreshToken(String refreshToken) {
        try {
            log.info("** check the refresh token **");

            JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(refreshToken);
        } catch (Exception e){

            throw new JwtException(JwtErrorMessage.JWT_REFRESH_IS_NOT_VALID);
        }
        return true;
    }

    /**
     * @param   accessToken
     * @brief   accessToken 확인
     * @details accessToken의 만료 여부를 확인한다
     * @return  true
     */
    @Override
    public boolean isNotExpiredAccessToken(String accessToken) {
        try {

            log.info("** check the access token **");

            JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(accessToken);

            return true;
        } catch (TokenExpiredException e) {
            throw new JwtException(JwtErrorMessage.JWT_ACCESS_IS_EXPIRED);
        } catch (Exception e2) {
            throw new JwtException(JwtErrorMessage.JWT_ACCESS_IS_NOT_VALID);
        }
    }

    /**
     * @brief   토큰을 통한 User 조회
     * @param   refreshToken
     * @return  loginResponseDto
     */
    @Override
    @Transactional
    public LoginResponseDto selectByRefreshToken(String refreshToken) {

        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow();

        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .build();
    }

    /**
     * @brief   토큰의 10경과 여부 확인
     * @param   refreshToken
     * @return  true
     */
    @Override
    public boolean checkTokenIsMadeInTendays(String refreshToken) {
        try {
            Date expiresDate = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(refreshToken).getExpiresAt();

            Date currentDate = new Date();

            return expiresDate.before(currentDate) ? true : false;
        } catch (TokenExpiredException e) {
            throw new JwtException(JwtErrorMessage.JWT_REFRESH_IS_EXPIRED);
        } catch (Exception e2) {
            throw new JwtException(JwtErrorMessage.JWT_REFRESH_IS_NOT_VALID);
        }
    }

    /**
     * @brief   Refresh Token 재설정
     * @param   email
     * @param   refreshToken
     * @return  refreshToken
     */
    @Override
    @Transactional
    public String updateRefreshToken(String email, String refreshToken) {

        String newRefreshToken = createRefreshToken();

        User user = userRepository.findByEmail(email).orElseThrow();

        user.updateRefreshToken(newRefreshToken);

        return newRefreshToken;
    }
}
