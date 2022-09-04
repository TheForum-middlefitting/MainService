package com.practice.springbasic.service.comment;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.comment.dto.CommentDto;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CommentService {
    Comment postComment(Member member, Board board, CommentDto postCommentDto);
    Optional<Comment> findComment(Long commentId);
    Comment updateComment(Comment comment, CommentUpdateDto commentUpdateDto);
    void deleteComment(Comment comment);
}
