package com.practice.springbasic.controller.jwt;

import com.practice.springbasic.controller.form.SuccessResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtController {
    SuccessResult updateAccessToken(HttpServletRequest request, HttpServletResponse response, Long id);
}
