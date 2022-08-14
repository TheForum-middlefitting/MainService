package com.practice.springbasic.controller.utils;

import lombok.*;

@Getter
@NoArgsConstructor
public class ReturnMemberForm {
    Long id;
    String email;
    String nickname;

    @Builder
    public ReturnMemberForm(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
