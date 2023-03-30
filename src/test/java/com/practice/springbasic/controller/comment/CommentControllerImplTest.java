package com.practice.springbasic.controller.comment;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.comment.CommentService;
import com.practice.springbasic.service.comment.dto.CommentDto;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentControllerImpl.class)
@TestPropertySource(locations = "classpath:test.properties")
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

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    Environment env;

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
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));
        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePostResultActions("/comment-service/boards/1/comments", content, jwtToken);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId", equalTo(null)))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.email").value(comment.getMember().getEmail()));
    }

    @Test
    @DisplayName("postCommentFailedByContentLen")
    void postCommentFailedByContentLen() throws Exception {
        String content = objectMapper.writeValueAsString(new CommentDto("hello"));
        ResultActions resultActions = makePostResultActions("/comment-service/boards/1/comments", content, "");
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(env.getProperty("CommentContentLen.code")))
                .andExpect(jsonPath("$.message").value(env.getProperty("CommentContentLen.msg")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("postCommentFailedByContentEmpty")
    void postCommentFailedByContentEmpty() throws Exception {
        String content = objectMapper.writeValueAsString(new CommentDto(null));
        ResultActions resultActions = makePostResultActions("/comment-service/boards/1/comments", content, "");
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(env.getProperty("CommentContentEmpty.code")))
                .andExpect(jsonPath("$.message").value(env.getProperty("CommentContentEmpty.msg")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("findSingleCommentSuccess")
    void findSingleCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));

        ResultActions resultActions = makeGetResultActions("/comment-service/boards/1/comments/1");

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId", equalTo(null)))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.email").value(comment.getMember().getEmail()));
    }

    @Test
    @DisplayName("updateCommentSuccess")
    void updateCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        when(commentService.updateComment(any(Comment.class), any(CommentUpdateDto.class))).thenReturn(updatedComment);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePutResultActions("/comment-service/boards/1/comments/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.boardId", equalTo(null)))
                .andExpect(jsonPath("$.content").value(updatedComment.getContent()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateCommentFailedByDifferentEmail")
    void updateCommentFailedByDifferentEmail() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        when(commentService.updateComment(any(Comment.class), any(CommentUpdateDto.class))).thenReturn(updatedComment);
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        String content = objectMapper.writeValueAsString(commentDto);

        ResultActions resultActions = makePutResultActions("/comment-service/boards/1/comments/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("Forbidden.msg"))))))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("Forbidden.code"))))));
    }

    @Test
    @DisplayName("deleteCommentSuccess")
    void deleteCommentSuccess() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeDeleteResultActions("/comment-service/boards/1/comments/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("요청이 정상적으로 수행되었습니다")))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("deleteCommentFailedByDifferentEmail")
    void deleteCommentFailedByDifferentEmail() throws Exception{
        when(commentService.findComment(1L)).thenReturn(Optional.ofNullable(comment));
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeDeleteResultActions("/comment-service/boards/1/comments/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("Forbidden.msg"))))))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("Forbidden.code"))))));
    }
    @Test
    @DisplayName("findCommentPageSuccess")
    void findCommentPageSuccess() throws Exception{
        CommentPageSearchCondition condition = new CommentPageSearchCondition(1L);
        ResultActions resultActions = makeGetPageResultActions("/comment-service/boards/1/comments/", condition);
        when(commentService.findCommentPage(any(Pageable.class), any(CommentPageSearchCondition.class), any(Long.class))).thenReturn(null);
        resultActions
                .andExpect(status().isOk());
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
                        .accept(MediaType.APPLICATION_JSON));
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetResultActions(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetPageResultActions(String url, CommentPageSearchCondition condition) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("commentId", String.valueOf(condition.getCommentId()))
                        .accept(MediaType.APPLICATION_JSON));
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
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