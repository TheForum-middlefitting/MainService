package com.practice.springbasic.service;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;

import java.util.Optional;

public interface MemberService {

    Member join(Member member);

    Optional<Member> find(String nickname, String password);

    Long update(Member member, MemberDto memberUpdateDto);

    boolean withdrawal(String email, String password);

    Boolean duplicateEmail(String email);

    Boolean duplicateNickname(String nickname);

    Boolean findMemberByIdAndPassword(long id, String password);
}
