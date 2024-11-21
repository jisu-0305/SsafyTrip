package com.trip.comment.service;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.dto.CommentDto;
import com.trip.comment.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<CommentDto> getCommentsByAttractionId(int attractionId) {
        return commentMapper.findCommentsByAttractionId(attractionId);
    }
}
