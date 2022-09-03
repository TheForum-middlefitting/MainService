package com.practice.springbasic.repository.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Page<BoardPageDto> findBoardPage(Pageable pageable, BoardPageSearchCondition condition);

}
