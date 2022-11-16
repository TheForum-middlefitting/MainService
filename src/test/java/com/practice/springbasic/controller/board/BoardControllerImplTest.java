package com.practice.springbasic.controller.board;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.board.dto.BoardDto;
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

@WebMvcTest(BoardControllerImpl.class)
class BoardControllerImplTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;
    @MockBean
    BoardService boardService;

    @Autowired
    ObjectMapper objectMapper;

    private Member member;
    private Board board;
    private BoardDto boardDto;

    private BoardUpdateDto boardUpdateDto;
    private Board updatedBoard;

    @BeforeEach
    public void createBoard() {
        member = memberSample("middlefitting@google.com", "%middlefitting", "middlefitting");
        board = boardSample(BoardCategory.free, "hello world", "Hello my name is middlefitting", member);
        boardDto = new BoardDto(board.getBoardCategory(), board.getTitle(), board.getContent());
        boardUpdateDto = new BoardUpdateDto(BoardCategory.total, "world hello", "hello my board update");
        updatedBoard = boardSample(boardUpdateDto.getBoardCategory(), boardUpdateDto.getTitle(), boardUpdateDto.getContent(), member);
    }

    @Test
    @DisplayName("postBoardSuccess")
    void postBordSuccess() throws Exception {
        when(memberService.findMemberById(1L)).thenReturn(Optional.ofNullable(member));
        when(boardService.postBoard(any(Member.class), any(BoardDto.class))).thenReturn(board);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));
        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePostResultActions("/boards", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.boardId", equalTo(null)))
                .andExpect(jsonPath("$.data.boardCategory").value(boardDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.data.title").value(boardDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(board.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("findSingleBoardSuccess")
    void findSingleBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));

        ResultActions resultActions = makeGetResultActions("/boards/1");

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.boardId", equalTo(null)))
                .andExpect(jsonPath("$.data.boardCategory").value(boardDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.data.title").value(boardDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(board.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateBoardSuccess")
    void updateBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        when(boardService.updateBoard(any(Board.class), any(BoardUpdateDto.class))).thenReturn(updatedBoard);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePutResultActions("/boards/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.boardId", equalTo(null)))
                .andExpect(jsonPath("$.data.boardCategory").value(boardUpdateDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.data.title").value(boardUpdateDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(updatedBoard.getContent()))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateBoardFailedByDifferentEmail")
    void updateBoardFailedByDifferentEmail() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        when(boardService.updateBoard(any(Board.class), any(BoardUpdateDto.class))).thenReturn(updatedBoard);
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePutResultActions("/boards/1", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("경고 정상적이지 않은 접근")))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo("FORBIDDEN")));
    }

    @Test
    @DisplayName("deleteBoardSuccess")
    void deleteBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makeDeleteResultActions("/boards/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("deleteBoardFailedByDifferentEmail")
    void deleteBoardFailedByDifferentEmail() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makeDeleteResultActions("/boards/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("경고 정상적이지 않은 접근")))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code", equalTo("FORBIDDEN")));
    }
    @Test
    @DisplayName("findBoardPageSuccess")
    void findBoardPageSuccess() throws Exception{
        BoardPageSearchCondition condition = new BoardPageSearchCondition();
        String content = objectMapper.writeValueAsString(condition);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makePostResultActions("/boards/offset/", content, jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data", equalTo(null)));
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

    private MemberDto memberDtoSample() {
        return MemberDto.builder()
                .email("middlefitting@google.com")
                .password("%middlefitting")
                .nickname("middlefitting2")
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