package com.practice.springbasic.domain.comment;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    Member          member;
    Board           board;
    String          content;
    String          contentFail;

    @BeforeEach
    public void initializing() {
        member = memberSample();
        board = boardSample(member);
        content = "comment hello";
        contentFail = "hello";
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @Test
    public void commentCreateSuccess() throws Exception{
        Comment comment = Comment.builder()
                .member(this.member)
                .board(this.board)
                .content(content)
                .build();
        assertThat(comment.getContent()).isEqualTo(content);

        Set<ConstraintViolation<Board>> validate = validator.validate(board);

        assertThat(validate).isEmpty();
    }

    @Test
    public void CommentCreateFailed() throws Exception{
        Comment commentFail = Comment.builder()
                .member(this.member)
                .board(this.board)
                .content(contentFail)
                .build();

        assertThat(validator.validate(commentFail)).isNotEmpty();

    }

    @Test
    public void commentUpdateSuccess() throws Exception{
        CommentUpdateDto updateDto = new CommentUpdateDto("my name is middlefitting");
        Comment comment = Comment.builder()
                .member(this.member)
                .board(this.board)
                .content(content)
                .build();

        comment.commentUpdate(updateDto);

        assertThat(comment.getContent()).isEqualTo(updateDto.getContent());
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getBoard()).isEqualTo(board);

    }

    public Member memberSample() {
        return Member.builder()
                .nickname("middlefitting")
                .password("!middletigging123")
                .email("middlefitting@gmail.com")
                .build();
    }

    public Board boardSample(Member member) {
        return Board.builder()
                .member(member)
                .boardCategory(BoardCategory.total)
                .title("hello world")
                .content("hello this is content")
                .build();
    }
}