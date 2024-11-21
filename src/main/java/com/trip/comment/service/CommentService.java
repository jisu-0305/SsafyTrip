package com.trip.comment.service;


import com.trip.comment.dto.CommentCreateRequestDto;

public interface CommentService {
    void createComment(int attractionId, CommentCreateRequestDto requestDto);
    void deleteComment(int commentId);
}