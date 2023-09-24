package com.example.spring_security.config.security.oAuth2.service;

import com.example.spring_security.config.security.oAuth2.CustomOAuth2User;
import com.example.spring_security.config.security.oAuth2.OAuthAttributes;
import com.example.spring_security.config.security.oAuth2.dto.OAuth2UserInfo;
import com.example.spring_security.user.common.SocialType;
import com.example.spring_security.user.domain.User;
import com.example.spring_security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

import static com.example.spring_security.user.common.SocialType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService {

    private final UserRepository userRepository;
    /**
     * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
     * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
     * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
     * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        OAuthAttributes oAuthAttributes = new OAuthAttributes();
        oAuthAttributes = oAuthAttributes.of(socialType, userNameAttributeName, attributes);

        User createdUser = getUser(oAuthAttributes, socialType); //

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getUserRole())),
                attributes,
                oAuthAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getUserRole()
        );
    }

    private SocialType getSocialType(String registrationId) {

        if (String.valueOf(KAKAO).equals(registrationId.toUpperCase()))       return KAKAO;
        else if (String.valueOf(GOOGLE).equals(registrationId.toUpperCase())) return GOOGLE;

        return NONE;
    }

    @Transactional
    public User getUser(OAuthAttributes oAuthAttributes, SocialType socialType) {

        User user = userRepository.findBySocialTypeAndSocialId(socialType, oAuthAttributes.getOAuth2UserInfo().getId()).orElse(null);

        if (user == null) {
            return createUser(socialType, oAuthAttributes);
        }

        if (user.getProfileUrl() != oAuthAttributes.getOAuth2UserInfo().getProfileUrl()) {
            user.updateProfileUrl(oAuthAttributes.getOAuth2UserInfo().getProfileUrl());
        }

        if (user.getNickname() != oAuthAttributes.getOAuth2UserInfo().getNickname()) {
            user.updateNickname(oAuthAttributes.getOAuth2UserInfo().getNickname());
        }

        return user;
    }

    private User createUser(SocialType socialType, OAuthAttributes oAuthAttributes) {

        return userRepository.save(oAuthAttributes.toEntity(socialType, oAuthAttributes.getOAuth2UserInfo()));
    }
}
