package com.trip.comment.service;


import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    void createComment(int attractionId, CommentCreateRequestDto requestDto);

    boolean deleteComment(int commentId, String loggedInEmail);

    List<CommentDto> getCommentsByAttractionId(int attractionId);
}