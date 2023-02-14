package com.practice.springbasic.controller.comment.vo;

import com.practice.springbasic.domain.comment.Comment;
import lombok.Getter;

@Getter
public class ResponseCommentsForm {
    Long commentId;
    Long memberId;
    Long boardId;
    String content;
    String nickname;
    String email;

    public ResponseCommentsForm(Comment comment) {
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.memberId = comment.getMember().getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.email = comment.getMember().getEmail();
    }

}
