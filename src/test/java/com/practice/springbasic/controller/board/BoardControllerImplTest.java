package com.practice.springbasic.controller.board;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.board.dto.BoardDto;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.service.member.dto.MemberDto;
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

@WebMvcTest(BoardControllerImpl.class)
@TestPropertySource(locations = "classpath:test.properties")
class BoardControllerImplTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;
    @MockBean
    BoardService boardService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtUtils jwtUtils;

    private Member member;
    private Board board;
    private BoardDto boardDto;

    private BoardUpdateDto boardUpdateDto;
    private Board updatedBoard;

    @Autowired
    private Environment env;

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
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));
        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePostResultActions("/board-service/boards", content, jwtToken);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.boardId", equalTo(null)))
                .andExpect(jsonPath("$.boardCategory").value(boardDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.title").value(boardDto.getTitle()))
                .andExpect(jsonPath("$.content").value(board.getContent()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("findSingleBoardSuccess")
    void findSingleBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));

        ResultActions resultActions = makeGetResultActions("/board-service/boards/1");

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", equalTo(null)))
                .andExpect(jsonPath("$.boardCategory").value(boardDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.title").value(boardDto.getTitle()))
                .andExpect(jsonPath("$.content").value(board.getContent()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateBoardSuccess")
    void updateBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        when(boardService.updateBoard(any(Board.class), any(BoardDto.class))).thenReturn(updatedBoard);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePutResultActions("/board-service/boards/1", content, jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", equalTo(null)))
                .andExpect(jsonPath("$.boardCategory").value(boardUpdateDto.getBoardCategory().toString()))
                .andExpect(jsonPath("$.title").value(boardUpdateDto.getTitle()))
                .andExpect(jsonPath("$.content").value(updatedBoard.getContent()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    @DisplayName("updateBoardFailedByDifferentEmail")
    void updateBoardFailedByDifferentEmail() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        when(boardService.updateBoard(any(Board.class), any(BoardDto.class))).thenReturn(updatedBoard);
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        String content = objectMapper.writeValueAsString(boardDto);

        ResultActions resultActions = makePutResultActions("/board-service/boards/1", content, jwtToken);

        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.msg"))))))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.code"))))));
    }

    @Test
    @DisplayName("deleteBoardSuccess")
    void deleteBoardSuccess() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeDeleteResultActions("/board-service/boards/1", jwtToken);

        resultActions
                .andExpect(jsonPath("$.message", equalTo("요청이 정상적으로 수행되었습니다")))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("deleteBoardFailedByDifferentEmail")
    void deleteBoardFailedByDifferentEmail() throws Exception{
        when(boardService.findBoard(1L)).thenReturn(Optional.ofNullable(board));
        String jwtToken = JWT.create()
                .withSubject("Different" + member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeDeleteResultActions("/board-service/boards/1", jwtToken);

        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.msg"))))))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.code"))))));
    }
    @Test
    @DisplayName("findBoardPageSuccess")
    void findBoardPageSuccess() throws Exception{
        BoardPageSearchCondition condition = new BoardPageSearchCondition();
        when(boardService.findBoardPage(any(Pageable.class), any(BoardPageSearchCondition.class))).thenReturn(null);
        ResultActions resultActions = makeGetPageResultActions("/board-service/boards", condition);
        resultActions
                .andExpect(status().isOk());
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
                        .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetResultActions(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetPageResultActions(String url, BoardPageSearchCondition condition) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("boardTitle", condition.getBoardTitle())
                        .param("boardContent", condition.getBoardContent())
                        .param("boardWriterNickname", condition.getBoardWriterNickname())
                        .param("boardCategory", String.valueOf(condition.getBoardCategory()))
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