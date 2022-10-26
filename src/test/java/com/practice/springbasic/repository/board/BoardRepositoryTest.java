package com.practice.springbasic.repository.board;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@Transactional
class BoardRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardRepository boardRepository;
    Member member1;
    Member member2;
    Member member3;
    Member member4;

    Board board1;
    Board board2;
    Board board3;
    Board board4;
    Board board5;
    Board board6;
    Board board7;
    Board board8;
    Board board9;
    Board board10;
    Board board11;

    @BeforeEach
    public void createMember() throws Exception{

        member1 = new Member("member1", "member1@gmail.com", "1!memberPassword123");
        member2 = new Member("member2", "member2@gmail.com", "2!memberPassword123");
        member3 = new Member("member3", "member3@gmail.com", "3!memberPassword123");
        member4 = new Member("member4", "member4@gmail.com", "4!memberPassword123");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        board1 = new Board(BoardCategory.free, "board1", "hello this is board1", member1);
        board2 = new Board(BoardCategory.free, "board2", "hello this is board2", member1);
        board3 = new Board(BoardCategory.free, "board3", "hello this is board3", member1);
        board4 = new Board(BoardCategory.free, "board4", "hello this is board4", member1);
        board5 = new Board(BoardCategory.free, "board5", "hello this is board5", member1);
        board6 = new Board(BoardCategory.notice, "board6", "hello this is board6", member2);
        board7 = new Board(BoardCategory.notice, "board7", "hello this is board7", member2);
        board8 = new Board(BoardCategory.notice, "board8", "hello this is board8", member2);
        board9 = new Board(BoardCategory.notice, "board9", "hello this is board9", member2);
        board10 = new Board(BoardCategory.notice, "board10", "hello this is board10", member2);
        board11 = new Board(BoardCategory.total, "board11", "hello this is board11", member3);

        Board[] arr = new Board[] {board1, board2, board3, board4, board5, board6, board7, board8, board9, board10, board11};
        for(Board board : arr) {
            em.persist(board);
            em.flush();
        }
    }

    @Test
    @DisplayName("searchBoardPageSuccessByBlankOrder")
    void searchBoardPageSuccessByBlankOrder() {
        Pageable pageable = PageRequest.of(0, 10);
        BoardPageSearchCondition condition = new BoardPageSearchCondition();

        Page<BoardPageDto> result = boardRepository.findBoardPage(pageable, condition);

        assertThat(result).extracting("title")
                .containsExactly(board11.getTitle(), board10.getTitle(), board9.getTitle(), board8.getTitle(), board7.getTitle(), board6.getTitle(), board5.getTitle(),
                        board4.getTitle(), board3.getTitle(), board2.getTitle());
        assertThat(result).extracting("memberId")
                .contains(member1.getId(), member2.getId());
        assertThat(result.getTotalPages()).isEqualTo(2L);
        assertThat(result.getTotalElements()).isEqualTo(11L);
    }

    @Test
    @DisplayName("searchBoardPageSuccessBySwitchDefaultASC")
    void searchBoardPageSuccessBySwitchDefaultASC() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC,  "regDate");
        BoardPageSearchCondition condition = new BoardPageSearchCondition();

        Page<BoardPageDto> result = boardRepository.findBoardPage(pageable, condition);

        assertThat(result).extracting("title")
                .containsExactly(board1.getTitle(), board2.getTitle(), board3.getTitle(), board4.getTitle(), board5.getTitle(), board6.getTitle(),
                        board7.getTitle(), board8.getTitle(), board9.getTitle(), board10.getTitle());
        assertThat(result).extracting("memberId")
                .contains(member1.getId(), member2.getId());
        assertThat(result.getTotalPages()).isEqualTo(2L);
        assertThat(result.getTotalElements()).isEqualTo(11L);
    }

    @Test
    @DisplayName("searchBoardPageSuccessBySwitchDefaultDESC")
    void searchBoardPageSuccessBySwitchDefaultDESC() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,  "regDate");
        BoardPageSearchCondition condition = new BoardPageSearchCondition();

        Page<BoardPageDto> result = boardRepository.findBoardPage(pageable, condition);

        assertThat(result).extracting("title")
                .containsExactly(board11.getTitle(), board10.getTitle(), board9.getTitle(), board8.getTitle(), board7.getTitle(), board6.getTitle(), board5.getTitle(),
                        board4.getTitle(), board3.getTitle(), board2.getTitle());
        assertThat(result).extracting("memberId")
                .contains(member1.getId(), member2.getId(), member3.getId());
        assertThat(result.getTotalPages()).isEqualTo(2L);
        assertThat(result.getTotalElements()).isEqualTo(11L);
    }

    @Test
    @DisplayName("searchBoardPageSuccessBySwitchDefaultDESCAndCategoryBoolean")
    void searchBoardPageSuccessBySwitchDefaultDESCAndCategoryBoolean() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,  "regDate");
        BoardPageSearchCondition condition = BoardPageSearchCondition.builder()
                .boardCategory(BoardCategory.free)
                .build();

        Page<BoardPageDto> result = boardRepository.findBoardPage(pageable, condition);

        assertThat(result).extracting("title")
                .containsExactly(board5.getTitle(), board4.getTitle(), board3.getTitle(), board2.getTitle(), board1.getTitle());
        assertThat(result).extracting("memberId")
                .contains(member1.getId());
        assertThat(result.getTotalPages()).isEqualTo(1L);
        assertThat(result.getTotalElements()).isEqualTo(5L);
    }

    @Test
    @DisplayName("searchBoardPageSuccessBySwitchDefaultDESCAndCNicknameBoolean")
    void searchBoardPageSuccessBySwitchDefaultDESCAndCNicknameBoolean() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,  "regDate");
        BoardPageSearchCondition condition = BoardPageSearchCondition.builder()
                .boardCategory(BoardCategory.total)
                .boardWriterNickname("member1")
                .build();

        Page<BoardPageDto> result = boardRepository.findBoardPage(pageable, condition);

        assertThat(result).extracting("boardId")
                .containsExactly(board5.getId(), board4.getId(), board3.getId(), board2.getId(), board1.getId());
        assertThat(result).extracting("boardCategory")
                .containsExactly(board5.getBoardCategory(), board4.getBoardCategory(), board3.getBoardCategory(), board2.getBoardCategory(), board1.getBoardCategory());
        assertThat(result).extracting("title")
                .containsExactly(board5.getTitle(), board4.getTitle(), board3.getTitle(), board2.getTitle(), board1.getTitle());
        assertThat(result).extracting("nickname")
                .contains(member1.getNickname());
        assertThat(result).extracting("memberId")
                        .contains(member1.getId());
        assertThat(result.getTotalPages()).isEqualTo(1L);
        assertThat(result.getTotalElements()).isEqualTo(5L);
    }
}

