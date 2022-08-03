package com.practice.springbasic.domain;

import com.practice.springbasic.domain.dto.MemberDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    private Long    id;
    private String  nickname;
    private String  email;
    private String  password;

    @Builder
    public Member(String nickname, String email, String password) throws IllegalStateException{
        if(nickname == null || email == null || password == null) {
            throw new IllegalStateException("필수 파라미터가 누락되었습니다!");
        }

        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void memberUpdate(MemberDto memberUpdateDto) {
        this.nickname = memberUpdateDto.getNickname();
        this.email = memberUpdateDto.getEmail();
        this.password = memberUpdateDto.getPassword();
    }

}
