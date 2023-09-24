package com.example.spring_security.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String homeController(){

        return "/views/index";
    }

    @GetMapping("/common/loginForm")
    public String loginForm(){

        return "/views/user/loginForm";
    }

    @GetMapping("/common/joinUserForm")
    public String joinUserForm() {

        return "/views/user/joinUserForm";
    }

    @PostMapping("/user/main")
    @ResponseBody
    public ModelAndView mainForm(){

        log.info("asdasd");

        return new ModelAndView("/views/main");
    }
}
