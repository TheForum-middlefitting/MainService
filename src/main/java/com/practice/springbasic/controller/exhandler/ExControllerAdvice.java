package com.practice.springbasic.controller.exhandler;

import com.auth0.jwt.exceptions.*;
import com.practice.springbasic.controller.utils.form.ErrorResult;
import com.practice.springbasic.utils.error.exception.AuthenticationFailedException;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import javax.validation.ConstraintViolationException;

import java.util.Objects;

@RestControllerAdvice(basePackages = "com.practice.springbasic.controller")
public class ExControllerAdvice {

    private final Environment env;

    public ExControllerAdvice(Environment env) {
        this.env = env;
    }

    public String[] errorMsgParse(String message) {
        String code;
        String msg;

        try {
            code = String.format(Objects.requireNonNull(this.env.getProperty(message + ".code")));
        } catch (NullPointerException e) {
            code = String.format(Objects.requireNonNull(env.getProperty("Unexpected.code")));
        }
        try {
            msg = String.format(Objects.requireNonNull(this.env.getProperty(message + ".msg")));
        } catch (NullPointerException e) {
            msg = String.format(Objects.requireNonNull(env.getProperty("Unexpected.msg")));
        }
        return new String[]{code, msg};
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
        String message = e.getMessage()
                .split(",", 2)[0]
                .split(":", 2)[1]
                .trim();
        String[] errorArr = errorMsgParse(message);
        return new ErrorResult(errorArr[0], errorArr[1], 400);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailedException.class)
    public ErrorResult authenticationFailedExHandler(AuthenticationFailedException e) {
        String[] errorArr = errorMsgParse(e.getMessage());
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AlgorithmMismatchException.class)
    public ErrorResult jwtDecodeExHandler(JWTDecodeException e) {
        String[] errorArr = errorMsgParse("JWTDecodeFailed");
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResult jwtDecodeExHandler(TokenExpiredException e) {
        String[] errorArr = errorMsgParse("TokenExpired");
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClaimException.class)
    public ErrorResult jwtDecodeExHandler(InvalidClaimException e) {
        String[] errorArr = errorMsgParse("InvalidClaim");
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureVerificationException.class)
    public ErrorResult jwtDecodeExHandler(SignatureVerificationException e) {
        String[] errorArr = errorMsgParse("SignatureVerificationFailed");
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public ErrorResult jwtDecodeExHandler(JWTVerificationException e) {
        String[] errorArr = errorMsgParse("JWTVerificationFailed");
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResult runtimeExHandler(RuntimeException e) {
        String[] errorArr = errorMsgParse("Forbidden");
        return new ErrorResult(errorArr[0], errorArr[1], 403);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult notFoundExHandler(HttpClientErrorException.NotFound e) {
        String[] errorArr = errorMsgParse("NotFound");
        return new ErrorResult(errorArr[0], errorArr[1], 404);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult notFoundExHandler(NullPointerException e) {
        String[] errorArr = errorMsgParse(e.getMessage());
        return new ErrorResult(errorArr[0], errorArr[1], 404);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResult dataIntegrityViolationExHandler(DataIntegrityViolationException e) {
        String[] errorArr = errorMsgParse("DataIntegrityViolation");
        return new ErrorResult(errorArr[0], errorArr[1], 500);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        String[] errorArr = errorMsgParse("InternalServer");
        return new ErrorResult(errorArr[0], errorArr[1], 500);
    }
}
