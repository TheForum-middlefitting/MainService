package com.practice.springbasic.service;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Transactional
class MemberServiceImplTest {

    @Mock MemberJpaRepository memberRepository;
    @InjectMocks MemberServiceImpl memberService;
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
    public void memberJoinSuccess() throws Exception {
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.join(member);

        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findMemberSuccess() throws Exception{
        when(memberRepository.findByNicknameAndPassword(member.getNickname(), member.getPassword())).thenReturn(Optional.of(member));

        Member result = memberService.find(member.getNickname(), member.getPassword()).orElse(null);

        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findMemberFailed() throws Exception{
        when(memberRepository.findByNicknameAndPassword("differentNickname", "differentPassword")).thenReturn(Optional.ofNullable(null));

        Member result = memberService.find(member.getNickname(), member.getPassword()).orElse(null);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void updateMemberFound() throws Exception{
        MemberDto memberSuccessDto = memberDtoExistSample();
        when(memberRepository.findByNicknameAndPassword(memberSuccessDto.getNickname(), memberSuccessDto.getPassword())).thenReturn(Optional.ofNullable(member));

        Long result = memberService.update(memberSuccessDto);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void updateMemberNotFound() throws Exception{
        MemberDto memberFailedDto = memberDtoNotExistSample();
        when(memberRepository.findByNicknameAndPassword(memberFailedDto.getNickname(), memberFailedDto.getPassword())).thenReturn(Optional.ofNullable(null));

        Long result = memberService.update(memberFailedDto);

        assertThat(result).isEqualTo(0L);
    }

    @Test
    public void withdrawalMemberSuccess() throws Exception{
        MemberDto memberSuccessDto = memberDtoExistSample();
        when(memberRepository.findByNicknameAndPassword(memberSuccessDto.getNickname(), memberSuccessDto.getPassword())).thenReturn(Optional.ofNullable(member));

        boolean result = memberService.withdrawal(memberSuccessDto.getNickname(), memberSuccessDto.getPassword());

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void withdrawalMemberFailed() throws Exception{
        MemberDto memberFailedDto = memberDtoNotExistSample();
        when(memberRepository.findByNicknameAndPassword(memberFailedDto.getNickname(), memberFailedDto.getPassword())).thenReturn(Optional.ofNullable(null));

        boolean result = memberService.withdrawal(memberFailedDto.getNickname(), memberFailedDto.getPassword());

        assertThat(result).isEqualTo(false);
    }

    private Member memberSample() {
        return Member.builder()
                .email("middlefitting@google.com")
                .password("%middlefitting")
                .nickname("middlefitting")
                .build();
    }

    private MemberDto memberDtoExistSample() {
        return MemberDto.builder()
                .email("middlefitting@google.com")
                .password("%middlefitting")
                .nickname("middlefitting")
                .build();
    }

    private MemberDto memberDtoNotExistSample() {
        return MemberDto.builder()
                .email("differentNickname")
                .password("%differentPassword")
                .nickname("middlefitting")
                .build();
    }
}