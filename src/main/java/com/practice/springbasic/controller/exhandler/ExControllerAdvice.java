package com.practice.springbasic.controller.exhandler;

import com.auth0.jwt.exceptions.*;
import com.practice.springbasic.controller.utils.form.ErrorResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "com.practice.springbasic.controller")
public class ExControllerAdvice {
    public String[] errorMsgParse(String message) {
        String[] result = {"", ""};
        String[] err = message.split("@", 2);
        if (err.length == 2) {
            result = new String[]{err[0], err[1]};
        }
        return result;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalArgumentExHandler(IllegalArgumentException e) {
        String[] errorArr = errorMsgParse(e.getMessage());
        return new ErrorResult(errorArr[0], errorArr[1], 400);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResult constraintViolationExHandler(ConstraintViolationException e) {
        String[] errorArr = e.getMessage()
                .split(",", 2)[0]
                .split(":", 2)[1]
                .trim().split("@");
//        return new ErrorResult("BAD_REQUEST", "입력 값이 잘못되었습니다", 400);
        String message = errorArr.length == 2 ? errorArr[1] : "";
        String code = errorArr.length == 2 ? errorArr[0] : "";
        return new ErrorResult(code, message, 400);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AlgorithmMismatchException.class)
    public ErrorResult jwtDecodeExHandler(JWTDecodeException e) {
        return new ErrorResult("UNAUTHORIZED", "잘못된 알고리즘의 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResult jwtDecodeExHandler(TokenExpiredException e) {
        return new ErrorResult("UNAUTHORIZED", "만료된 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClaimException.class)
    public ErrorResult jwtDecodeExHandler(InvalidClaimException e) {
        return new ErrorResult("UNAUTHORIZED", "경고 정상적이지 않은 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureVerificationException.class)
    public ErrorResult jwtDecodeExHandler(SignatureVerificationException e) {
        return new ErrorResult("UNAUTHORIZED", "경고 정상적이지 않은 토큰", 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public ErrorResult jwtDecodeExHandler(JWTVerificationException e) {
        return new ErrorResult("UNAUTHORIZED", "인증할 수 없는 토큰", 401);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResult runtimeExHandler(RuntimeException e) {
        return new ErrorResult("FORBIDDEN", "경고 정상적이지 않은 접근", 403);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult exHandler(HttpClientErrorException.NotFound e) {
        return new ErrorResult("UNKNOWN_ERROR", "알 수 없는 오류", 404);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult exHandler(NullPointerException e) {
        return new ErrorResult("UNKNOWN_ERROR", e.getMessage(), 404);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResult dataIntegrityViolationExHandler(DataIntegrityViolationException e) {
        return new ErrorResult("INTERNAL_SERVER_ERROR", "내부 서버 오류, 잠시 후 다시 시도해 주세요", 500);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        return new ErrorResult("UNKNOWN_ERROR", "알 수 없는 오류", 500);
    }
}
