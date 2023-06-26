package com.example.spring_security.user.controller;

import com.example.spring_security.user.dto.UserSignupDto;
import com.example.spring_security.user.dto.UserSignupDto.UserSignupRequestDto;
import com.example.spring_security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * @brief   회원가입
     * @param   userSignupRequestDto    회원가입 요청 dto
     * @return  resultMap               회원가입 결과
     */
    @PostMapping("/common/sign-up")
    public Map<String, Object> signUp(@RequestBody UserSignupRequestDto userSignupRequestDto) {

        userService.insertUser(userSignupRequestDto);

        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("resultCode", "200");
        resultMap.put("resultMessage", "회원가입이 성공적으로 진행되었습니다");

        return resultMap;
    }
}
