package com.trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedAttractionResponseDto {
    private List<AttractionDto> attractionList;
    private int totalCount;
    private int totalPages;
}
