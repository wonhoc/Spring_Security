package com.example.spring_security.user.repository;

import com.example.spring_security.user.common.SocialType;
import com.example.spring_security.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @package     com.example.spring_security.user.repository
 * @interface   UserRepository
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    @Query("select user from User user where user.socialType = :socialType and user.socialId = :socialId")
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
