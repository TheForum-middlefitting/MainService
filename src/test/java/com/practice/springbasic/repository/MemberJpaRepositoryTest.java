package com.practice.springbasic.repository;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
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

        Member result = memberJpaRepository.findByNicknameAndPassword("middleFitting", "helloSpringBoot!").get();

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

        Optional<Member> result = memberJpaRepository.findByNicknameAndPassword("middleFitting", "helloSpringBoot!");

        assertThat(result.orElse(null)).isEqualTo(null);
    }

    private Member memberSample() {
        return Member.builder()
                .email("middleFitting@gmail.com")
                .nickname("middleFitting")
                .password("helloSpringBoot!")
                .build();
    }
}