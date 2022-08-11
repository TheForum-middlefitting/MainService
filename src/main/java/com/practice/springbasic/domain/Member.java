package com.practice.springbasic.domain;

import com.practice.springbasic.domain.dto.MemberDto;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    private Long    id;
    @NotNull
    @Length(min=4, max=20)
    private String  nickname;
    @NotNull
    @Email
    private String  email;
    @NotNull
    @Length(min=10, max=20)
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
