package com.practice.springbasic.repository.comment;

import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.repository.comment.dto.QCommentPageDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.practice.springbasic.domain.comment.QComment.comment;


public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private OrderSpecifier<?> commentSort(Pageable pageable) {
//        if (!pageable.getSort().isEmpty()) {
//            for (Sort.Order order : pageable.getSort()) {
//                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
//                switch (order.getProperty()){
//                    default:
//                        return new OrderSpecifier<>(direction, comment.regDate);
//                }
//            }
//        }
        return new OrderSpecifier<>(Order.DESC, comment.regDate);
    }

    @Override
    public Page<CommentPageDto> findCommentPage(Pageable pageable, CommentPageSearchCondition condition, Long boardId) {
        List<CommentPageDto> content = queryFactory
                .select(new QCommentPageDto(
                                comment.id,
                                comment.content,
                                comment.board.id,
                                comment.member.id,
                                comment.member.nickname,
                                comment.member.email,
                                comment.regDate
                        )
                )
                .from(comment)
                .where(
                        ltCommentId(condition.getCommentId()),
                        comment.board.id.eq(boardId)
                )
                .orderBy(commentSort(pageable))
                .limit(10)
                .fetch();
        JPAQuery<Comment> countQuery = queryFactory
                .selectFrom(comment)
                .where(
                        ltCommentId(condition.getCommentId()),
                        comment.board.id.eq(boardId)
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery.fetch()::size);
    }

    private BooleanExpression ltCommentId(Long commentId) {
        return commentId == null ? null : comment.id.lt(commentId);
    }
}
