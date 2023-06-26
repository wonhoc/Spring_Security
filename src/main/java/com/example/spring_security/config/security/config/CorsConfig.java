package com.example.spring_security.config.security.config;

import com.example.spring_security.config.security.jwt.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @package     com.example.spring_security.config.security.config
 * @enum        CorsConfig
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
@Configuration
public class CorsConfig {

    /**
     * @brief   CORS 차단을 해제하고 JWT 방식으로만 검증할 것임으로 생성
     * @return  CorsFilter
     */

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);                           //json 서버 응답을 자바 스크립트에서 처리할 수 있게 해줌
        config.addAllowedOrigin("http://localhost:8080");           //모든 IP에 응답을 허용
        config.addAllowedHeader("*");                               //모든 Http Header에 허용
        config.addAllowedMethod("*");                               //모든 Http METHOD에 허용
        config.addExposedHeader(JwtProperties.ACCESS_TOKEN_HEADER);
        config.addExposedHeader(JwtProperties.REFRESH_TOKEN_HEADER);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
