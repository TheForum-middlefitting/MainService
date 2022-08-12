package com.practice.springbasic.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResult {
    private ReturnMemberForm data;
    private String message;
    private int status;
}
