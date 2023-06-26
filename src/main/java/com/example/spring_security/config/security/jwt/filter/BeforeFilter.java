package com.example.spring_security.config.security.jwt.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class BeforeFilter implements Filter {

    /**
     * @brief  인증 전 필터
     * @return 인증 전에 실행하는 필터
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("BeforeFilter");
        chain.doFilter(request, response);
    }
}
