package com.trip.attraction.dto;

import com.trip.comment.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AttractionDetailResponseDto {
    private AttractionDetailDto attract;
    private List<CommentDto> commentList;
}
