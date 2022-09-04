package com.practice.springbasic.domain.comment;

import com.practice.springbasic.domain.base.BaseConstructorEntity;
import com.practice.springbasic.domain.base.BaseEntity;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseConstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @NotNull @NotEmpty @Size( min = 10, max = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @NotNull
    private Board board;
    @Builder
    public Comment(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }

    public void commentUpdate(CommentUpdateDto commentUpdateDto) {
        this.content = commentUpdateDto.getContent();
    }
}