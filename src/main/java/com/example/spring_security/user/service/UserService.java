package com.example.spring_security.user.service;

import com.example.spring_security.user.dto.UserSignupDto.UserSignupRequestDto;

/**
 * @package     com.example.spring_security.user.service
 * @interface   UserService
 * @brief       user 서비스
 * @author      최원호
 * @date        2023.06.22
 * version      1.0
 */
public interface UserService {

    void insertUser(UserSignupRequestDto userSignupRequestDto);
}
