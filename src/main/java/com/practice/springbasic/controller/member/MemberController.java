package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.form.DeleteMemberForm;
import com.practice.springbasic.controller.utils.SuccessResult;
import com.practice.springbasic.domain.Member;
import org.springframework.validation.BindingResult;

public interface MemberController {
    SuccessResult joinMember(Member member, BindingResult bindingResult);

    SuccessResult findMember(Member member, BindingResult bindingResult);

    SuccessResult updateMember(Long id, Member member, BindingResult bindingResult);

    SuccessResult deleteMember(Long id, DeleteMemberForm memberForm, BindingResult bindingResult);

    void duplicateEmailCheck(String email);

    void duplicateNicknameCheck(String nickname);
}
