package com.practice.springbasic.controller.utils.form;

import lombok.Getter;

@Getter
public class SuccessReturnForm {
    private final String message;
    private final int status;

    public SuccessReturnForm(int status) {
        this.message = "요청이 정상적으로 수행되었습니다";
        this.status = status;
    }
}
