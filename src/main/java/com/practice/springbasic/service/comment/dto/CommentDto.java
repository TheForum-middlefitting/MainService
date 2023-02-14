package com.practice.springbasic.service.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {
    @NotEmpty(message = "CommentContentEmpty")
    @Length(min=10, max=1000, message = "CommentContentLen")
    private String content;
}
