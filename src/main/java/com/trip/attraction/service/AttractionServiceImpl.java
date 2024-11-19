package com.trip.attraction.service;

import com.trip.attraction.dto.*;
import com.trip.attraction.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;
    private final SidoGunMapper sidoGunMapper;
    private final ContentTypeMapper contentTypeMapper;

    @Override
    public AttractionInitDataResponseDto getAttractionInitialData(int page, int size) {
        int offset = (page - 1) * size;

        // 총 게시물 갯수 조회
        int totalCount = attractionMapper.countTotalAttractions();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 각 Mapper에서 데이터 가져오기
        List<SidoDto> sidoList = sidoGunMapper.getSidoList();
        List<ContentTypeDto> contentTypeList = contentTypeMapper.selectAllContentTypes();
        List<AttractionDto> attractList = attractionMapper.getAttractions(offset, size);

        // DTO 구성
        AttractionInitDataResponseDto response = new AttractionInitDataResponseDto();
        response.setSidoList(sidoList);
        response.setContentTypeList(contentTypeList);
        response.setAttractList(attractList);
        response.setTotalCount(totalCount); // 총 게시물 갯수 추가
        response.setTotalPages(totalPages); // 총 페이지 수 추가

        return response;
    }

    @Override
    public PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy) {
        int offset = (page - 1) * size;

        // 총 게시물 갯수 조회
        int totalCount = attractionMapper.countFilteredAttractions(sidoCode, gugunCode, type, word);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 조건에 맞는 관광지 데이터 조회
        List<AttractionDto> attractionList = attractionMapper.searchAttractions(sidoCode, gugunCode, type, word, offset, size, sortBy);

        // DTO 구성
        PagedAttractionResponseDto pagedAttractionResponseDto = new PagedAttractionResponseDto();
        pagedAttractionResponseDto.setAttractionList(attractionList);
        pagedAttractionResponseDto.setTotalCount(totalCount);
        pagedAttractionResponseDto.setTotalPages(totalPages);

        return pagedAttractionResponseDto;
    }

    @Override
    public List<GuGunDto> getGuGunList(int sidoCode) {
        return sidoGunMapper.getGuGunList(sidoCode);
    }

    @Override
    public AttractionDetailDto getAttractionDetail(int attractionId) {
        return attractionMapper.getAttractionDetail(attractionId);
    }
}
