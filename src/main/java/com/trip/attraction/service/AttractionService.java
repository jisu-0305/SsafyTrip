package com.trip.attraction.service;

import com.trip.attraction.dto.*;

import java.util.List;

public interface AttractionService {

    AttractionInitDataResponseDto getAttractionInitialData(int page, int size);

    PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy);

    List<GuGunDto> getGuGunList(int sidoCode);

    AttractionDetailResponseDto getAttractionDetailWithComments(int attractionId);

    AttractionDto enrichWithLikeStatus(AttractionDto attraction, Long userId);

    List<AttractionDto> enrichWithLikeStatus(List<AttractionDto> attractions, Long userId);
}
