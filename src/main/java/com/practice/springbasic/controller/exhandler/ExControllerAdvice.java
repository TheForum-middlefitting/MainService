package com.practice.springbasic.controller.exhandler;

import com.practice.springbasic.controller.utils.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.practice.springbasic.controller")
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalArgumentExhandler(IllegalArgumentException e) {
        return new ErrorResult("BAD_REQUEST", e.getMessage(), 400);
    }
}
