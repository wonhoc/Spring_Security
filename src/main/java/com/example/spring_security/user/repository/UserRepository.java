package com.example.spring_security.user.repository;

import com.example.spring_security.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
