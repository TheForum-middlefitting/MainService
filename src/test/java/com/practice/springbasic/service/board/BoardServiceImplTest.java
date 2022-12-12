package com.practice.springbasic.service.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.BoardRepository;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class BoardServiceImplTest {

    @Mock
    BoardRepository boardRepository;

    @Autowired
    ModelMapper modelMapper;
//    @InjectMocks
    BoardServiceImpl boardService;
    private Board board;
    private Member member;

    @BeforeEach
    public void initialize() {
//        MockitoAnnotations.openMocks(this);
        boardService  = new BoardServiceImpl(boardRepository, modelMapper);
    }

    @BeforeEach
    public void createBoard() {
        member = memberSample();
        board = boardSample();
    }

    @Test
    @DisplayName("postBoardSuccess")
    void postBoardSuccess() {
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        BoardDto postBoardDto = new BoardDto(BoardCategory.free, "hello world!", "hello world! My name is middlefitting");

        Board result = boardService.postBoard(member, postBoardDto);

        assertThat(result).isEqualTo(board);
    }

    @Test
    @DisplayName("findSingleBoardSuccess")
    void findSingleBoardSuccess() {
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(board));

        Optional<Board> result = boardService.findBoard(1L);

        assertThat(result.orElse(null)).isEqualTo(board);
    }

    @Test
    @DisplayName("findSingleBoardFailed")
    void findSingleBoardFailed() {
        when(boardRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

        Optional<Board> result = boardService.findBoard(2L);

        assertThat(result.orElse(null)).isEqualTo(null);
    }

    @Test
    @DisplayName("updateBoardSuccess")
    void updateBoardSuccess() {
        BoardDto updateBoardDto = new BoardDto(BoardCategory.free, "hello world!", "hello world! My name is middlefitting");
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        Board result = boardService.updateBoard(board, updateBoardDto);

        assertThat(result).isEqualTo(board);
    }

    @Test
    @DisplayName("findBoardPageSuccess")
    void findBoardPageSuccess() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,  "regDate");
        BoardPageSearchCondition condition = new BoardPageSearchCondition();
        Page<BoardPageDto> mockPage = Mockito.mock(Page.class);
        when(boardRepository.findBoardPage(pageable, condition)).thenReturn(mockPage);

        Page<BoardPageDto> result = boardService.findBoardPage(pageable, condition);

        assertThat(result.getClass().getName()).isEqualTo(mockPage.getClass().getName());
    }

    @Test
    @DisplayName("deleteBoardSuccess")
    void deleteBoardSuccess() {
        boardService.deleteBoard(board);
    }

    private Member memberSample() {
        return Member.builder()
                .email("middlefitting@google.com")
                .password("%middlefitting")
                .nickname("middlefitting")
                .build();
    }

    private Board boardSample() {
        return Board.builder()
                .boardCategory(BoardCategory.free)
                .title("hello world!")
                .content("hello world! My name is middlefitting")
                .member(member)
                .build();
    }
}