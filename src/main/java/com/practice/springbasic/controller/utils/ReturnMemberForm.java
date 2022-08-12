package com.practice.springbasic.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
