package com.practice.springbasic.service.member;

import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.member.MemberJpaRepository;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.utils.check.CommonCheckUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberJpaRepository memberRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member join(MemberDto memberDto) {
        memberDto = MemberDto.builder()
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .build();
        Member member = modelMapper.map(memberDto, Member.class);
        memberRepository.save(member);
        return member;
    }

    @Override
    public Optional<Member> findMemberByEmailAndPassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        CommonCheckUtil.nullCheck400(member.orElse(null), "LoginFailedByWrongInput");
        CommonCheckUtil.booleanCheck400(passwordEncoder.matches(password, member.orElse(null).getPassword()), "LoginFailedByWrongInput");
        return member;
//        return memberRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Long update(Member member, MemberDto memberDto) {
        member.memberUpdate(memberDto);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void withdrawal(Long memberId, String password){
//        Member member = memberRepository.findByIdAndPassword(memberId, password).orElse(null);
//        CommonCheckUtil.nullCheck400(member, PasswordWrong);

        Member member = memberRepository.findById(memberId).orElse(null);
        CommonCheckUtil.nullCheck400(member, "LoginFailedByWrongInput");
        CommonCheckUtil.booleanCheck400(passwordEncoder.matches(password, member.getPassword()), "PasswordWrong");
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
