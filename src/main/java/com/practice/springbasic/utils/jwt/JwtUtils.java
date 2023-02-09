package com.practice.springbasic.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.utils.check.CommonCheckUtil;
import com.practice.springbasic.utils.error.exception.AuthenticationFailedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {

    private JwtUtils() throws InstantiationException {
        throw new InstantiationException();
    }

    public static String generateExtendJwtToken(HttpServletRequest request, Long id) {
        try {
            verifyJwtTokenAndAuthority(request, id, JwtProperties.ACCESS_HEADER_STRING);
        } catch (TokenExpiredException ignored) {

        }
        verifyJwtTokenAndAuthority(request, id, JwtProperties.REFRESH_HEADER_STRING);
        String accessToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        sameTokenMemberCheck(accessToken, refreshToken);
        return generateAccessJwtToken(JWT.decode(accessToken).getClaim("id").asLong(), JWT.decode(accessToken).getSubject());
    }

    public static void verifyJwtTokenAndAuthority(HttpServletRequest request, Long id, String tokenType) {
        Long verifyId = verifyJwtToken(request, tokenType);
        if (!verifyId.equals(id)) {
            throw new AuthenticationFailedException("AuthFailed");
        }
    }

    public static Long verifyJwtToken(HttpServletRequest request, String tokenType) {
        CommonCheckUtil.nullCheck404(request.getHeader(tokenType), "NoToken");
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
