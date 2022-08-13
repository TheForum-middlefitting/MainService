package com.practice.springbasic.service;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberJpaRepository memberRepository;

    public MemberServiceImpl(MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member join(Member member) {
        memberRepository.save(member);
        return member;
    }

    @Override
    public Optional<Member> find(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, password);
        return member;
    }

    @Override
    public Long update(Member member, MemberDto memberDto) {
        member.memberUpdate(memberDto);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public boolean withdrawal(String email, String password){
        Member member = find(email, password).orElse(null);
        if(member == null)
            return false;
        memberRepository.delete(member);
        return true;
    }

    @Override
    public Boolean duplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElse(null) != null;
    }

    @Override
    public Boolean duplicateNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        return member.orElse(null) != null;
    }

    @Override
    public Boolean findMemberByIdAndPassword(long id, String password) {
        Optional<Member> member = memberRepository.findByIdAndPassword(id, password);
        return member.orElse(null) != null;
    }
}
