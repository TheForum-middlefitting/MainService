package com.practice.springbasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.practice.springbasic.domain.Member;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
public class JwtUtils {

    private JwtUtils() throws InstantiationException{
        throw new InstantiationException();
    }
    public static String generateAccessJwtToken(Member member) {
        return  JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("nickname", member.getNickname())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public static String generateRefreshJwtToken(Member member) {
        return  JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("nickname", member.getNickname())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public static void verifyJwtToken(HttpServletRequest request, Long id, String tokenType) {
        String jwtToken = request.getHeader(tokenType).replace(JwtProperties.TOKEN_PREFIX, "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken);
        Long verifyId = verify.getClaim("id").asLong();
        if(!verifyId.equals(id)) {
            throw new RuntimeException();
        }
    }

    public static String generateExtendJwtToken(HttpServletRequest request, Long id) {
        try {
            verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
        } catch (TokenExpiredException e) {
            verifyJwtToken(request, id, JwtProperties.REFRESH_HEADER_STRING);
            String accessToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
            String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
            DecodedJWT decodeInfo = JWT.decode(accessToken);
            String refreshId = JWT.decode(refreshToken).getClaim("id").toString();
            if (decodeInfo.getClaim("id").toString().equals(refreshId)) {
                return  JWT.create()
                        .withSubject(decodeInfo.getClaim("email").toString())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                        .withClaim("id", decodeInfo.getClaim("id").toString())
                        .withClaim("nickname", decodeInfo.getClaim("nickname").toString())
                        .sign(Algorithm.HMAC512(JwtProperties.SECRET));
                }
            }
        throw new IllegalArgumentException("토큰이 아직 만료되지 않았습니다!");
        }
}
