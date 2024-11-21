package com.trip.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private int id;
    private String email;
    private LocalDateTime createdAt;
    private String content;
}