package com.practice.springbasic.domain.board;

import com.practice.springbasic.domain.Member;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;
    @Enumerated(EnumType.STRING) @NotNull @NotEmpty
    private BoardCategory boardCategory;
    @NotNull @NotEmpty @Length( min = 5, max = 20)
    private String title;
    @NotNull @NotEmpty @Length( min = 10, max = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(BoardCategory boardCategory, String title, String content, Member member) throws IllegalStateException{
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void boardUpdate(BoardUpdateDto boardUpdateDto) {
        this.boardCategory = boardUpdateDto.getBoardCategory();
        this.title = boardUpdateDto.getTitle();
        this.content = boardUpdateDto.getContent();
    }
}
