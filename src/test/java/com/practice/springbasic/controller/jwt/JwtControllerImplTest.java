package com.practice.springbasic.controller.jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Objects;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(JwtControllerImpl.class)
@TestPropertySource(locations = "classpath:test.properties")
class JwtControllerImplTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment env;

    @Autowired
    JwtUtils jwtUtils;
    String accessToken;
    String refreshToken;
    String accessToken2;
    String refreshToken2;
    String accessSecretFailToken;
    String accessExpireToken;
    String refreshSecretFailToken;
    String refreshExpireToken;

    @BeforeEach
    public void initialize() {
        accessToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.ACCESS_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        accessToken2 = tokenExample("middlefitting@gmail.com", null, env.getProperty("token.ACCESS_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));

        refreshToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        refreshToken2 = tokenExample("middlefitting@gmail.com", null, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));

        accessSecretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        accessExpireToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.ACCESS_SECRET"), new Date(System.currentTimeMillis() - 60000));

        refreshSecretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        refreshExpireToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() - 60000));
    }

    @Test
    public void updateAccessTokenSuccess() throws Exception{
        ResultActions resultActions = makeResultActions("/member-service/tokens/1", accessExpireToken, refreshToken);
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    public void updateAccessTokenFailedByBothExpired() throws Exception{
        ResultActions resultActions = makeResultActions("/member-service/tokens/1", accessExpireToken, refreshExpireToken);
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("TokenExpired.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("TokenExpired.msg"))))))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    public void updateAccessTokenFailedById() throws Exception{
        ResultActions resultActions = makeResultActions("/member-service/tokens/2", accessToken, refreshToken);
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.msg"))))))
                .andExpect(jsonPath("$.status").value(401));
    }

    ResultActions makeResultActions(String url, String accessToken, String refreshToken) throws Exception {
        return mockMvc.perform(get(url)
                        .header("Authorization", accessToken)
                        .header("refresh", refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    public String tokenExample(String email, Long id, String secret, Date date) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(date)
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secret));
    }
}