package com.traders.tradersback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeRequests()
                .antMatchers("/api/members/**").permitAll()  //보안 해제
                .antMatchers("/api/**").permitAll()  //보안 해제
                .antMatchers("/api/email/**").permitAll()
                .antMatchers("/api/products/**").permitAll()
                .antMatchers("/images/**").permitAll()  // 이미지 경로에 대한 보안 해제
                .antMatchers("/api/categories/**").permitAll()
                .antMatchers("/api/fraud/search").permitAll()  // 상품 추가 API는 인증 필요
                .antMatchers("/api/products/add").authenticated()  // 상품 추가 API는 인증 필요
                .antMatchers("/api/fraud/report").authenticated()  // 상품 추가 API는 인증 필요
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
                .and()
                .formLogin()
                //.loginPage("/login").permitAll()  // 로그인 페이지 설정 (필요한 경우)
                .and()
                .logout()
                .permitAll();  // 로그아웃 설정 (필요한 경우)
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 클라이언트 주소
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
