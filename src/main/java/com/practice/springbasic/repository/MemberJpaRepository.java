package com.practice.springbasic.repository;

import com.practice.springbasic.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNicknameAndPassword(String nickname, String password);

    void delete(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
