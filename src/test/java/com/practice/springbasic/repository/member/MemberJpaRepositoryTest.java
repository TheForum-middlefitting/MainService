package com.practice.springbasic.repository.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    private Member member;

    @BeforeEach
    public void createMember() {
        member = memberSample();
    }

    @Test
    public void createAndRead() throws Exception{
        memberJpaRepository.save(member);

        Member result = memberJpaRepository.findByEmailAndPassword("middleFitting@gmail.com", "helloSpringBoot!").get();

        assertThat(member.getId()).isEqualTo(member.getId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    public void update() throws Exception{
        memberJpaRepository.save(member);
        MemberDto memberUpdateDto = MemberDto.builder()
                .email(member.getEmail() + '2')
                .nickname(member.getNickname() + '2')
                .password(member.getPassword() + '2')
                .build();

        member.memberUpdate(memberUpdateDto);
        memberJpaRepository.save(member);

        assertThat(member.getId()).isEqualTo(member.getId());
        assertThat(member.getEmail()).isEqualTo(memberUpdateDto.getEmail());
        assertThat(member.getNickname()).isEqualTo(memberUpdateDto.getNickname());
        assertThat(member.getPassword()).isEqualTo(memberUpdateDto.getPassword());
    }

    @Test
    public void delete() throws Exception{
        memberJpaRepository.save(member);
        memberJpaRepository.delete(member);

        Optional<Member> result = memberJpaRepository.findByEmailAndPassword("middleFitting@gmail.com", "helloSpringBoot!");

        assertThat(result.orElse(null)).isEqualTo(null);
    }

    @Test
    public void duplicateEmailFind() throws Exception{
        memberJpaRepository.save(member);

        Optional<Member> result = memberJpaRepository.findByEmail(member.getEmail());

        assertThat(member).isEqualTo(result.orElse(null));
    }

    @Test
    public void duplicateEmailNotFind() throws Exception{
        Optional<Member> result = memberJpaRepository.findByEmail(member.getEmail());

        assertThat(result.orElse(null)).isNull();
    }

    @Test
    public void duplicateNicknameFind() throws Exception{
        memberJpaRepository.save(member);

        Optional<Member> result = memberJpaRepository.findByNickname(member.getNickname());

        assertThat(member).isEqualTo(result.orElse(null));
    }

    @Test
    public void duplicateNicknameNotFind() throws Exception{
        Optional<Member> result = memberJpaRepository.findByNickname(member.getNickname());

        assertThat(result.orElse(null)).isNull();
    }

    @Test
    public void findByIdAndPasswordFind() throws Exception{
        memberJpaRepository.save(member);

        Optional<Member> result = memberJpaRepository.findByIdAndPassword(member.getId(), member.getPassword());

        assertThat(member).isEqualTo(result.orElse(null));
    }

    @Test
    public void findByIdAndPasswordNotFind() throws Exception{
        memberJpaRepository.save(member);
        Optional<Member> result = memberJpaRepository.findByIdAndPassword(member.getId(), member.getPassword() + "2");
        Optional<Member> result2 = memberJpaRepository.findByIdAndPassword(member.getId() + 1L, member.getPassword());

        assertThat(result.orElse(null)).isNull();
        assertThat(result2.orElse(null)).isNull();
    }

    private Member memberSample() {
        return Member.builder()
                .email("middleFitting@gmail.com")
                .nickname("middleFitting")
                .password("helloSpringBoot!")
                .build();
    }
}