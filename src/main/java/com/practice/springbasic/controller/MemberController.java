package com.practice.springbasic.controller;

import com.practice.springbasic.domain.Member;

public interface MemberController {
    Long joinMember(Member member);

    Member findMember(Member member);

    Member updateMember(Member member);

    boolean deleteMember(Member member);
}
