package com.practice.springbasic.controller;

import com.practice.springbasic.domain.Member;

public interface MemberController {
    Long joinMember(Member member);

    Member findMember(Member member);

    Member updateMember(Long id, Member member);

    boolean deleteMember(Long id, Member member);

    boolean duplicateEmail(String email);

    boolean duplicateNickname(String nickname);
}
