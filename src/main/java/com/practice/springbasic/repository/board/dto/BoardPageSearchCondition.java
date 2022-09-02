package com.practice.springbasic.repository.board.dto;

import com.practice.springbasic.domain.board.BoardCategory;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardPageSearchCondition {
    private String boardWriterNickname;
    private String boardTitle;
    private String boardContent;
    @NotNull
    private BoardCategory boardCategory;

    public BoardPageSearchCondition() {
        this.boardCategory = BoardCategory.total;
    }
}
