package com.practice.springbasic.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.controller.member.MemberControllerImpl;
import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberControllerImpl.class)
class MemberControllerImplTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    MemberService memberService;
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

//    @Test
//    public void joinMemberSuccess1() throws Exception{
//        when(memberService.join(ArgumentMatchers.any(Member.class))).thenReturn(member);
//        String content = objectMapper.writeValueAsString(member);
//        mockMvc.perform(post("/members") //mockmvcrequestbuilders.post
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) //mockmvcresult
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) //mockmvcresult
//                .andExpect(jsonPath("$.data.id").value(member.getId()))
//                .andDo(print()); //mockmvc print
//    }

    @Test
    public void joinMemberSuccess() throws Exception{
        when(memberService.join(ArgumentMatchers.any(Member.class))).thenReturn(member);
        String content = objectMapper.writeValueAsString(member);

//        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        ResultActions resultActions = makePostResultActions("/members", content);

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
    public void joinMemberFailedByEmailParsingError() throws Exception{
        String content = objectMapper.writeValueAsString(FailedEmailParsingMember);

        ResultActions resultActions = makePostResultActions("/members", content);

        resultActions
                .andExpect(status().isBadRequest())
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
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("중복된 닉네임입니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void findMemberSuccess() throws Exception{
        when(memberService.find(member.getNickname(), member.getPassword())).thenReturn(Optional.ofNullable(member));
        String content = objectMapper.writeValueAsString(member);
        ResultActions resultActions = makePostResultActions("/members/login", content);

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
    public void findMemberFailed() throws Exception{
        when(memberService.find(member.getNickname(), member.getPassword())).thenReturn(Optional.ofNullable(null));
        String content = objectMapper.writeValueAsString(member);
        ResultActions resultActions = makePostResultActions("/members/login", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("닉네임 혹은 패스워드가 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }
//
//    @Test
//    public void updateMemberSuccess() throws Exception{
//        MemberDto memberDto = memberDtoSample();
//        when(memberService.update(ArgumentMatchers.any(MemberDto.class))).thenReturn(1L);
//        System.out.println(memberService.update(memberDto));
//
//        Member result = memberController.updateMember(1L, member);
//
//        assertThat(result).isEqualTo(member);
//    }
//
//    @Test
//    public void updateMemberFailed() throws Exception{
//        MemberDto memberDto = memberDtoSample();
//        when(memberService.update(ArgumentMatchers.any(MemberDto.class))).thenReturn(0L);
//        System.out.println(memberService.update(memberDto));
//
//        Member result = memberController.updateMember(1L, member);
//
//        assertThat(result).isEqualTo(null);
//    }
//
//    @Test
//    public void deleteMemberSuccess() throws Exception{
//        when(memberService.withdrawal(member.getNickname(), member.getPassword())).thenReturn(true);
//
//        boolean result = memberController.deleteMember(1L, member);
//
//        assertThat(result).isEqualTo(true);
//    }
//
//    @Test
//    public void deleteMemberFailed() throws Exception{
//        when(memberService.withdrawal(member.getNickname(), member.getPassword())).thenReturn(false);
//
//        boolean result = memberController.deleteMember(1L, member);
//
//        assertThat(result).isEqualTo(false);
//    }
//
//    @Test
//    public void duplicateEmailFind() throws Exception{
//        when(memberService.duplicateEmail(member.getEmail())).thenReturn(true);
//
//        boolean result = memberController.duplicateEmail(member.getEmail());
//        assertThat(result).isEqualTo(true);
//    }
//
//    @Test
//    public void duplicateEmailNotFind() throws Exception{
//        when(memberService.duplicateEmail(member.getEmail())).thenReturn(false);
//
//        boolean result = memberController.duplicateEmail(member.getEmail());
//        assertThat(result).isEqualTo(false);
//    }
//
//    @Test
//    public void duplicateNicknameFind() throws Exception{
//        when(memberService.duplicateNickname(member.getNickname())).thenReturn(true);
//
//        boolean result = memberController.duplicateNickname(member.getNickname());
//
//        assertThat(result).isEqualTo(true);
//    }
//
//    @Test
//    public void duplicateNicknameNotFind() throws Exception{
//        when(memberService.duplicateNickname(member.getNickname())).thenReturn(false);
//
//        boolean result = memberController.duplicateNickname(member.getNickname());
//
//        assertThat(result).isEqualTo(false);
//    }
//
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