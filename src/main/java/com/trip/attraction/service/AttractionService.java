package com.trip.attraction.service;

import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.AttractionInitDataResponseDto;
import com.trip.attraction.dto.GuGunDto;
import com.trip.attraction.dto.PagedAttractionResponseDto;

import java.util.List;

public interface AttractionService {

    AttractionInitDataResponseDto getAttractionInitialData(int page, int size);

    PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy);

    List<GuGunDto> getGuGunList(int sidoCode);

    AttractionDetailDto getAttractionDetail(int attractionId);
}
