package com.practice.springbasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {
    String accessToken;
    String refreshToken;
    String accessToken2;
    String refreshToken2;
    String secretFailToken;
    String expireToken;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    Environment env;

    @BeforeEach
    public void initialize() {
        accessToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.ACCESS_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        accessToken2 = tokenExample("middlefitting@gmail.com", null, env.getProperty("token.ACCESS_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));

        refreshToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        refreshToken2 = tokenExample("middlefitting@gmail.com", null, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));

        secretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))));
        expireToken = tokenExample("middlefitting@gmail.com", 1L, env.getProperty("token.REFRESH_SECRET"), new Date(System.currentTimeMillis() - 60000));
    }

    @Test
    public void generateRefreshJwtTokenSuccess() throws Exception{
        Long id = 1L;
        String email = "middlefitting@gmaail.com";

        String refreshToken = jwtUtils.generateRefreshJwtToken(1L, "middlefitting@gmaail.com");

        assertThat(JWT.decode(refreshToken).getClaim("id").asLong()).isEqualTo(id);
        assertThat(JWT.decode(refreshToken).getSubject().toString()).isEqualTo(email);
    }

    @Test
    public void generateAccessJwtTokenSuccess() throws Exception{
        Long id = 1L;
        String email = "middlefitting@gmaail.com";

        String refreshToken = jwtUtils.generateAccessJwtToken(1L, "middlefitting@gmaail.com");

        assertThat(JWT.decode(refreshToken).getClaim("id").asLong()).isEqualTo(id);
        assertThat(JWT.decode(refreshToken).getSubject().toString()).isEqualTo(email);
    }

    @Test
    public void sameTokenCheckSuccess() throws Exception{
        jwtUtils.sameTokenMemberCheck(accessToken, refreshToken);
        jwtUtils.sameTokenMemberCheck(accessToken2, refreshToken2);
    }

    @Test
    public void sameTokenCheckFailed() throws Exception{
        assertThrows(RuntimeException.class, () -> {jwtUtils.sameTokenMemberCheck(accessToken, accessToken2);});
        assertThrows(RuntimeException.class, () -> {jwtUtils.sameTokenMemberCheck(accessToken2, accessToken);});
    }

    public String tokenExample(String email, Long id, String secret, Date date) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(date)
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secret));
    }
}