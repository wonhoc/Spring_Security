package com.example.spring_security.config.security.auth;

import com.example.spring_security.config.security.jwt.dto.LoginDto;
import com.example.spring_security.config.security.jwt.dto.LoginDto.LoginResponseDto;
import com.example.spring_security.user.domain.User;
import com.example.spring_security.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @package com.example.spring_security.config.security.auth
 * @class   PrincipalDetailsService
 * @brief   사용자의 정보를 가져오기 위한 비즈니스 로직
 * @author  최원호
 * @date    2023.06.22
 * version  1.0
 */

@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow();

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickName(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .refreshToken(user.getRefreshToken())
                .userRole(user.getUserRole())
                .build();

        return new PrincipalDetails(loginResponseDto);
    }
}
