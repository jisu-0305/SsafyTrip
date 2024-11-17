package com.trip.attraction.service;

import com.trip.attraction.dto.AttractionListDto;
import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.ContentTypeDto;
import com.trip.attraction.dto.GuGunDto;
import com.trip.attraction.dto.SidoDto;
import com.trip.attraction.mapper.AttractionMapper;
import com.trip.attraction.mapper.ContentTypeMapper;
import com.trip.attraction.mapper.SidoGunMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {
    private final AttractionMapper attractMapper;
    private final SidoGunMapper sidoGunMapper;
    private final ContentTypeMapper contentTypeMapper;

    @Override
    public List<AttractionListDto> getAttractionList(Integer areaCode, Integer sigunguCode, Integer contentTypeId, String keyword) {
        return attractMapper.selectAllAttractions(areaCode, sigunguCode, contentTypeId, keyword);
    }

    @Override
    public AttractionDetailDto getAttractionDetail(int contentId) {
        return attractMapper.selectAttractionById(contentId);
    }

    @Override
    public List<SidoDto> getSidosList() {
        return sidoGunMapper.selectAllSidos();
    }

    @Override
    public List<GuGunDto> getGuGunList(int sidoCode) {
        return sidoGunMapper.selectGuGunsBySidoCode(sidoCode);
    }

    @Override
    public List<ContentTypeDto> getContentList() {
        return contentTypeMapper.selectAllContentTypes();
    }
}
