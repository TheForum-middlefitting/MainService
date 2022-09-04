package com.practice.springbasic.repository.board;

import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardPageDto> findBoardPage(Pageable pageable, BoardPageSearchCondition condition);

}
