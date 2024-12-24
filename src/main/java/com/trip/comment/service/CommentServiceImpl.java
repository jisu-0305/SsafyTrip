package com.trip.comment.service;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.dto.CommentDto;
import com.trip.comment.dto.CommentResponseDto;
import com.trip.comment.entity.Comment;
import com.trip.comment.mapper.CommentMapper;
import com.trip.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public void createComment(int attractionId, CommentCreateRequestDto requestDto, long userId) {
        Comment comment = Comment.create(
                userId,
                attractionId,
                requestDto.getContent()
        );
        commentRepository.save(comment);
    }

    @Override
    public boolean deleteComment(int commentId, long userId) {
        if (!commentRepository.existsBycommentIdAndAuthorId(commentId, userId)) {
            return false;
        }
        commentRepository.deleteById(commentId);
        return true;
    }

    @Override
    public List<CommentDto> getCommentsByAttractionId(int attractionId) {
        return commentMapper.findCommentsByAttractionId(attractionId);
    }

    @Override
    public List<CommentResponseDto> getUserComments(Long userId) {
        return commentMapper.findCommentsByUserId(userId);
    }
}
