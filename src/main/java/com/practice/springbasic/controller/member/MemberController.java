package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.form.SuccessCreatedResult;
import com.practice.springbasic.controller.member.vo.LoginMemberForm;
import com.practice.springbasic.controller.form.SuccessResult;
import com.practice.springbasic.controller.member.vo.RequestMemberForm;
import com.practice.springbasic.controller.member.vo.ResponseMemberForm;
import com.practice.springbasic.domain.member.Member;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface MemberController {
    ResponseEntity<SuccessCreatedResult> joinMember(HttpServletResponse response, @Valid RequestMemberForm requestUserForm, BindingResult bindingResult);

    SuccessResult loginMember(HttpServletResponse response, LoginMemberForm loginMemberForm, BindingResult bindingResult);
    SuccessResult getMember(HttpServletRequest request, Long id);

    SuccessResult updateMember(HttpServletRequest request, Long id, Member member, BindingResult bindingResult);

    SuccessResult deleteMember(HttpServletRequest request, Long id, String password);

    SuccessResult nicknameDuplicateCheck(@NotEmpty @Length(min=4, max=20, message = "닉네임은 4자에서 20자 사이로 입력하세요!") String nickname);

    SuccessResult emailDuplicateCheck(@NotEmpty @Email String email);
}
