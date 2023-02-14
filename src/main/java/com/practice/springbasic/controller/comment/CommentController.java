package com.practice.springbasic.controller.comment;

import com.practice.springbasic.controller.comment.dto.ReturnSingleCommentForm;
import com.practice.springbasic.controller.utils.form.SuccessReturnForm;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.service.comment.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface CommentController {
    ResponseEntity<ReturnSingleCommentForm> postComment(HttpServletRequest request, Long boardId, @Valid CommentDto commentDto, BindingResult bindingResult);
    ResponseEntity<ReturnSingleCommentForm> getComment(Long boardId, Long commentId);
    ResponseEntity<ReturnSingleCommentForm> updateComment(HttpServletRequest request, Long boardId, @Valid CommentUpdateDto commentUpdateDto, Long commentId, BindingResult bindingResult);
    ResponseEntity<SuccessReturnForm> deleteComment(HttpServletRequest request, Long boardId, Long commentId);
    ResponseEntity<Page<CommentPageDto>> searchCommentPage(Pageable pageable, Long boardId, Long commentId);
}
