package com.practice.springbasic.repository.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.repository.board.dto.QBoardPageDto;
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

import static com.practice.springbasic.domain.board.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private OrderSpecifier<?> boardSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    default:
                        return new OrderSpecifier<>(direction, board.regDate);
                }
            }
        }
        return new OrderSpecifier<>(Order.DESC, board.regDate);
    }
    @Override
    public Page<BoardPageDto> findBoardPage(Pageable pageable, BoardPageSearchCondition condition) {
        List<BoardPageDto> content = queryFactory
                .select(new QBoardPageDto(
                        board.id,
                        board.boardCategory,
                        board.title,
                        board.member.id,
                        board.member.nickname,
                        board.regDate
                        )
                )
                .from(board)
                .where(
                        board.id.goe(0L),
                        boardWriterNicknameContains(condition.getBoardWriterNickname()),
                        boardTitleContains(condition.getBoardTitle()),
                        boardContentContains(condition.getBoardContent()),
                        boardCategoryEq(condition.getBoardCategory())
                )
                .orderBy(boardSort(pageable))
                .offset(pageable.getPageNumber() * 10L)
                .limit(10)
                .fetch();
        JPAQuery<Board> countQuery = queryFactory
                .selectFrom(board)
                .where(
                        board.id.goe(0L),
                        boardWriterNicknameContains(condition.getBoardWriterNickname()),
                        boardTitleContains(condition.getBoardTitle()),
                        boardContentContains(condition.getBoardContent()),
                        boardCategoryEq(condition.getBoardCategory()));
        return PageableExecutionUtils.getPage(content, pageable, countQuery.fetch()::size);
    }


    private BooleanExpression boardWriterNicknameContains(String boardWriterNickname) {
        return hasText(boardWriterNickname) ?  board.member.nickname.contains(boardWriterNickname) : null;
    }
    private BooleanExpression boardTitleContains(String boardTitle) {
        return hasText(boardTitle) ?  board.title.contains(boardTitle) : null;
    }
    private BooleanExpression boardContentContains(String boardContent) {
        return hasText(boardContent) ?  board.content.contains(boardContent) : null;
    }
    private BooleanExpression boardCategoryEq(BoardCategory boardCategory) {
        if (boardCategory.equals(BoardCategory.free) || boardCategory.equals(BoardCategory.notice)) {
            return board.boardCategory.eq(BoardCategory.valueOf(String.valueOf(boardCategory)));
        }
        return null;
    }
}
