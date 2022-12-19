package com.practice.springbasic.controller.comment.vo;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCommentsForm {
    @NotNull
    @NotEmpty
    @Size( min = 10, max = 1000)
    private String content;

}
