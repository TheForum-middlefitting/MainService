package com.practice.springbasic.controller.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.controller.member.dto.LoginMemberForm;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.domain.member.dto.MemberDto;
import com.practice.springbasic.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberControllerImpl.class)
class MemberControllerImplTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    MemberService memberService;
    
    //편한데 오래된 방식이라 나온다 
    //LinkedMultiValueMap<String, String> 를 통해 추후 변경
    @Autowired
    private ObjectMapper objectMapper;

    private Member member;
    private Member FailedEmailParsingMember;
    private Member FailedPasswordParsingMember;
    private Member FailedNicknameParsingMember;

    @BeforeEach
    public void createMember() {
        member = memberSample("middlefitting@google.com", "%middlefitting", "middlefitting");
        FailedEmailParsingMember = memberSample("middle", "%middlefitting", "middlefitting");
        FailedPasswordParsingMember = memberSample("middlefitting@google.com", "%mid", "middlefitting");
        FailedNicknameParsingMember = memberSample("middlefitting@google.com", "%middlefitting", "");
    }

    @Test
    public void joinMemberSuccess() throws Exception{
        when(memberService.join(ArgumentMatchers.any(Member.class))).thenReturn(member);
        String content = objectMapper.writeValueAsString(member);

//        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(member.getId()))
                .andExpect(jsonPath("$.data.id", equalTo(null)))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
//                .andDo(print());
    }

    @Test
    public void joinMemberFailedByEmailParsingError() throws Exception{
        String content = objectMapper.writeValueAsString(FailedEmailParsingMember);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("입력 값이 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinMemberFailedByNicknameParsingError() throws Exception{
        String content = objectMapper.writeValueAsString(FailedNicknameParsingMember);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("입력 값이 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinMemberFailedByPasswordParsingError() throws Exception{
        String content = objectMapper.writeValueAsString(FailedPasswordParsingMember);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("입력 값이 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinMemberFailedByDuplicateEmail() throws Exception{
        when(memberService.duplicateEmail(ArgumentMatchers.anyString())).thenReturn(true);
        String content = objectMapper.writeValueAsString(member);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("중복된 이메일입니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void joinMemberFailedByDuplicateNickname() throws Exception{
        when(memberService.duplicateNickname(member.getNickname())).thenReturn(true);
        String content = objectMapper.writeValueAsString(member);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("중복된 닉네임입니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void loginMemberSuccess() throws Exception{
        when(memberService.find(member.getEmail(), member.getPassword())).thenReturn(Optional.ofNullable(member));
        LoginMemberForm loginMemberForm = new LoginMemberForm(member.getEmail(), member.getPassword());
        String content = objectMapper.writeValueAsString(loginMemberForm);
        ResultActions resultActions = makePostResultActions("/members/login", content);
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(member.getId()))
                .andExpect(jsonPath("$.data.id", equalTo(null)))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }

    @Test
    public void loginMemberFailed() throws Exception{
        when(memberService.find(member.getEmail(), member.getPassword())).thenReturn(Optional.ofNullable(null));
        LoginMemberForm loginMemberForm = new LoginMemberForm(member.getEmail(), member.getPassword());
        String content = objectMapper.writeValueAsString(loginMemberForm);
        ResultActions resultActions = makePostResultActions("/members/login", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("이메일 혹은 패스워드가 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void updateMemberSuccess() throws Exception{
        when(memberService.find(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(member));
        String content = objectMapper.writeValueAsString(member);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));

        ResultActions resultActions = makePutResultActions("/members/1", content, jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(member.getId()))
                .andExpect(jsonPath("$.data.id", equalTo(null)))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.data.email").value(member.getEmail()));
    }



    @Test
    public void deleteMemberSuccess() throws Exception{
        when(memberService.findMemberByIdAndPassword(1L, member.getPassword())).thenReturn(Optional.ofNullable(member));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(JwtProperties.Access_SECRET));
        ResultActions resultActions = makeDeleteResultActions("/members/1/%middlefitting", jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data", equalTo(null)));
    }

    ResultActions makeDeleteResultActions(String url, String jwtToken) throws Exception {
        return mockMvc.perform(delete(url)
                        .header("Authorization", jwtToken)
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

    ResultActions makePostResultActions(String url, String content) throws Exception {
        return mockMvc.perform(post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetResultActions(String url, String content, LinkedMultiValueMap queryParams) throws Exception {
        return mockMvc.perform(get(url)
                        .queryParams(queryParams)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
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
}