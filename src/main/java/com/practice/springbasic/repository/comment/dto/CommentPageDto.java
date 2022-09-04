package com.practice.springbasic.repository.comment.dto;

import com.practice.springbasic.domain.board.BoardCategory;

public class CommentPageDto {
    private final Long commentId;
    private final String content;
    private final Long boardId;
    private final Long memberId;
    private final String nickname;
    private final String email;
}
