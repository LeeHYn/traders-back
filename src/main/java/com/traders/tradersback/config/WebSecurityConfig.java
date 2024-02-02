package com.traders.tradersback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()  // CORS 설정 활성화
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeRequests()
                // 인증이 필요한 경로 먼저 설정
                .antMatchers("/api/products/add", "/api/fraud/report").authenticated()
                // 그 외 모든 요청에 대해서는 접근 허용
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()  // 필요한 경우 로그인 설정
                .logout().permitAll();  // 로그아웃 설정
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // 허용할 Origin
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 허용할 HTTP 메소드
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));  // 허용할 헤더
        config.setAllowCredentials(true);  // 쿠키를 포함한 요청 허용
        source.registerCorsConfiguration("/**", config);  // 모든 경로에 대해 이 CORS 정책 적용
        return new CorsFilter(source);
    }
}



