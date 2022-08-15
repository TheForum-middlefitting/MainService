package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.form.DeleteMemberForm;
import com.practice.springbasic.controller.utils.SuccessResult;
import com.practice.springbasic.domain.Member;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberController {
    SuccessResult joinMember(Member member, BindingResult bindingResult);

    SuccessResult loginMember(HttpServletResponse response, Member member, BindingResult bindingResult);

    SuccessResult updateMember(HttpServletRequest request, Long id, Member member, BindingResult bindingResult);

    SuccessResult deleteMember(HttpServletRequest request, Long id, DeleteMemberForm memberForm, BindingResult bindingResult);

    void duplicateEmailCheck(String email);

    void duplicateNicknameCheck(String nickname);
}
