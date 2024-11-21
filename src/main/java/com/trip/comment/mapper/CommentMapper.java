package com.trip.comment.mapper;

import com.trip.comment.dto.CommentCreateRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    void insertComment(@Param("attractionId") int attractionId, @Param("requestDto") CommentCreateRequestDto requestDto);

    void deleteComment(@Param("commentId") int commentId);
}