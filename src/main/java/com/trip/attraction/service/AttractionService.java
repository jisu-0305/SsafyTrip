package com.trip.attraction.service;

import com.trip.attraction.dto.AttractionDto;
import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.ContentTypeDto;
import com.trip.attraction.dto.GuGunDto;
import com.trip.attraction.dto.SidoDto;

import java.util.List;

public interface AttractionService {
    List<AttractionDto> getAttractionList(Integer areaCode, Integer sigunguCode, Integer contentTypeId, String keyword);

    AttractionDetailDto getAttractionDetail(int contentId);

    List<SidoDto> getSidosList();

    List<GuGunDto> getGuGunList(int sidoCode);

    List<ContentTypeDto> getContentList();
}
