package com.practice.springbasic.controller.member.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class RequestMemberForm {
    @NotEmpty(message = "EmailEmpty")
    @Email(message = "EmailForm")
    String email;
    @NotEmpty(message = "NicknameEmpty")
    @Length(min=4, max=20, message = "NicknameLen")
    String nickname;

    @NotEmpty(message = "PasswordEmpty")
    @Length(min=10, max=20, message = "PasswordLen")
    String password;

    @Builder
    public RequestMemberForm(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
