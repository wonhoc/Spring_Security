package com.example.spring_security.config.security.config;

import com.example.spring_security.config.security.jwt.Service.JwtService;
import com.example.spring_security.config.security.jwt.exception.AuthenticationEntryPointImpl;
import com.example.spring_security.config.security.jwt.filter.BeforeFilter;
import com.example.spring_security.config.security.jwt.filter.JwtAuthenticationFilter;
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

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;

    private final CorsConfig corsConfig;

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

    @Override
    public void configure(WebSecurity web) throws Exception {
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
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(authenticationFilter())
                .authorizeRequests()

                .antMatchers("**/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("**/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl);

    }


    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception{

        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(jwtService, authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/login");
        return authenticationFilter;
    }
}
