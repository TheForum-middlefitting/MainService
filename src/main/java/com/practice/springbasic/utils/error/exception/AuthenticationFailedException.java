package com.practice.springbasic.utils.error.exception;

public class AuthenticationFailedException extends RuntimeException{
    public  AuthenticationFailedException() {}
    public  AuthenticationFailedException(String message) {
        super(message);
    }
}
