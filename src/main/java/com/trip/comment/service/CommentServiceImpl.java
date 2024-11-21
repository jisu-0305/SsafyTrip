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
    public boolean deleteComment(int commentId, String loggedInEmail) {
        // 댓글 작성자 이메일 조회
        String authorEmail = commentMapper.findAuthorEmailByCommentId(commentId);

        // 검증 로직
        if (!loggedInEmail.equals(authorEmail)) {
            return false; // 작성자와 로그인 이메일이 다르면 삭제 불가
        }

        // 댓글 삭제
        commentMapper.deleteComment(commentId);
        return true;
    }

    @Override
    public List<CommentDto> getCommentsByAttractionId(int attractionId) {
        return commentMapper.findCommentsByAttractionId(attractionId);
    }
}
