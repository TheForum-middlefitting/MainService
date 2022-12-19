package com.practice.springbasic.controller.exhandler;

import com.auth0.jwt.exceptions.*;
import com.practice.springbasic.controller.utils.form.ErrorResult;
import com.practice.springbasic.utils.error.exception.AuthenticationFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import javax.validation.ConstraintViolationException;

import static com.practice.springbasic.config.error.ErrorMessage.*;

@RestControllerAdvice(basePackages = "com.practice.springbasic.controller")
public class ExControllerAdvice {
    public String[] errorMsgParse(String message) {
        String[] result = {Unknown.split("@")[0], Unknown.split("@")[1]};
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
        String message = errorArr.length == 2 ? errorArr[1] : "";
        String code = errorArr.length == 2 ? errorArr[0] : "";
        return new ErrorResult(code, message, 400);
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
        String[] errorArr = errorMsgParse(JWTDecodeFailed);
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResult jwtDecodeExHandler(TokenExpiredException e) {
        String[] errorArr = errorMsgParse(TokenExpired);
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClaimException.class)
    public ErrorResult jwtDecodeExHandler(InvalidClaimException e) {
        String[] errorArr = errorMsgParse(InvalidClaim);
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureVerificationException.class)
    public ErrorResult jwtDecodeExHandler(SignatureVerificationException e) {
        String[] errorArr = errorMsgParse(SignatureVerificationFailed);
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public ErrorResult jwtDecodeExHandler(JWTVerificationException e) {
        String[] errorArr = errorMsgParse(JWTVerificationFailed);
        return new ErrorResult(errorArr[0], errorArr[1], 401);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResult runtimeExHandler(RuntimeException e) {
        String[] errorArr = errorMsgParse(Forbidden);
        return new ErrorResult(errorArr[0], errorArr[1], 403);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResult notFoundExHandler(HttpClientErrorException.NotFound e) {
        String[] errorArr = errorMsgParse(NotFound);
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
        String[] errorArr = errorMsgParse(DataIntegrityViolation);
        return new ErrorResult(errorArr[0], errorArr[1], 500);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        String[] errorArr = errorMsgParse(InternalServer);
        return new ErrorResult(errorArr[0], errorArr[1], 500);
    }
}
