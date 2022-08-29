package com.practice.springbasic.service.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.BoardJpaRepository;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
    private final BoardJpaRepository boardRepository;

    public BoardServiceImpl(BoardJpaRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board postBoard(Member member, BoardDto postBoardDto) {
        Board board = Board.builder()
                .boardCategory(postBoardDto.getBoardCategory())
                .title(postBoardDto.getTitle())
                .content(postBoardDto.getContent())
                .member(member)
                .build();

        return boardRepository.save(board);
    }

    @Override
    public Optional<Board> findBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public Board updateBoard(Board board, BoardUpdateDto boardUpdateDto) {
        board.boardUpdate(boardUpdateDto);
        return boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }
}
