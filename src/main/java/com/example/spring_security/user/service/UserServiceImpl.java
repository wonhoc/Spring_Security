package com.example.spring_security.user.service;

import com.example.spring_security.user.common.SocialType;
import com.example.spring_security.user.domain.User;
import com.example.spring_security.user.dto.UserSignupDto.UserSignupRequestDto;
import com.example.spring_security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @package     com.example.spring_security.user.service
 * @interface   UserServiceIMpl
 * @brief       UserService 상속 클래스
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * @brief   user 정보 저장
     * @param   userSignupRequestDto    회원가입 요청 Dto
     */
    @Override
    @Transactional
    public void insertUser(UserSignupRequestDto userSignupRequestDto) {

        User user = User.builder()
                .email(userSignupRequestDto.getEmail())
                .password(userSignupRequestDto.getPassword())
                .nickname(userSignupRequestDto.getNickname())
                .socialType(SocialType.NONE)
                .build();

        user.passwordEncode(bCryptPasswordEncoder);

        userRepository.save(user);
    }
}
