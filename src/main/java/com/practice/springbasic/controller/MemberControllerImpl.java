package com.practice.springbasic.controller;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;

    public MemberControllerImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    @PostMapping("members")
    public Long joinMember(@RequestBody Member member) {
        if(memberService.duplicateEmail(member.getEmail())) return -1L;
        if(memberService.duplicateNickname(member.getNickname())) return -1L;
        memberService.join(member);
        return member.getId();
    }

    @Override
    @PostMapping("members/login")
    public Member findMember(@RequestBody Member member) {
        return memberService.find(member.getNickname(), member.getPassword()).orElse(null);
    }

    @Override
    @PutMapping("members/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member member) {
        if(memberService.duplicateEmail(member.getEmail())) return null;
        if(memberService.duplicateNickname(member.getNickname())) return null;
        MemberDto memberDto = createMemberDto(member);
        Long result = memberService.update(memberDto);
        if (result == 0L)
            return null;
        return member;
    }

    @Override
    @DeleteMapping("members/{id}")
    public boolean deleteMember(@PathVariable Long id, @RequestBody Member member) {
        return memberService.withdrawal(member.getNickname(), member.getPassword());
    }

    @Override
    public boolean duplicateEmail(String email) {
        return memberService.duplicateEmail(email);
    }

    @Override
    public boolean duplicateNickname(String nickname) {
        return memberService.duplicateNickname(nickname);
    }

    public MemberDto createMemberDto(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }

}
