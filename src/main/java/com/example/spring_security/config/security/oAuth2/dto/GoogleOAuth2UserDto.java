package com.example.spring_security.config.security.oAuth2.dto;

import java.util.Map;

public class GoogleOAuth2UserDto extends OAuth2UserInfo{


    public GoogleOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileUrl() {
        return (String) attributes.get("picture");
    }
}
