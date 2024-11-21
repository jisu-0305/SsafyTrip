package com.trip.comment.service;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public void createComment(int attractionId, CommentCreateRequestDto requestDto) {
        commentMapper.insertComment(attractionId, requestDto);
    }

    @Override
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }
}
