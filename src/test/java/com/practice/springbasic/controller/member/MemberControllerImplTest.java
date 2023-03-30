package com.practice.springbasic.controller.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbasic.controller.member.vo.RequestLoginMemberForm;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberControllerImpl.class)
@TestPropertySource(locations = "classpath:test.properties")
class MemberControllerImplTest {
//    @Autowired
//    ModelMapper modelMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    MemberService memberService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtils;

    MemberControllerImpl memberController;
    //편한데 오래된 방식이라 나온다 
    //LinkedMultiValueMap<String, String> 를 통해 추후 변경
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    Environment env;

    private Member member;
    private Member FailedEmailParsingMember;
    private Member FailedPasswordParsingMember;
    private Member FailedNicknameParsingMember;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        memberController = new MemberControllerImpl(memberService, modelMapper, jwtUtils, env);
    }
    @BeforeEach
    public void createMember() {
        member = memberSample("middlefitting@google.com", "%middlefitting", "middlefitting");
        FailedEmailParsingMember = memberSample("middle", "%middlefitting", "middlefitting");
        FailedPasswordParsingMember = memberSample("middlefitting@google.com", "%mid", "middlefitting");
        FailedNicknameParsingMember = memberSample("middlefitting@google.com", "%middlefitting", "m");
    }

    @Test
    public void joinMemberSuccess() throws Exception {
        when(memberService.join(ArgumentMatchers.any(MemberDto.class))).thenReturn(member);
        String content = objectMapper.writeValueAsString(member);

//        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        ResultActions resultActions = makePostResultActions("/member-service/members", content);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberId", equalTo(null)))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    public void joinMemberFailedByEmailFormError() throws Exception {
        String content = objectMapper.writeValueAsString(FailedEmailParsingMember);

        ResultActions resultActions = makePostResultActions("/member-service/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("EmailForm.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("EmailForm.msg"))))))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinMemberFailedByNicknameLenError() throws Exception {
        String content = objectMapper.writeValueAsString(FailedNicknameParsingMember);

        ResultActions resultActions = makePostResultActions("/member-service/members", content);
//
//        resultActions
//                .andExpect(status().isBadRequest())
//                .andExpect(header().doesNotExist("Authorization"))
//                .andExpect(header().doesNotExist("Refresh"))
//                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("NicknameLen.code"))))))
//                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("NicknameLen.msg"))))))
//                .andExpect(jsonPath("$.status").value(400))
//                .andDo(print());
    }

    @Test
    public void joinMemberFailedByPasswordLenError() throws Exception {
        String content = objectMapper.writeValueAsString(FailedPasswordParsingMember);

        ResultActions resultActions = makePostResultActions("/member-service/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("PasswordLen.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("PasswordLen.msg"))))))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinMemberFailedByDuplicateEmail() throws Exception {
        when(memberService.duplicateEmail(ArgumentMatchers.anyString())).thenReturn(true);
        String content = objectMapper.writeValueAsString(member);

        ResultActions resultActions = makePostResultActions("/member-service/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateEmail.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateEmail.msg"))))))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void joinMemberFailedByDuplicateNickname() throws Exception {
        when(memberService.duplicateNickname(member.getNickname())).thenReturn(true);
        String content = objectMapper.writeValueAsString(member);

        ResultActions resultActions = makePostResultActions("/member-service/members", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateNickname.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateNickname.msg"))))))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void loginMemberSuccess() throws Exception {
        when(memberService.findMemberByEmailAndPassword(member.getEmail(), member.getPassword())).thenReturn(Optional.ofNullable(member));
        RequestLoginMemberForm loginMemberForm = new RequestLoginMemberForm(member.getEmail(), member.getPassword());
        String content = objectMapper.writeValueAsString(loginMemberForm);
        ResultActions resultActions = makePostResultActions("/member-service/members/login", content);
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberId", equalTo(null)))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    public void loginMemberFailed() throws Exception {
        when(memberService.findMemberByEmailAndPassword(member.getEmail(), member.getPassword())).thenReturn(Optional.empty());
        RequestLoginMemberForm loginMemberForm = new RequestLoginMemberForm(member.getEmail(), member.getPassword());
        String content = objectMapper.writeValueAsString(loginMemberForm);
        ResultActions resultActions = makePostResultActions("/member-service/members/login", content);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("Refresh"))
                .andExpect(jsonPath("$.code", equalTo("1000")))
                .andExpect(jsonPath("$.message", equalTo("이메일 혹은 패스워드가 잘못되었습니다!")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void getMemberSuccess() throws Exception {
        when(memberService.findMemberById(1L)).thenReturn(Optional.ofNullable(member));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeGetResultActions("/member-service/members/1", jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberId", equalTo(null)))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    public void getMemberFailedByTokenAuthFailed() throws Exception {
        when(memberService.findMemberById(1L)).thenReturn(Optional.ofNullable(member));
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 2)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeGetResultActions("/member-service/members/1", jwtToken);

        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.code"))))))
                .andExpect(jsonPath("$.message").value(String.format(Objects.requireNonNull(env.getProperty("AuthFailed.msg")))))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    public void getMemberFailedBy404Member() throws Exception {
        when(memberService.findMemberById(1L)).thenReturn(Optional.ofNullable(member));
        when(memberService.findMemberById(2L)).thenReturn(Optional.empty());
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 2)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makeGetResultActions("/member-service/members/2", jwtToken);

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("MemberNotFound.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("MemberNotFound.msg"))))))
                .andExpect(jsonPath("$.status").value(404));
    }


    @Test
    public void updateMemberSuccess() throws Exception {
        when(memberService.findMemberByEmailAndPassword(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(member));
        String content = objectMapper.writeValueAsString(member);
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));

        ResultActions resultActions = makePutResultActions("/member-service/members/1", content, jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberId", equalTo(null)))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    public void nicknameDuplicateFalse() throws Exception {
        when(memberService.duplicateNickname(ArgumentMatchers.any())).thenReturn(true);

        ResultActions resultActions = makeGetResultActions("/member-service/nickname-check?nickname=middle");

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateNickname.code"))))))
                .andExpect(jsonPath("$.message", equalTo(String.format(Objects.requireNonNull(env.getProperty("DuplicateNickname.msg"))))))
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    public void deleteMemberSuccess() throws Exception {
        String jwtToken = JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(env.getProperty("token.ACCESS_EXPIRATION_TIME"))))
                .withClaim("id", 1)
                .sign(Algorithm.HMAC512(env.getProperty("token.ACCESS_SECRET")));
        ResultActions resultActions = makeDeleteResultActions("/member-service/members/1?password=%middlefitting", jwtToken);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("요청이 정상적으로 수행되었습니다")))
                .andExpect(jsonPath("$.status").value(200));
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

    //    ResultActions makeGetResultActions(String url, String content, LinkedMultiValueMap queryParams) throws Exception {
    ResultActions makeGetResultActions(String url) throws Exception {
        return mockMvc.perform(get(url)
//                        .queryParams(queryParams)
//                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    ResultActions makeGetResultActions(String url, String jwtToken) throws Exception {
        return mockMvc.perform(get(url)
                        .header("Authorization", jwtToken)
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
}