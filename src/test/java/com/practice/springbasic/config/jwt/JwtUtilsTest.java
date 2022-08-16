package com.practice.springbasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.auth0.jwt.JWT.decode;
import static com.practice.springbasic.config.jwt.JwtUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    String accessToken;
    String refreshToken;
    String accessToken2;
    String refreshToken2;
    String secretFailToken;
    String expireToken;

    //.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
    @BeforeEach
    public void initialize() {
        accessToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Access_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        accessToken2 = tokenExample("middlefitting@gmail.com", null, JwtProperties.Access_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));

        refreshToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        refreshToken2 = tokenExample("middlefitting@gmail.com", null, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));

        secretFailToken = tokenExample("middlefitting@gmail.com", 1L, "helloWorld!", new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME));
        expireToken = tokenExample("middlefitting@gmail.com", 1L, JwtProperties.Refresh_SECRET, new Date(System.currentTimeMillis() - 60000));
    }

    @Test
    public void generateRefreshJwtTokenSuccess() throws Exception{
        Long id = 1L;
        String email = "middlefitting@gmaail.com";

        String refreshToken = generateRefreshJwtToken(1L, "middlefitting@gmaail.com");

        assertThat(JWT.decode(refreshToken).getClaim("id").asLong()).isEqualTo(id);
        assertThat(JWT.decode(refreshToken).getSubject().toString()).isEqualTo(email);
    }

    @Test
    public void generateAccessJwtTokenSuccess() throws Exception{
        Long id = 1L;
        String email = "middlefitting@gmaail.com";

        String refreshToken = generateAccessJwtToken(1L, "middlefitting@gmaail.com");

        assertThat(JWT.decode(refreshToken).getClaim("id").asLong()).isEqualTo(id);
        assertThat(JWT.decode(refreshToken).getSubject().toString()).isEqualTo(email);
    }

    @Test
    public void sameTokenCheckSuccess() throws Exception{
        sameTokenMemberCheck(accessToken, refreshToken);
        sameTokenMemberCheck(accessToken2, refreshToken2);
    }

    @Test
    public void sameTokenCheckFailed() throws Exception{
        assertThrows(RuntimeException.class, () -> {sameTokenMemberCheck(accessToken, accessToken2);});
        assertThrows(RuntimeException.class, () -> {sameTokenMemberCheck(accessToken2, accessToken);});
    }

    public String tokenExample(String email, Long id, String secret, Date date) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(date)
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secret));
    }
}