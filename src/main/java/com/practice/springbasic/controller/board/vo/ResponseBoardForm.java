package com.practice.springbasic.controller.board.vo;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class ResponseBoardForm {
    Long boardId;
    BoardCategory boardCategory;
    String title;
    String content;
    String nickname;
    String email;
    Long memberId;
    LocalDateTime regDate;

    public ResponseBoardForm(Board board) {
        this.boardId = board.getId();
        this.boardCategory = board.getBoardCategory();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getMember().getNickname();
        this.email = board.getMember().getEmail();
        this.memberId = board.getMember().getId();
        this.regDate = board.getRegDate();
    }
}
