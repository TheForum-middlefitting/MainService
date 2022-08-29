package com.practice.springbasic.controller.jwt;

import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.config.jwt.JwtUtils;
import com.practice.springbasic.controller.form.SuccessResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class JwtControllerImpl implements JwtController{

    @Override
    @GetMapping("/tokens/{id}")
    public SuccessResult updateAccessToken(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        String newToken = JwtUtils.generateExtendJwtToken(request, id);
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX + newToken);
        return new SuccessResult(null);
    }
}
