package com.practice.springbasic.controller.exhandler;

import com.auth0.jwt.exceptions.*;
import com.practice.springbasic.controller.utils.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.practice.springbasic.controller")
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalArgumentExHandler(IllegalArgumentException e) {
        return new ErrorResult("BAD_REQUEST", e.getMessage(), 400);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AlgorithmMismatchException.class)
    public ErrorResult jwtDecodeExHandler(JWTDecodeException e) {
        return new ErrorResult("BAD_REQUEST", "잘못된 알고리즘의 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResult jwtDecodeExHandler(TokenExpiredException e) {
        return new ErrorResult("BAD_REQUEST", "만료된 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClaimException.class)
    public ErrorResult jwtDecodeExHandler(InvalidClaimException e) {
        return new ErrorResult("BAD_REQUEST", "경고 정상적이지 않은 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureVerificationException.class)
    public ErrorResult jwtDecodeExHandler(SignatureVerificationException e) {
        return new ErrorResult("BAD_REQUEST", "경고 정상적이지 않은 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public ErrorResult jwtDecodeExHandler(JWTVerificationException e) {
        return new ErrorResult("UNAUTHORIZED", "인증할 수 없는 토큰", 401);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResult runtimeExHandler(RuntimeException e) {
        return new ErrorResult("BAD_REQUEST", "경고 정상적이지 않은 접근", 403);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        return new ErrorResult("UNKNOWN_ERROR", "알 수 없는 오류", 500);
    }
}
