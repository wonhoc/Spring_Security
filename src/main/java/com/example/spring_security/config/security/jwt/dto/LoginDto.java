package com.example.spring_security.config.security.jwt.dto;

import com.example.spring_security.user.common.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginDto {

    @Getter
    public static class LoginRequestDto {

        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponseDto {

        private long userId;
        private String email;
        private String password;
        private String nickName;
        private String profileUrl;
        private String refreshToken;
        private String userRole;
        private SocialType socialType;

        public List<String> getRoleList(){
            if(this.userRole.length() > 0){
                return Arrays.asList(this.userRole.split(","));
            }
            return new ArrayList<>();
        }

    }

}
