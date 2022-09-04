package com.practice.springbasic.domain.board.dto;

import com.practice.springbasic.domain.board.BoardCategory;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateDto {
    @Enumerated(EnumType.STRING) @NotNull
    BoardCategory boardCategory;
    @NotNull @NotEmpty @Length( min = 5, max = 20)
    String title;
    @NotNull @NotEmpty @Length( min = 10, max = 1000)
    String content;
}
