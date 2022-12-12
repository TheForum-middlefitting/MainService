package com.practice.springbasic.service.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.BoardRepository;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public BoardServiceImpl(BoardRepository boardRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Board postBoard(Member member, BoardDto postBoardDto) {
        Board board = buildBoard(member, postBoardDto);
//        Board board = modelMapper.map(postBoardDto, Board.class);
        return boardRepository.save(board);
    }
    @Override
    public Optional<Board> findBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public Board updateBoard(Board board, BoardDto boardUpdateDto) {
        board.boardUpdate(boardUpdateDto);
        return boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }

    @Override
    public Page<BoardPageDto> findBoardPage(Pageable pageable, BoardPageSearchCondition condition) {
        return boardRepository.findBoardPage(pageable, condition);
    }

    private static Board buildBoard(Member member, BoardDto postBoardDto) {
        return Board.builder()
                .boardCategory(postBoardDto.getBoardCategory())
                .title(postBoardDto.getTitle())
                .content(postBoardDto.getContent())
                .member(member)
                .build();
    }
}
