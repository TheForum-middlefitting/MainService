package com.practice.springbasic.controller.board.dto;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
public class ReturnSingleBoardForm {
    Long boardId;
    BoardCategory boardCategory;
    String title;
    String content;
    String nickname;
    String email;

    public ReturnSingleBoardForm(Board board) {
        this.boardId = board.getId();
        this.boardCategory = board.getBoardCategory();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getMember().getNickname();
        this.email = board.getMember().getEmail();
    }
}
