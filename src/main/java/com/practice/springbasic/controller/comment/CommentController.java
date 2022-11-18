package com.practice.springbasic.controller.comment;

import com.practice.springbasic.controller.utils.form.SuccessResult;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.comment.dto.CommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface CommentController {
    public SuccessResult postComment(HttpServletRequest request, Long boardId, CommentDto commentDto, BindingResult bindingResult);
    public SuccessResult getComment(Long boardId, Long commentId);
    public SuccessResult updateComment(HttpServletRequest request, Long boardId, CommentUpdateDto commentUpdateDto, Long commentId, BindingResult bindingResult);
    public SuccessResult deleteComment(HttpServletRequest request, Long boardId,  Long commentId);
    public SuccessResult searchCommentPage(Pageable pageable, Long boardId, CommentPageSearchCondition condition, BindingResult bindingResult);
}
