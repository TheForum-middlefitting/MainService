package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.member.dto.EmailCheckForm;
import com.practice.springbasic.controller.member.dto.LoginMemberForm;
import com.practice.springbasic.controller.form.SuccessResult;
import com.practice.springbasic.controller.member.dto.NicknameCheckForm;
import com.practice.springbasic.domain.member.Member;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberController {
    SuccessResult joinMember(HttpServletResponse response, Member member, BindingResult bindingResult);

    SuccessResult loginMember(HttpServletResponse response, LoginMemberForm loginMemberForm, BindingResult bindingResult);

    SuccessResult updateMember(HttpServletRequest request, Long id, Member member, BindingResult bindingResult);

    SuccessResult deleteMember(HttpServletRequest request, Long id, String password);

    SuccessResult duplicateEmailCheckAPI(EmailCheckForm emailCheckForm, BindingResult bindingResult);

    SuccessResult duplicateNicknameCheckAPI(NicknameCheckForm nicknameCheckForm, BindingResult bindingResult);
}
