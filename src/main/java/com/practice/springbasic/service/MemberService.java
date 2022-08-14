package com.practice.springbasic.service;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;

import java.util.Optional;

public interface MemberService {

    Member join(Member member);

    Optional<Member> find(String nickname, String password);

    Long update(Member member, MemberDto memberUpdateDto);

    void withdrawal(Member member);

    Boolean duplicateEmail(String email);

    Boolean duplicateNickname(String nickname);

    Optional<Member> findMemberByIdAndPassword(long id, String password);
}
