package com.practice.springbasic.domain.board;

import com.practice.springbasic.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

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
    }

    //해당 처리에 있어 빌더에도 FORM 만 받던지 해야하는 것인가
//    @Test
//    public void boardCreateFailed() throws Exception{
//        Board           board = Board.builder()
//                .member(this.member)
//                .boardCategory(boardCategory)
//                .content(contentFail)
//                .title(titleFail)
//                .build();
//        assertThat(board.getBoardCategory()).isEqualTo(boardCategory);
//        assertThat(board.getContent()).isEqualTo(content);
//        assertThat(board.getTitle()).isEqualTo(title);
//    }

    public Member memberSample() {
        return Member.builder()
                .nickname("middlefitting")
                .password("!middletigging123")
                .email("middlefitting@gmail.com")
                .build();
    }
}