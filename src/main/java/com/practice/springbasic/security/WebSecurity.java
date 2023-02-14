package com.practice.springbasic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.cors().configurationSource(corsConfigurationSource());
        http.formLogin().disable();
        http.httpBasic().disable();
//        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/**").permitAll();
//        .access("hasIpAddress('127.0.0.1') or hasIpAddress('localhost') or hasIpAddress('192.168.219.109')");
        //프레임옵션 제거하여 h2 나누어지는 것 무시
        http.headers().frameOptions().disable();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        8000에서 다이렉트로 요청보내면 허용이 된다.
//        리엑트에서 거쳐서 보내는 것을 허용하기 위햐서는 8000 포트를 거쳐서 오는 것을 허용해주는 것으로 보인다.
//        이후 docker를 통해 추후 서버를 분리할 때, IP 기반으로 차단하도록 수행한다.
        configuration.addAllowedOrigin("http://127.0.0,1:8000");
        configuration.addAllowedOrigin("http://localhost:8000");
        configuration.addAllowedOrigin("http://192.168.219.109:8000");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

