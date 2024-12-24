package com.trip.comment.mapper;

import com.trip.comment.dto.CommentDto;
import com.trip.comment.dto.CommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentDto> findCommentsByAttractionId(@Param("attractionId") int attractionId);

    List<CommentResponseDto> findCommentsByUserId(@Param("userId") Long userId);
}