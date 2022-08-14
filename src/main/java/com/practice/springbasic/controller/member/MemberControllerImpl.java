package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.form.DeleteMemberForm;
import com.practice.springbasic.controller.utils.ReturnMemberForm;
import com.practice.springbasic.controller.utils.SuccessResult;
import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.dto.MemberDto;
import com.practice.springbasic.service.MemberService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributesException;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@RestController
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;

    public MemberControllerImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    @PostMapping("/members")
    public SuccessResult joinMember(@RequestBody @Validated Member member, BindingResult bindingResult) {
        bindingResultCheck(bindingResult.hasErrors());
        duplicateEmailCheck(member.getEmail());
        duplicateNicknameCheck(member.getNickname());
        memberService.join(member);
        ReturnMemberForm memberForm = createMemberForm(member);
        return new SuccessResult(memberForm, "success", 200);
    }

    @Override
    @PostMapping("/members/login")
    public SuccessResult findMember(@RequestBody @Validated Member member, BindingResult bindingResult) {
        bindingResultCheck(bindingResult.hasErrors());
        Member find =  memberService.find(member.getEmail(), member.getPassword()).orElse(null);
        memberNullCheck(find);
        ReturnMemberForm memberForm = createMemberForm(find);
        return new SuccessResult(memberForm, "success", 200);
    }

    @Override
    @PutMapping("members/{id}")
    public SuccessResult updateMember(@PathVariable Long id, @RequestBody @Validated Member member, BindingResult bindingResult) {
        //++++++id 와 토큰의 id 불일치 하면 exception
        //이메일은 바뀌지 말아야 한다.
        bindingResultCheck(bindingResult.hasErrors());
        Member preMember = memberService.find(member.getEmail(), member.getPassword()).orElse(null);
        memberNullCheck(preMember);
        duplicateNicknameCheck(member.getNickname());
        MemberDto memberDto = createMemberDto(member);
        memberService.update(preMember, memberDto);
        ReturnMemberForm memberForm = createMemberForm(preMember);
        return new SuccessResult(memberForm, "success", 200);
    }

    @Override
    @DeleteMapping("members/{id}")
    public SuccessResult deleteMember(@PathVariable Long id, @RequestBody @Validated DeleteMemberForm memberForm, BindingResult bindingResult) {
        bindingResultCheck(bindingResult.hasErrors());
        Member compareMember = memberService.findMemberByIdAndPassword(id, memberForm.getPassword()).orElse(null);
        memberNullCheck(compareMember);
        memberService.withdrawal(compareMember);
        return new SuccessResult(null, "success", 200);
    }

    @Override
    public void duplicateEmailCheck(String email) {
        if (memberService.duplicateEmail(email)){
            throw new IllegalArgumentException("중복된 이메일입니다!");
        }
    }

    @Override
    public void duplicateNicknameCheck(String nickname) {
        if(memberService.duplicateNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다!");
        }
    }

    public void bindingResultCheck(boolean bindingResultHasError) {
        if(bindingResultHasError) {throw new IllegalArgumentException("입력 값이 잘못되었습니다!");}
    }

    public void memberNullCheck(Object checkValue) {
        if(checkValue == null) {throw new IllegalArgumentException("이메일 혹은 패스워드가 잘못되었습니다!");}
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
