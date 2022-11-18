package com.practice.springbasic.controller.utils.form;

import lombok.Getter;

@Getter
public class SuccessCreatedResult {
    private final Object data;
    private final String message;
    private final int status;

    public SuccessCreatedResult(Object data) {
        this.data = data;
        this.message = "success";
        this.status = 201;
    }
}
