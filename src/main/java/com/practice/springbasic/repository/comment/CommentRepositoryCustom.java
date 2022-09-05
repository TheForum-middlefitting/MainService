package com.practice.springbasic.repository.comment;

import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    public Page<CommentPageDto> findCommentPage(Pageable pageable, CommentPageSearchCondition condition, Long boardId);
}
