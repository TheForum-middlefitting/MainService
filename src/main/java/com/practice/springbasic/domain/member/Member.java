package com.practice.springbasic.domain.member;

import com.practice.springbasic.domain.base.BaseEntity;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
//    @Column(nullable = false, unique = true)
//    private String userId;
    @NotNull @NotEmpty @Length(min=4, max=20)
    @Column(unique = true)
    private String  nickname;
    @NotNull @NotEmpty @Email
    @Column(unique = true)
    private String  email;
    @NotNull @NotEmpty @Length(min=10)
    private String  password;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private final List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private final List<Comment> comment = new ArrayList<>();

    @Builder
    public Member(String nickname, String email, String password) throws IllegalStateException{
        if(nickname == null || email == null || password == null) {
            throw new IllegalStateException("필수 파라미터가 누락되었습니다!");
        }
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void memberUpdate(MemberDto memberDto) {
        this.nickname = memberDto.getNickname();
        this.email = memberDto.getEmail();
        this.password = memberDto.getPassword();
    }

}
