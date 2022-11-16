package com.practice.springbasic.controller.member.vo;

import lombok.*;

@Getter
public class ResponseMemberForm {
    Long memberId;
    String email;
    String nickname;

    @Builder
    public ResponseMemberForm(Long memberId, String nickname, String email) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
    }
}
