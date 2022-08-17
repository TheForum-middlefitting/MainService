package com.practice.springbasic.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class LoginMemberForm {
    private String email;
    private String password;
}
