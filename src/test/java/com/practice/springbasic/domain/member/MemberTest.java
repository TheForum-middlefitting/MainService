package com.practice.springbasic.domain.member;

import com.practice.springbasic.service.member.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void memberCreateSuccess() throws Exception{
        Member member = Member.builder()
                .email("middleFitting@gmail.com")
                .password("hello@Entity!")
                .nickname("middleFitting")
                .build();
        em.persist(member);

        assertThat(member.getId()).isEqualTo(member.getId());
        assertThat(member.getEmail()).isEqualTo(member.getEmail());
        assertThat(member.getPassword()).isEqualTo(member.getPassword());
        assertThat(member.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void memberCreateFail() throws Exception{
        try {
            Member member = Member.builder()
                    .password("hello@Entity!")
                    .nickname("middleFitting")
                    .build();
        } catch (IllegalStateException e3) {
            assertThat(e3.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");
        }
        finally {
            IllegalStateException e1 = assertThrows(IllegalStateException.class,
                    () ->   Member.builder()
                            .email("middleFitting@gmail.com")
                            .nickname("middleFitting")
                            .build());
             assertThat(e1.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");

            IllegalStateException e2 = assertThrows(IllegalStateException.class,
                    () ->   Member.builder()
                            .email("middleFitting@gmail.com")
                            .password("hello@Entity!")
                            .build());
            assertThat(e2.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");
        }
    }

    @Test
    public void memberUpdateSuccess() throws Exception{
        Member member = Member.builder()
                .email("middleFitting@gmail.com")
                .password("hello@Entity!")
                .nickname("middleFitting")
                .build();
        em.persist(member);
        Long firstId = member.getId();

        MemberDto memberUpdateDto = MemberDto.builder()
                .email(member.getEmail() + '2')
                .nickname(member.getNickname() + '2')
                .password(member.getPassword() + '2')
                .build();
        member.memberUpdate(memberUpdateDto);
        em.persist(member);

        assertThat(member.getId()).isEqualTo(firstId);
        assertThat(member.getEmail()).isEqualTo(memberUpdateDto.getEmail());
        assertThat(member.getPassword()).isEqualTo(memberUpdateDto.getPassword());
        assertThat(member.getNickname()).isEqualTo(memberUpdateDto.getNickname());
  }

//    @Test
//    public void memberUpdateFail() throws Exception{
//        MemberDto memberUpdateDtoEx = new MemberDto("nickname", "email", "password");
//
//        try {
//            MemberDto memberUpdateDto = MemberDto.builder()
//                    .password("hello@Entity!")
//                    .nickname("middleFitting")
//                    .build();
//        } catch (IllegalStateException e3) {
//            assertThat(e3.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");
//        }
//        finally {
//            IllegalStateException e1 = assertThrows(IllegalStateException.class,
//                    () ->   MemberDto.builder()
//                            .email("middleFitting@gmail.com")
//                            .nickname("middleFitting")
//                            .build());
//            assertThat(e1.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");
//
//            IllegalStateException e2 = assertThrows(IllegalStateException.class,
//                    () ->   MemberDto.builder()
//                            .email("middleFitting@gmail.com")
//                            .password("hello@Entity!")
//                            .build());
//            assertThat(e2.getMessage()).isEqualTo("필수 파라미터가 누락되었습니다!");
//        }
//    }
}