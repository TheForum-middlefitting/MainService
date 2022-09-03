package com.practice.springbasic.repository.board.dto;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BoardPageDto {
    private final Long boardId;
    private final BoardCategory boardCategory;
    private final String title;
    private final Long memberId;
    private final String nickname;

    @QueryProjection
    public BoardPageDto(Long boardId, BoardCategory boardCategory, String title, Long memberId, String nickname) {
        this.boardId = boardId;
        this.boardCategory = boardCategory;
        this.title = title;
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
