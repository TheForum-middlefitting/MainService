package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.member.dto.EmailCheckForm;
import com.practice.springbasic.controller.member.dto.LoginMemberForm;
import com.practice.springbasic.controller.form.SuccessResult;
import com.practice.springbasic.controller.member.dto.NicknameCheckForm;
import com.practice.springbasic.domain.member.Member;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface MemberController {
    SuccessResult joinMember(HttpServletResponse response, @Valid Member member, BindingResult bindingResult);

    SuccessResult loginMember(HttpServletResponse response, LoginMemberForm loginMemberForm, BindingResult bindingResult);
    SuccessResult getMember(HttpServletRequest request, Long id);

    SuccessResult updateMember(HttpServletRequest request, Long id, Member member, BindingResult bindingResult);

    SuccessResult deleteMember(HttpServletRequest request, Long id, String password);

//    SuccessResult duplicateEmailCheckAPI(EmailCheckForm emailCheckForm, BindingResult bindingResult);
//
//    SuccessResult duplicateNicknameCheckAPI(NicknameCheckForm nicknameCheckForm, BindingResult bindingResult);

    SuccessResult nicknameDuplicateCheck(@NotEmpty @Length(min=4, max=20) String nickname);

    SuccessResult emailDuplicateCheck(@NotEmpty @Email String email);
}
