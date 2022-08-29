package com.practice.springbasic.service.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.domain.member.dto.MemberDto;
import com.practice.springbasic.repository.member.MemberJpaRepository;
import com.practice.springbasic.service.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
    @InjectMocks
    MemberServiceImpl memberService;
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
        when(memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword())).thenReturn(Optional.of(member));

        Member result = memberService.find(member.getEmail(), member.getPassword()).orElse(null);

        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findMemberFailed() throws Exception{
        Member result = memberService.find(member.getNickname(), member.getPassword()).orElse(null);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void updateMemberFound() throws Exception{
        MemberDto memberSuccessDto = memberDtoExistSample();

        Long result = memberService.update(member, memberSuccessDto);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void withdrawalMemberSuccess() throws Exception{
        when(memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword())).thenReturn(Optional.ofNullable(member));

        memberService.withdrawal(member);
    }

    @Test
    public void duplicateEmailFind() throws Exception{

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));

        Boolean result = memberService.duplicateEmail(member.getEmail());

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void duplicateEmailNotFind() throws Exception{

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(null));

        Boolean result = memberService.duplicateEmail(member.getEmail());

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void duplicateNicknameFind() throws Exception{

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.ofNullable(member));

        Boolean result = memberService.duplicateNickname(member.getNickname());

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void duplicateNicknameNotFind() throws Exception{

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.ofNullable(null));

        Boolean result = memberService.duplicateNickname(member.getNickname());

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void memberFindByIdAndPassword() throws Exception{

        when(memberRepository.findByIdAndPassword(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(member));

        Optional<Member> result = memberService.findMemberByIdAndPassword(1L, member.getPassword());

        assertThat(result.orElse(null)).isEqualTo(member);
    }

    @Test
    public void memberNotFindByIdAndPassword() throws Exception{
        when(memberRepository.findByIdAndPassword(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(null));

        Optional<Member> result = memberService.findMemberByIdAndPassword(1L, member.getPassword());

        assertThat(result.orElse(null)).isEqualTo(null);

    }

    @Test
    @DisplayName("")
    void findById() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));

        Optional<Member> result = memberService.findMemberById(1L);

        assertThat(result.orElse(null)).isEqualTo(member);
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