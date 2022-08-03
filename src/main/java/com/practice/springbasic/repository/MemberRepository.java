package com.practice.springbasic.repository;

import com.practice.springbasic.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByNicknameAndPassword(String nickname, String password);
    void             delete(Member member);
}
