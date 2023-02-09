package com.practice.springbasic.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.practice.springbasic.utils.check.CommonCheckUtil;
import com.practice.springbasic.utils.error.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

//@Component
public class JwtUtils {

    //다른 클래스와 교체할 수가 없기는 하지만 Environment는 교체할 필요가 없다.
    @Autowired
    private Environment env;

    //    private JwtUtils() throws InstantiationException {
//        throw new InstantiationException();
//    }

    public String generateExtendJwtToken(HttpServletRequest request, Long id) {
        try {
            verifyJwtTokenAndAuthority(request, id, env.getProperty("token.ACCESS_HEADER_STRING"));
        } catch (TokenExpiredException ignored) {

        }
        verifyJwtTokenAndAuthority(request, id, env.getProperty("token.REFRESH_HEADER_STRING"));
        String accessToken = request.getHeader(env.getProperty("token.ACCESS_HEADER_STRING")).replace(Objects.requireNonNull(env.getProperty("token.TOKEN_PREFIX")), "");
        String refreshToken = request.getHeader(env.getProperty("token.REFRESH_HEADER_STRING")).replace(Objects.requireNonNull(env.getProperty("token.TOKEN_PREFIX")), "");
        sameTokenMemberCheck(accessToken, refreshToken);
        return generateAccessJwtToken(JWT.decode(accessToken).getClaim("id").asLong(), JWT.decode(accessToken).getSubject());
    }

    public void verifyJwtTokenAndAuthority(HttpServletRequest request, Long id, String tokenType) {
        Long verifyId = verifyJwtToken(request, tokenType);
        if (!verifyId.equals(id)) {
            throw new AuthenticationFailedException("AuthFailed");
        }
    }

    public Long verifyJwtToken(HttpServletRequest request, String tokenType) {
        CommonCheckUtil.nullCheck404(request.getHeader(tokenType), "NoToken");
        String jwtToken = request.getHeader(tokenType).replace(Objects.requireNonNull(env.getProperty("token.TOKEN_PREFIX")), "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(Objects.requireNonNull(tokenType.equals(env.getProperty("token.ACCESS_HEADER_STRING")) ? env.getProperty("token.ACCESS_SECRET") : env.getProperty("token.REFRESH_SECRET")))).build().verify(jwtToken);
        return verify.getClaim("id").asLong();
    }

    public String getTokenEmail(HttpServletRequest request, String tokenType) {
        String jwtToken = request.getHeader(tokenType).replace(Objects.requireNonNull(env.getProperty("token.TOKEN_PREFIX")), "");
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(Objects.requireNonNull(tokenType.equals(env.getProperty("token.ACCESS_HEADER_STRING")) ? env.getProperty("token.ACCESS_SECRET") : env.getProperty("token.REFRESH_SECRET")))).build().verify(jwtToken);
        System.out.println(verify.getSubject());
        return verify.getSubject();
    }

    public void sameTokenMemberCheck(String accessToken, String refreshToken) {
        String accessId = JWT.decode(accessToken).getClaim("id").toString();
        String refreshId = JWT.decode(refreshToken).getClaim("id").toString();
        if (!accessId.equals(refreshId)) {
            throw new RuntimeException();
        }
    }

    public String generateAccessJwtToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(Objects.requireNonNull(env.getProperty("token.ACCESS_SECRET"))));
    }

    public String generateRefreshJwtToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.REFRESH_EXPIRATION_TIME"))))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(Objects.requireNonNull(env.getProperty("token.REFRESH_SECRET"))));
    }

}
