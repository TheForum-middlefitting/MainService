package com.practice.springbasic.service.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.dto.MemberDto;

import java.util.Optional;

public interface MemberService {

    Member join(MemberDto memberDto);

    Optional<Member> findMemberByEmailAndPassword(String nickname, String password);

    Long update(Member member, MemberDto memberUpdateDto);

//    void withdrawal(Member member);
    void withdrawal(Long memberId, String password);

    Boolean duplicateEmail(String email);

    Boolean duplicateNickname(String nickname);

    Optional<Member> findMemberByIdAndPassword(long id, String password);

    Optional<Member> findMemberById(long id);
}
