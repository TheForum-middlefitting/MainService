package com.practice.springbasic.repository.comment;

import com.practice.springbasic.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom, QuerydslPredicateExecutor<Comment> {

}
