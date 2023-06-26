package com.example.spring_security.user.domain;

import com.example.spring_security.user.common.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


/**
 * @package com.example.spring_security.user.domain
 * @class   User
 * @brief   user Entity
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, name = "USER_ID")
    private long userId;

    @Column(unique = true, nullable = false, name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "PROFILE_URL")
    private String profileUrl;

    @Column(name = "REFRESH_TOKEN", length = 1000)
    private String refreshToken;

    @Builder.Default
    @Column(name = "USER_ROLE")
    private String userRole = "ROLE_USER";

    @Enumerated(EnumType.STRING)
    @Column
    private SocialType socialType;

    @Column(name = "socialId")
    private String socialId; // 소셜 타입의 식별자 값

    /**
     * @brief   refreshToken 재설정 메서드
     * @param   refreshToken    토큰
     */
    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    /**
     * @brief   비밀번호 암호화 메서드
     * @param   passwordEncoder    패스워드 암호 클래스
     */
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
}
