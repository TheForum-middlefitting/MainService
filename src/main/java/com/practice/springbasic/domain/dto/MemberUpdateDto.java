package com.practice.springbasic.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdateDto {
    private String  nickname;
    private String  email;
    private String  password;

    @Builder
    public MemberUpdateDto(String nickname, String email, String password) {
        if(nickname == null || email == null || password == null) {
            throw new IllegalStateException("필수 파라미터가 누락되었습니다!");
        }

        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
