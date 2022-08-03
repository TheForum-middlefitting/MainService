package com.practice.springbasic.controller;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
class MemberControllerImplTest {

    @Mock
    MemberService memberService;
    @InjectMocks
    MemberControllerImpl memberController;
    private Member member;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void createMember() {
        member = memberSample();
    }

    @Test
    public void joinMemberSuccess() throws Exception{
        when(memberService.join(member)).thenReturn(member);

        Long result = memberController.joinMember(member);

        assertThat(result).isEqualTo(member.getId());
    }

    @Test
    public void findMemberSuccess() throws Exception{
        when(memberService.find(member.getNickname(), member.getPassword())).thenReturn(Optional.ofNullable(member));

        Member result = memberController.findMember(member);

        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findMemberFailed() throws Exception{
        when(memberService.find(member.getNickname(), member.getPassword())).thenReturn(Optional.ofNullable(null));

        Member result = memberController.findMember(member);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void updateMemberSuccess() throws Exception{
        MemberDto memberDto = memberDtoSample();
        when(memberService.update(ArgumentMatchers.any(MemberDto.class))).thenReturn(1L);
        System.out.println(memberService.update(memberDto));

        Member result = memberController.updateMember(member);

        assertThat(result).isEqualTo(member);
    }

    @Test
    public void updateMemberFailed() throws Exception{
        MemberDto memberDto = memberDtoSample();
        when(memberService.update(ArgumentMatchers.any(MemberDto.class))).thenReturn(0L);
        System.out.println(memberService.update(memberDto));

        Member result = memberController.updateMember(member);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void deleteMemberSuccess() throws Exception{
        when(memberService.withdrawal(member.getNickname(), member.getPassword())).thenReturn(true);

        boolean result = memberController.deleteMember(member);

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void deleteMemberFailed() throws Exception{
        when(memberService.withdrawal(member.getNickname(), member.getPassword())).thenReturn(false);

        boolean result = memberController.deleteMember(member);

        assertThat(result).isEqualTo(false);
    }

    private Member memberSample() {
        return Member.builder()
                .email("middlefitting@google.com")
                .password("%middlefitting")
                .nickname("middlefitting")
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