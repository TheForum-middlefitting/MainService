package com.practice.springbasic.service;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.repository.MemberJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
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
    public Optional<Member> find(String nickname, String password) {
        Optional<Member> member = memberRepository.findByNicknameAndPassword(nickname, password);
        return member;
    }

    @Override
    public Long update(MemberDto memberDto) {
        Member member = find(memberDto.getNickname(), memberDto.getPassword()).orElse(null);
        if (member == null)
            return 0L;
        member.memberUpdate(memberDto);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public boolean withdrawal(String nickname, String password){
        Member member = find(nickname, password).orElse(null);
        if(member == null)
            return false;
        memberRepository.delete(member);
        return true;
    }
}
