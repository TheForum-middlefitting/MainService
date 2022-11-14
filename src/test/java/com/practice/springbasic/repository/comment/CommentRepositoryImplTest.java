package com.practice.springbasic.repository.comment;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@EnableJpaAuditing
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentRepositoryImplTest {
//    @PersistenceContext
//    EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Autowired
    CommentRepository commentRepository;

    @BeforeAll
    public void createComment() throws Exception{
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        for(int i=0; i< 5; i++) {
            Member member = new Member("member" + Integer.toString(i), "member" + Integer.toString(i) + "@gmail.com", "@memberPassword" + Integer.toString(i));
            em.persist(member);
            em.flush();
            Board board = new Board(BoardCategory.free, "board" + Integer.toString(i) + "@gmail.com", "board content" + Integer.toString(i), member);
            em.persist(board);
            em.flush();
            for (int j=0; j<15; j++) {
                Comment comment = new Comment("comment is this" + Integer.toString(i * 15 + j), member, board);
                em.persist(comment);
            }
            System.out.println("here!!!");
            em.flush();
        }
        em.getTransaction().commit();
        em.close();
    }

    @Test
//    @Rollback(false)
    @DisplayName("searchCommentPageSuccess")
    void searchCommentPageSuccess() throws Exception{
        Pageable pageable = PageRequest.of(0, 10);
        CommentPageSearchCondition condition = new CommentPageSearchCondition(null);

        Page<CommentPageDto> result = commentRepository.findCommentPage(pageable, condition, 1L);

        assertThat(result).extracting("content")
                .containsExactly("comment is this14",
                        "comment is this13",
                        "comment is this12",
                        "comment is this11",
                        "comment is this10",
                        "comment is this9",
                        "comment is this8",
                        "comment is this7",
                        "comment is this6",
                        "comment is this5");
        assertThat(result.getTotalPages()).isEqualTo(2L);
        assertThat(result.getTotalElements()).isEqualTo(15L);

        Pageable pageable2 = PageRequest.of(0, 10);
        CommentPageSearchCondition condition2 = new CommentPageSearchCondition(6L);

        Page<CommentPageDto> result2 = commentRepository.findCommentPage(pageable2, condition2, 1L);

        assertThat(result2).extracting("content")
                .containsExactly("comment is this4",
                        "comment is this3",
                        "comment is this2",
                        "comment is this1",
                        "comment is this0");
        assertThat(result2.getTotalPages()).isEqualTo(1L);
        assertThat(result2.getTotalElements()).isEqualTo(5L);
    }

    @Test
//    @Rollback(false)
    @DisplayName("searchCommentPageSuccessByBeforeCommentId")
    void searchCommentPageSuccessByBeforeCommentId() throws Exception{
        Pageable pageable = PageRequest.of(0, 10);
        CommentPageSearchCondition condition = new CommentPageSearchCondition(6L);

        Page<CommentPageDto> result = commentRepository.findCommentPage(pageable, condition, 1L);

        assertThat(result).extracting("content")
                .containsExactly("comment is this4",
                        "comment is this3",
                        "comment is this2",
                        "comment is this1",
                        "comment is this0");
        assertThat(result.getTotalPages()).isEqualTo(1L);
        assertThat(result.getTotalElements()).isEqualTo(5L);
    }

}