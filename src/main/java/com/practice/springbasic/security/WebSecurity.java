package com.practice.springbasic.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //모든 요청 허용
        http.authorizeRequests().antMatchers("/**").permitAll();
        //프레임옵션 제거하여 h2 나누어지는 것 무시
        http.headers().frameOptions().disable();

    }
}
