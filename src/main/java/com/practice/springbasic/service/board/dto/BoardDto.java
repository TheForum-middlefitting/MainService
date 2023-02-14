package com.practice.springbasic.service.board.dto;

import com.practice.springbasic.domain.board.BoardCategory;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDto {
//    @Enumerated(EnumType.STRING) @NotNull
    private BoardCategory boardCategory;
//    @NotNull @NotEmpty
//    @Size( min = 5, max = 20)
    private String title;
//    @NotNull @NotEmpty @Size( min = 10, max = 1000)
    private String content;
}
