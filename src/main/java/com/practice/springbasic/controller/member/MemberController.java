package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.utils.SuccessResult;
import com.practice.springbasic.domain.Member;
import org.springframework.validation.BindingResult;

public interface MemberController {
    SuccessResult joinMember(Member member, BindingResult bindingResult);

    SuccessResult findMember(Member member, BindingResult bindingResult);

    SuccessResult updateMember(Long id, Member member, BindingResult bindingResult);

    boolean deleteMember(Long id, Member member);

    void duplicateEmail(String email);

    void duplicateNickname(String nickname);
}
