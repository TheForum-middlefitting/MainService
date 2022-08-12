package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.utils.ReturnMemberForm;
import com.practice.springbasic.controller.utils.SuccessResult;
import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;

    public MemberControllerImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    @PostMapping("/members")
    public SuccessResult joinMember(@RequestBody @Validated Member member, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {throw new IllegalArgumentException("입력 값이 잘못되었습니다!");}
        duplicateEmail(member.getEmail());
        duplicateNickname(member.getNickname());
        memberService.join(member);
        ReturnMemberForm memberForm = createMemberForm(member);
        return new SuccessResult(memberForm, "success", 200);
    }

    @Override
    @PostMapping("/members/login")
    public SuccessResult findMember(@RequestBody @Validated Member member, BindingResult bindingResult) {
        Member find =  memberService.find(member.getNickname(), member.getPassword()).orElse(null);
        if(find == null) {throw new IllegalArgumentException("닉네임 혹은 패스워드가 잘못되었습니다!");}
        ReturnMemberForm memberForm = createMemberForm(member);
        return new SuccessResult(memberForm, "success", 200);
    }

    @Override
    @PutMapping("members/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody @Validated Member member) {
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
    public boolean deleteMember(@PathVariable Long id, @RequestBody @Validated Member member) {
        return memberService.withdrawal(member.getNickname(), member.getPassword());
    }

    @Override
    public void duplicateEmail(String email) {
        if (memberService.duplicateEmail(email)){
            throw new IllegalArgumentException("중복된 이메일입니다!");
        }
    }

    @Override
    public void duplicateNickname(String nickname) {
        if(memberService.duplicateNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다!");
        }
    }

    public MemberDto createMemberDto(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }

    public ReturnMemberForm createMemberForm(Member member) {
        return ReturnMemberForm.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

}
