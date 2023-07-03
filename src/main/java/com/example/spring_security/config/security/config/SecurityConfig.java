package com.example.spring_security.config.security.config;

import com.example.spring_security.config.security.jwt.Service.JwtService;
import com.example.spring_security.config.security.jwt.exception.AccessDeniedHandlerImpl;
import com.example.spring_security.config.security.jwt.exception.AuthenticationEntryPointImpl;
import com.example.spring_security.config.security.jwt.filter.BeforeFilter;
import com.example.spring_security.config.security.jwt.filter.JwtAuthenticationFilter;
import com.example.spring_security.config.security.jwt.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @package com.example.spring_security.config.security.config
 * @class   SpringConfig
 * @brief   SpringSecurituy
 * @author  최원호
 * @date    2023.06.22
 * @version  1.0
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;

    private final CorsConfig corsConfig;

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers( "/", "/common/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적인 리소스들에 대해서 시큐리티 적용 무시.
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(new BeforeFilter(), BasicAuthenticationFilter.class);
        http.csrf().disable();
        http    .addFilter(corsConfig.corsFilter())                                             //인증(O), security Filter에 등록
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     //세션 사용 x
                .and()
                .formLogin().disable()                                                          // Security에서 제공하는 form로그인 사용 X
                .httpBasic().disable()                                                          // Http basic Auth  기반으로 로그인 인증창사용 X
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService))
                .authorizeRequests()
                .antMatchers("/user/**")
                .hasAuthority("ROLE_USER")
                .antMatchers("/manager/**")
                .hasAuthority("ROLE_MANAGER")

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl);

    }


    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception{

        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(jwtService, authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/login");
        return authenticationFilter;
    }
}
