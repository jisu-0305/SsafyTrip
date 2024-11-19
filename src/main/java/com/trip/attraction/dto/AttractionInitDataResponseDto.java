package com.trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionInitDataResponseDto {
    private List<SidoDto> sidoList;
    private List<ContentTypeDto> contentTypeList;
    private List<AttractionDto> attractList;
    private int totalCount;
    private int totalPages;
}