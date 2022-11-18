package com.practice.springbasic.controller.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static com.practice.springbasic.config.error.ErrorMessage.*;
import static com.practice.springbasic.config.error.ErrorMessage.PasswordLen;

@AllArgsConstructor
@Getter
public class LoginMemberForm {
    @NotEmpty(message = EmailEmpty)
    @Email(message = EmailForm)
    private String email;

    @NotEmpty(message = PasswordEmpty)
    @Length(min=10, max=20, message = PasswordLen)
    private String password;
}
