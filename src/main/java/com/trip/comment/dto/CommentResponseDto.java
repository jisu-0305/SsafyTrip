package com.trip.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentResponseDto {
    private int id;          // 댓글 ID
    private int attractionId; // 관광지 ID
    private String content;   // 댓글 내용
    private Date createdAt;   // 댓글 작성 시간
}