package com.practice.springbasic.controller.utils.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SuccessResult {
    private Object data;
    private String message;
    private int status;

    public SuccessResult(Object data) {
        this.data = data;
        this.message = "success";
        this.status = 200;
    }
}
