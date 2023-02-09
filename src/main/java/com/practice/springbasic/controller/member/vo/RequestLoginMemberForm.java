package com.practice.springbasic.controller.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@Getter
public class RequestLoginMemberForm {
    @NotEmpty(message = "EmailEmpty")
    @Email(message = "EmailForm")
    private String email;

    @NotEmpty(message = "PasswordEmpty")
    @Length(min=10, max=20, message = "PasswordLen")
    private String password;
}
