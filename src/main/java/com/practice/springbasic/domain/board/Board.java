package com.practice.springbasic.domain.board;

import com.practice.springbasic.domain.base.BaseConstructorEntity;
import com.practice.springbasic.domain.base.BaseEntity;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.service.board.dto.BoardDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseConstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Enumerated(EnumType.STRING) @NotNull
    private BoardCategory boardCategory;
    @NotNull @NotEmpty @Size( min = 5, max = 20)
    private String title;
    @NotNull @NotEmpty @Size( min = 10, max = 1000)
    private String content;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private final List<Comment> comment = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @Builder
    public Board(BoardCategory boardCategory, String title, String content, Member member) {
//        Assert.notNull(boardCategory, "잘못된 빌더");
//        Assert.notNull(title, "잘못된 빌더");
//        Assert.notNull(content, "잘못된 빌더");
//        Assert.notNull(member, "잘못된 빌더");
//        Assert.notEmpty(Collections.singleton(title), "잘못된 빌더");
//        Assert.notEmpty(Collections.singleton(content), "잘못된 빌더");
//        Assert.notEmpty((Collection<?>) member, "잘못된 빌더");
        if (boardCategory == null) {
            throw new IllegalArgumentException("잘못된 빌더");
        }
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void boardUpdate(BoardDto boardUpdateDto) {
        this.boardCategory = boardUpdateDto.getBoardCategory();
        this.title = boardUpdateDto.getTitle();
        this.content = boardUpdateDto.getContent();
    }
}
