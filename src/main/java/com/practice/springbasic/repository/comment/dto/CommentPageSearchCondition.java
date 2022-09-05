package com.practice.springbasic.repository.comment.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentPageSearchCondition {
    private Long commentId;
}
