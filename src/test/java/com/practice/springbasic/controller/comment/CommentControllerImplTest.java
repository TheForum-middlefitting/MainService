package com.practice.springbasic.controller.comment;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.comment.CommentService;
import com.practice.springbasic.service.comment.dto.CommentDto;
import com.practice.springbasic.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CommentControllerImpl.class)
class CommentControllerImplTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;
    @MockBean
    BoardService boardService;
    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper;

    private Member member;
    private Board board;
    private Comment comment;
    private CommentDto commentDto;

    private CommentUpdateDto commentUpdateDto;
    private Comment updatedComment;

    @BeforeEach
    public void createComment() {
        member = memberSample("middlefitting@google.com", "%middlefitting", "middlefitting");
        board = boardSample(BoardCategory.free, "hello world", "Hello my name is middlefitting", member);
        comment = commentSample("hello comment!", member, board);
        commentDto = new CommentDto(comment.getContent());
        commentUpdateDto = new CommentUpdateDto("hello my comment update");
        updatedComment = commentSample(comment.getContent(), member, board);
    }

    @Test
    @DisplayName("postCommentSuccess")
    void postCommentSuccess() throws Exception {
        when(memberService.findMemberById(1L)).thenReturn(Optional.ofNullable(member));
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        when(commentService.postComment(any(Member.class), any(Board.class), any(CommentDto.class))).thenReturn(comment);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));
        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePostResultActions("/boards/1/comments", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.commentId", equalTo(null)))
                .andExpect(jsonPath("$.data.content").value(comment.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.data.email").value(comment.getMember().getEmail()));
    }

    @Test
    @DisplayName("findSingleCommentSuccess")
    void findSingleCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));

        ResultActions resultActions = makeGetResultActions("/boards/1/comments/1");

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.commentId", equalTo(null)))
                .andExpect(jsonPath("$.data.content").value(comment.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.data.email").value(comment.getMember().getEmail()));
    }

    @Test
    @DisplayName("updateCommentSuccess")
    void updateCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        when(commentService.updateComment(any(Comment.class), any(CommentUpdateDto.class))).thenReturn(updatedComment);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePutResultActions("/boards/1/comments/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.boardId", equalTo(null)))
                .andExpect(jsonPath("$.data.content").value(updatedComment.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateCommentFailedByDifferentEmail")
    void updateCommentFailedByDifferentEmail() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        when(commentService.updateComment(any(Comment.class), any(CommentUpdateDto.class))).thenReturn(updatedComment);
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePutResultActions("/boards/1/comments/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("경고 정상적이지 않은 접근")))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo("FORBIDDEN")));
    }

    @Test
    @DisplayName("deleteCommentSuccess")
    void deleteCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makeDeleteResultActions("/boards/1/comments/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data", equalTo(null)));
    }

    @Test
    @DisplayName("deleteCommentFailedByDifferentEmail")
    void deleteCommentFailedByDifferentEmail() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makeDeleteResultActions("/boards/1/comments/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("경고 정상적이지 않은 접근")))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo("FORBIDDEN")));
    }
    @Test
    @DisplayName("findCommentPageSuccess")
    void findCommentPageSuccess() throws Exception{
        CommentPageSearchCondition condition = new CommentPageSearchCondition(1L);
        String content = objectMapper.writeValueAsString(condition);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makePostResultActions("/boards/1/comments/next/", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data", equalTo(null)));
    }
    private Comment commentSample(String content, Member member, Board board) {
        return Comment.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
    }
    private Board boardSample(BoardCategory boardCategory, String title, String content, Member member) {
        return Board.builder()
                .boardCategory(boardCategory)
                .content(content)
                .title(title)
                .member(member)
                .build();
    }

    private Member memberSample(String email, String password, String nickname) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
    ResultActions makePostResultActions(String url, String content, String jwtToken) throws Exception {
        return mockMvc.perform(post(url)
                        .header("Authorization", jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetResultActions(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makePutResultActions(String url, String content, String jwtToken) throws Exception {
        return mockMvc.perform(put(url)
                        .header("Authorization", jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeDeleteResultActions(String url, String jwtToken) throws Exception {
        return mockMvc.perform(delete(url)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}