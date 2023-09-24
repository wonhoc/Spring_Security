package com.example.spring_security.config.security.oAuth2.dto;

import java.util.Map;

public class KakaoOAuth2UserDto extends OAuth2UserInfo {

    public KakaoOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile == null ? null : (String) profile.get("nickname");
    }

    @Override
    public String getProfileUrl() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile == null ? null : String.valueOf(profile.get("thumbnail_image_url"));
    }
}
