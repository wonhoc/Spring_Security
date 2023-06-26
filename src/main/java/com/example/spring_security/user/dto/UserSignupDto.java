package com.example.spring_security.user.dto;

import lombok.Getter;

/**
 * @package com.example.spring_security.user.dto
 * @class   UserSignupDto
 * @brief   회원가입 Dto
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
public class UserSignupDto {

    @Getter
    public static class UserSignupRequestDto {

        private String email;
        private String password;
        private String nickname;

    }

    public static class UserSignupResponseDto {

        private long userId;
        private String email;
        private String password;
        private String refreshToken;
        private String userRole;

    }

}
