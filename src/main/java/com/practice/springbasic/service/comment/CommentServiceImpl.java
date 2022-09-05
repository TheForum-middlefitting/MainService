package com.practice.springbasic.service.comment;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.comment.CommentRepository;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.comment.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment postComment(Member member, Board board, CommentDto postCommentDto) {
        Comment comment = Comment.builder()
                .content(postCommentDto.getContent())
                .member(member)
                .board(board)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Comment updateComment(Comment comment, CommentUpdateDto commentUpdateDto) {
        comment.commentUpdate(commentUpdateDto);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Page<CommentPageDto> findCommentPage(Pageable pageable, CommentPageSearchCondition condition, long boardId) {
        return commentRepository.findCommentPage(pageable, condition, boardId);
    }

}
