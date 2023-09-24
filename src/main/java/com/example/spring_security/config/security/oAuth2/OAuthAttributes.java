package com.example.spring_security.config.security.oAuth2;

import com.example.spring_security.config.security.oAuth2.dto.GoogleOAuth2UserDto;
import com.example.spring_security.config.security.oAuth2.dto.KakaoOAuth2UserDto;
import com.example.spring_security.config.security.oAuth2.dto.OAuth2UserInfo;
import com.example.spring_security.user.common.SocialType;
import com.example.spring_security.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAttributes {

    private String nameAttributeKey;        // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oAuth2UserInfo;  // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    public OAuthAttributes of(SocialType socialType, String nameAttributeKey, Map<String, Object> attributes) {

        if (socialType == SocialType.KAKAO) {
            return ofKakao(nameAttributeKey, attributes);
        } else if (socialType == SocialType.GOOGLE) {
            return ofGoogle(nameAttributeKey, attributes);
        }
        return null;
    }

    private OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserDto(attributes))
                .build();
    }

    private OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserDto(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserDto) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserDto.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oAuth2UserDto.getNickname())
                .profileUrl(oAuth2UserDto.getProfileUrl())
                .userRole("ROLE_USER")
                .build();
    }
}
