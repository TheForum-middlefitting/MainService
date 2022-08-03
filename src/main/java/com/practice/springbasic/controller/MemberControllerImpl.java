package com.practice.springbasic.controller;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
import org.springframework.web.bind.annotation.*;

public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;

    public MemberControllerImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    @PostMapping("members")
    public Long joinMember(Member member) {
        memberService.join(member);
        return member.getId();
    }

    @Override
    @GetMapping("members")
    public Member findMember(Member member) {
        return memberService.find(member.getNickname(), member.getPassword()).orElse(null);
    }

    @Override
    @PutMapping("members")
    public Member updateMember(Member member) {
        MemberDto memberDto = createMemberDto(member);
        Long result = memberService.update(memberDto);
        if (result == 0L)
            return null;
        return member;
    }

    @Override
    @DeleteMapping("members")
    public boolean deleteMember(Member member) {
        return memberService.withdrawal(member.getNickname(), member.getPassword());
    }

    public MemberDto createMemberDto(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }
}
