package com.practice.springbasic.controller.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.config.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(JwtControllerImpl.class)
class JwtControllerImplTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
        accessToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Access_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        accessToken2 = tokenExample("middlefitting@gmail.com", null, JwtProperties.Access_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));

        refreshToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        refreshToken2 = tokenExample("middlefitting@gmail.com", null, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));

        accessSecretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        accessExpireToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Access_SECRET, new Date(System.currentTimeMillis() - 60000));

        refreshSecretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        refreshExpireToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() - 60000));
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

//    @Test
//    public void updateAccessTokenFailedByNotExpired() throws Exception{
//        ResultActions resultActions = makeResultActions("/tokens/1", accessToken, refreshToken);
//        resultActions
//                .andExpect(status().isBadRequest())
//                .andExpect(header().doesNotExist("Authorization"))
//                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
//                .andExpect(jsonPath("$.message", equalTo("토큰이 아직 만료되지 않았습니다!")))
//                .andExpect(jsonPath("$.status").value(400));
//    }

    @Test
    public void updateAccessTokenFailedByBothExpired() throws Exception{
        ResultActions resultActions = makeResultActions("/member-service/tokens/1", accessExpireToken, refreshExpireToken);
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.code", equalTo("UNAUTHORIZED")))
                .andExpect(jsonPath("$.message", equalTo("만료된 토큰")))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    public void updateAccessTokenFailedById() throws Exception{
        ResultActions resultActions = makeResultActions("/member-service/tokens/2", accessToken, refreshToken);
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.code", equalTo("FORBIDDEN")))
                .andExpect(jsonPath("$.message", equalTo("경고 정상적이지 않은 접근")))
                .andExpect(jsonPath("$.status").value(403));
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