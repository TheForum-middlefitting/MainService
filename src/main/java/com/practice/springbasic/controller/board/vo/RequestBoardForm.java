package com.practice.springbasic.controller.board.vo;

import com.practice.springbasic.domain.board.BoardCategory;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.practice.springbasic.config.error.ErrorMessage.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestBoardForm {
    @Enumerated(EnumType.STRING)
//    @NotEmpty(message = BoardCategoryEmpty)
    private BoardCategory boardCategory;
    @NotEmpty(message = BoardTitleEmpty)
    @Size( min = 5, max = 20, message = BoardTitleLen)
    private String title;
    @NotEmpty(message = BoardContentEmpty)
    @Size( min = 10, max = 1000, message = BoardContentLen)
    private String content;
}
