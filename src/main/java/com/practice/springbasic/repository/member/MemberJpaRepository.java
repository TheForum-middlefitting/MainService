package com.practice.springbasic.repository.member;

import com.practice.springbasic.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String nickname, String password);

    void delete(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByIdAndPassword(Long id, String password);
}
