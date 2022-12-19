package com.practice.springbasic.domain.board;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.validation.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    Member          member;
    BoardCategory   boardCategory;
    String          boardCategoryFail;
    String          content;
    String          title;
    String          contentFail;
    String          titleFail;

    @BeforeEach
    public void initializing() {
        member = memberSample();
        boardCategory = BoardCategory.free;
        content = "hello world";
        title = "hello";
        boardCategoryFail = "notype";
        contentFail = "hello";
        titleFail = "no";
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @Test
    public void boardCreateSuccess() throws Exception{
        Board           board = Board.builder()
                .member(this.member)
                .boardCategory(boardCategory)
                .content(content)
                .title(title)
                .build();
        assertThat(board.getBoardCategory()).isEqualTo(boardCategory);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getTitle()).isEqualTo(title);

        Set<ConstraintViolation<Board>> validate = validator.validate(board);

        assertThat(validate).isEmpty();

    }

    @Test
    public void boardCreateFailed() throws Exception{
        try {

            Board boardFail1 = Board.builder()
                    .member(this.member)
                    .boardCategory(null)
                    .content(content)
                    .title(title)
                    .build();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("잘못된 빌더");
        }
        Board           boardFail2 = Board.builder()
                .member(this.member)
                .boardCategory(boardCategory)
                .content(contentFail)
                .title(title)
                .build();
        Board           boardFail3 = Board.builder()
                .member(this.member)
                .boardCategory(boardCategory)
                .content(content)
                .title(titleFail)
                .build();

        assertThat(validator.validate(boardFail2)).isNotEmpty();
        assertThat(validator.validate(boardFail3)).isNotEmpty();

    }

    @Test
    public void boardUpdateSuccess() throws Exception{
        BoardDto updateDto = new BoardDto(BoardCategory.total, "middlefitting", "my name is middlefitting");
        Board           board = Board.builder()
                .member(this.member)
                .boardCategory(boardCategory)
                .content(content)
                .title(title)
                .build();

        board.boardUpdate(updateDto);

        assertThat(board.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(board.getContent()).isEqualTo(updateDto.getContent());
        assertThat(board.getBoardCategory()).isEqualTo(updateDto.getBoardCategory());
        assertThat(board.getMember()).isEqualTo(member);
    }

    public Member memberSample() {
        return Member.builder()
                .nickname("middlefitting")
                .password("!middletigging123")
                .email("middlefitting@gmail.com")
                .build();
    }
}