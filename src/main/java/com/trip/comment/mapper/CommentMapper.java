package com.trip.comment.mapper;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.dto.CommentDto;
import com.trip.comment.dto.CommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(@Param("attractionId") int attractionId, @Param("requestDto") CommentCreateRequestDto requestDto);

    void deleteComment(@Param("commentId") int commentId);

    String findAuthorEmailByCommentId(@Param("commentId") int commentId);

    List<CommentDto> findCommentsByAttractionId(@Param("attractionId") int attractionId);

    List<CommentResponseDto> findCommentsByUserId(@Param("userId") Long userId);
}