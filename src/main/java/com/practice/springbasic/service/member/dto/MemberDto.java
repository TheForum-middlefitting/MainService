package com.practice.springbasic.service.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDto {
    private String  nickname;
    private String  email;
    private String  password;
}
