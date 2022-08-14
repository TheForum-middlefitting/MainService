package com.practice.springbasic.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResult {
    private ReturnMemberForm data;
    private String message;
    private int status;
}
