package com.practice.springbasic.controller.board.vo;

import com.practice.springbasic.domain.board.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestBoardForm {
    //이건 어떻게 에외처리할 것인가
    @Enumerated(EnumType.STRING)
//    @NotEmpty(message = BoardCategoryEmpty)
    private BoardCategory boardCategory;
    @NotEmpty(message = "BoardTitleEmpty")
    @Size( min = 5, max = 20, message = "BoardTitleLen")
    private String title;
    @NotEmpty(message = "BoardContentEmpty")
    @Size( min = 10, max = 1000, message = "BoardContentLen")
    private String content;
}
