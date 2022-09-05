package com.practice.springbasic.service.comment;

import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.repository.comment.CommentRepository;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.comment.dto.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;


    private Comment comment;
    private Board board;
    private Member member;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void createBoard() {
        member = memberSample();
        board = boardSample();
        comment = commentSample();
    }

    @Test
    @DisplayName("postCommentSuccess")
    void postCommentSuccess() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentDto postCommentDto = new CommentDto("hello comment!");

        Comment result = commentService.postComment(member, board, postCommentDto);

        assertThat(result).isEqualTo(comment);
    }

    @Test
    @DisplayName("findSingleCommentSuccess")
    void findSingleCommentSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));

        Optional<Comment> result = commentService.findComment(1L);

        assertThat(result.orElse(null)).isEqualTo(comment);
    }

    @Test
    @DisplayName("findSingleCommentFailed")
    void findSingleCommentFailed() {
        when(commentRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

        Optional<Comment> result = commentService.findComment(2L);

        assertThat(result.orElse(null)).isEqualTo(null);
    }

    @Test
    @DisplayName("updateCommentSuccess")
    void updateCommentSuccess() {
        CommentUpdateDto updateCommentDto = new CommentUpdateDto("hello comment update!");
        Comment compareComment = commentSample();
        compareComment.commentUpdate(updateCommentDto);
        when(commentRepository.save(any(Comment.class))).thenReturn(compareComment);

        Comment result = commentService.updateComment(comment, updateCommentDto);

        assertThat(result.getContent()).isEqualTo(compareComment.getContent());
    }

    @Test
    @DisplayName("findCommentPageSuccess")
    void findCommentPageSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        CommentPageSearchCondition condition = new CommentPageSearchCondition(null);
        Page<CommentPageDto> mockPage = Mockito.mock(Page.class);
        when(commentRepository.findCommentPage(pageable, condition, 1L)).thenReturn(mockPage);

        Page<CommentPageDto> result = commentService.findCommentPage(pageable, condition, 1L);

        assertThat(result.getClass().getName()).isEqualTo(mockPage.getClass().getName());
    }

    @Test
    @DisplayName("deleteCommentSuccess")
    void deleteCommentSuccess() {
        commentService.deleteComment(comment);
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

    private Comment commentSample() {
        return Comment.builder()
                .content("hello comment!")
                .member(member)
                .board(board)
                .build();

    }
}