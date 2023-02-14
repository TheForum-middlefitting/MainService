package com.practice.springbasic.controller.comment.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCommentsForm {
    @NotEmpty(message = "CommentContentEmpty")
    @Length( min = 10, max = 1000, message = "CommentContentLen")
    private String content;
}
