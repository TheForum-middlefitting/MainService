package com.practice.springbasic.service.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.member.MemberJpaRepository;
import com.practice.springbasic.service.member.dto.MemberDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceImplTest {
    @Mock MemberJpaRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

//    @InjectMocks
    MemberServiceImpl memberService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberRepository, modelMapper, passwordEncoder);
    }
    private Member member;
    private MemberDto memberDto;

    @BeforeEach
    public void createMember() {
        member = memberSample();
        memberDto = MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }

    @Test
    public void memberJoinSuccess() {
        when(memberRepository.save(any(Member.class))).thenReturn(null);

        Member result = memberService.join(memberDto);

        assertThat(result.getPassword()).isNotEqualTo(memberDto.getPassword());

        assertThat(result.getNickname()).isEqualTo(memberDto.getNickname());
        assertThat(result.getEmail()).isEqualTo(memberDto.getEmail());

    }

    @Test
    public void findMemberSuccess() {
//        when(memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword())).thenReturn(Optional.of(member));
//        Optional<Member> member = memberRepository.findByEmail(email);
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

//        Member result = memberService.findMemberByEmailAndPassword(member.getEmail(), member.getPassword()).orElse(null);
        Member result = memberService.findMemberByEmailAndPassword(member.getEmail(), "%middlefitting").orElse(null);


        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findMemberFailed() {
//        Member result = memberService.findMemberByEmailAndPassword(member.getNickname(), member.getPassword()).orElse(null);
//        assertThat(result).isEqualTo(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.findMemberByEmailAndPassword(member.getNickname(), member.getPassword()));
    }

    @Test
    public void updateMemberFound() {
        MemberDto memberSuccessDto = memberDtoExistSample();

        Long result = memberService.update(member, memberSuccessDto);

        assertThat(result).isEqualTo(null);
    }

    @Test
    public void withdrawalMemberSuccess() {
//        when(memberRepository.findByIdAndPassword(null, member.getPassword())).thenReturn(Optional.ofNullable(member));
        when(memberRepository.findById(null)).thenReturn(Optional.ofNullable(member));
        memberService.withdrawal(member.getId(), "%middlefitting");
    }

    @Test
    public void withdrawalMemberFailedByNotFound() {
        when(memberRepository.findByIdAndPassword(null, member.getPassword())).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.withdrawal(member.getId(), member.getPassword()));
        }

    @Test
    public void duplicateEmailFind() {

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));

        Boolean result = memberService.duplicateEmail(member.getEmail());

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void duplicateEmailNotFind() {

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

        Boolean result = memberService.duplicateEmail(member.getEmail());

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void duplicateNicknameFind() {

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.ofNullable(member));

        Boolean result = memberService.duplicateNickname(member.getNickname());

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void duplicateNicknameNotFind() {

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.empty());

        Boolean result = memberService.duplicateNickname(member.getNickname());

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void memberFindByIdAndPassword() {

        when(memberRepository.findByIdAndPassword(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(member));

        Optional<Member> result = memberService.findMemberByIdAndPassword(1L, member.getPassword());

        assertThat(result.orElse(null)).isEqualTo(member);
    }

    @Test
    public void memberNotFindByIdAndPassword() {
        when(memberRepository.findByIdAndPassword(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(Optional.empty());

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
//                .password("%middlefitting")
                .password(passwordEncoder.encode("%middlefitting"))
                .nickname("middlefitting")
                .build();
    }

    private MemberDto memberDtoExistSample() {
        return MemberDto.builder()
                .email("middlefitting@google.com")
//                .password("%middlefitting"
                .password(passwordEncoder.encode("%middlefitting"))
                .nickname("middlefitting")
                .build();
    }

}