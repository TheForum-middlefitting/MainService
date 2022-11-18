package com.practice.springbasic.service.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.repository.member.MemberJpaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MemberServiceImpl(MemberJpaRepository memberRepository, ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Member join(MemberDto memberDto) {
        Member member = modelMapper.map(memberDto, Member.class);
        memberRepository.save(member);
        return member;
    }

    @Override
    public Optional<Member> find(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Long update(Member member, MemberDto memberDto) {
        member.memberUpdate(memberDto);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void withdrawal(Member member){
        memberRepository.delete(member);
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
    public Optional<Member> findMemberByIdAndPassword(long id, String password) {
        return memberRepository.findByIdAndPassword(id, password);
    }

    @Override
    public Optional<Member> findMemberById(long id) {
        return memberRepository.findById(id);
    }
}
