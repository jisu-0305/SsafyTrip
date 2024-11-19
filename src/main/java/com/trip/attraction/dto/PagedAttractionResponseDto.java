package com.trip.attraction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedAttractionResponseDto {
    private List<AttractionDto> attractionList;
    private int totalCount;
    private int totalPages;
}
