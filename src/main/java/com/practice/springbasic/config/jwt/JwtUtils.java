package com.practice.springbasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {

    private JwtUtils() throws InstantiationException {
        throw new InstantiationException();
    }

//    public static String generateExtendJwtToken(HttpServletRequest request, Long id) {
//        try {
//            verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
//        } catch (TokenExpiredException e) {
//            verifyJwtToken(request, id, JwtProperties.REFRESH_HEADER_STRING);
//            String accessToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
//            String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
//            sameTokenMemberCheck(accessToken, refreshToken);
//            return generateRefreshJwtToken(JWT.decode(accessToken).getClaim("id").asLong(), JWT.decode(accessToken).getClaim("email").toString());
//        }
//        throw new IllegalArgumentException("토큰이 아직 만료되지 않았습니다!");
//    }

    public static String generateExtendJwtToken(HttpServletRequest request, Long id) {
        try {
            verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
        } catch (TokenExpiredException ignored) {

        }
        verifyJwtToken(request, id, JwtProperties.REFRESH_HEADER_STRING);
        String accessToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        sameTokenMemberCheck(accessToken, refreshToken);
        return generateAccessJwtToken(JWT.decode(accessToken).getClaim("id").asLong(), JWT.decode(accessToken).getSubject().toString());
    }

    public static void verifyJwtToken(HttpServletRequest request, Long id, String tokenType) {
        String jwtToken = request.getHeader(tokenType).replace(JwtProperties.TOKEN_PREFIX, "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(tokenType.equals(JwtProperties.ACCESS_HEADER_STRING) ? JwtProperties.Access_SECRET : JwtProperties.Refresh_SECRET)).build().verify(jwtToken);
        Long verifyId = verify.getClaim("id").asLong();
        if (!verifyId.equals(id)) {
            throw new RuntimeException();
        }
    }

    public static Long verifyJwtToken(HttpServletRequest request, String tokenType) {
        String jwtToken = request.getHeader(tokenType).replace(JwtProperties.TOKEN_PREFIX, "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(tokenType.equals(JwtProperties.ACCESS_HEADER_STRING) ? JwtProperties.Access_SECRET : JwtProperties.Refresh_SECRET)).build().verify(jwtToken);
        return verify.getClaim("id").asLong();
    }

    public static String getTokenEmail(HttpServletRequest request, String tokenType) {
        String jwtToken = request.getHeader(tokenType).replace(JwtProperties.TOKEN_PREFIX, "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(tokenType.equals(JwtProperties.ACCESS_HEADER_STRING) ? JwtProperties.Access_SECRET : JwtProperties.Refresh_SECRET)).build().verify(jwtToken);
        System.out.println(verify.getSubject());
        return verify.getSubject();
    }

    public static void sameTokenMemberCheck(String accessToken, String refreshToken) {
        String accessId = JWT.decode(accessToken).getClaim("id").toString();
        String refreshId = JWT.decode(refreshToken).getClaim("id").toString();
        if (!accessId.equals(refreshId)) {
            throw new RuntimeException();
        }
    }

    public static String generateAccessJwtToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));
    }

    public static String generateRefreshJwtToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(JwtProperties.Refresh_SECRET));
    }

}
