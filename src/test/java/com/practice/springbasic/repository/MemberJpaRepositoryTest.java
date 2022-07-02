package com.practice.springbasic.repository;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void createAndRead() throws Exception{
        Member member = Member.builder()
                .email("middleFitting@gmail.com")
                .nickname("middleFitting")
                .password("helloSpringBoot!")
                .build();
        memberJpaRepository.save(member);

        Member result = memberJpaRepository.findByNicknameAndPassword("middleFitting", "helloSpringBoot!").get();
        assertThat(member.getId()).isEqualTo(member.getId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    public void update() throws Exception{
        Member member = Member.builder()
                .email("middleFitting@gmail.com")
                .nickname("middleFitting")
                .password("helloSpringBoot!")
                .build();
        memberJpaRepository.save(member);

        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
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
        Member member = Member.builder()
                .email("middleFitting@gmail.com")
                .nickname("middleFitting")
                .password("helloSpringBoot!")
                .build();
        memberJpaRepository.save(member);
        memberJpaRepository.delete(member);

        Optional<Member> result = memberJpaRepository.findByNicknameAndPassword("middleFitting", "helloSpringBoot!");
        assertThat(result.orElse(null)).isEqualTo(null);
    }
}