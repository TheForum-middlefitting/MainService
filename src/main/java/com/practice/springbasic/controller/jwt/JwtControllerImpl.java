package com.practice.springbasic.controller.jwt;

import com.practice.springbasic.utils.jwt.JwtUtils;
import com.practice.springbasic.controller.utils.form.SuccessResult;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member-service")
public class JwtControllerImpl implements JwtController{

    private final JwtUtils jwtUtils;
    private final Environment env;

    public JwtControllerImpl(JwtUtils jwtUtils, Environment env) {
        this.jwtUtils = jwtUtils;
        this.env = env;
    }

    @Override
    @GetMapping("/tokens/{id}")
    public SuccessResult updateAccessToken(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        String newToken = jwtUtils.generateExtendJwtToken(request, id);
        response.addHeader(env.getProperty("token.ACCESS_HEADER_STRING"), env.getProperty("token.TOKEN_PREFIX") + newToken);
        return new SuccessResult(null);
    }
}
