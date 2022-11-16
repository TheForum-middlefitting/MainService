package com.practice.springbasic.controller.member.vo;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class RequestMemberForm {
    @NotNull @NotEmpty @Email
    String email;
    @NotNull
    @NotEmpty @Length(min=4, max=20)
    String nickname;

    @NotNull @NotEmpty @Length(min=10, max=20)
    String password;

    @Builder
    public RequestMemberForm(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
