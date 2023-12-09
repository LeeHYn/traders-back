package com.traders.tradersback.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeRequests()
                .antMatchers("/api/members/**").permitAll()  //보안 해제
                .antMatchers("/api/email/**").permitAll()
                .antMatchers("/api/products/**").permitAll()
                .antMatchers("/api/categories/**").permitAll()
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
                .and()
                .formLogin()
                .loginPage("/login").permitAll()  // 로그인 페이지 설정 (필요한 경우)
                .and()
                .logout()
                .permitAll();  // 로그아웃 설정 (필요한 경우)
    }
}
