package com.trip.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentResponseDto {
    private int id;
    private int attractionId;
    private String attractionName;
    private String image;
    private String content;
    private Date createdAt;
}