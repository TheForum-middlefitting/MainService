package com.practice.springbasic.service.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardService {
    public Board postBoard(Member member, BoardDto postBoardDto);

    public Optional<Board> findBoard(Long boardId);

    public Board updateBoard(Board board, BoardDto boardUpdateDto);

    public void deleteBoard(Board board);

    Page<BoardPageDto> findBoardPage(Pageable pageable, BoardPageSearchCondition condition);
}
