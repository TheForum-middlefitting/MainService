package com.practice.springbasic.repository.comment.dto;

import com.practice.springbasic.domain.board.BoardCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CommentPageDto {
    private final Long commentId;
    private final String content;
    private final Long boardId;
    private final Long memberId;
    private final String nickname;
    private final String email;

    @QueryProjection
    public CommentPageDto(Long commentId, String content, Long boardId, Long memberId, String nickname, String email) {
        this.commentId = commentId;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
    }
}
