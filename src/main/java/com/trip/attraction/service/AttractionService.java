package com.trip.attraction.service;

import com.trip.attraction.dto.*;
import com.trip.schedule.dto.ScheduleDetailDto;

import java.util.List;
import java.util.Map;

public interface AttractionService {

    AttractionInitDataResponseDto getAttractionInitialData(int page, int size);

    PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy);

    List<GuGunDto> getGuGunList(int sidoCode);

    List<SidoDto> getSidoList();

    AttractionDetailResponseDto getAttractionDetailWithComments(int attractionId, Long userId);

    List<AttractionDto> enrichWithLikeStatus(List<AttractionDto> attractions, Long userId);

    public Map<String, List<String>> getTitlesByDate(ScheduleDetailDto scheduleDetail);
}
