package com.trip.attraction.service;

import com.trip.attraction.dto.*;

import com.trip.attraction.mapper.*;
import com.trip.attraction.util.OverviewDataUtil;

import com.trip.comment.dto.CommentDto;
import com.trip.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;
    private final SidoGunMapper sidoGunMapper;
    private final ContentTypeMapper contentTypeMapper;
    private final CommentService commentService;

    @Override
    public AttractionInitDataResponseDto getAttractionInitialData(int page, int size) {
        int offset = (page - 1) * size;

        int totalCount = attractionMapper.countTotalAttractions();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        List<SidoDto> sidoList = sidoGunMapper.getSidoList();
        List<ContentTypeDto> contentTypeList = contentTypeMapper.selectAllContentTypes();
        List<AttractionDto> attractList = attractionMapper.getAttractions(offset, size);

        AttractionInitDataResponseDto response = new AttractionInitDataResponseDto();
        response.setSidoList(sidoList);
        response.setContentTypeList(contentTypeList);
        response.setAttractList(attractList);
        response.setTotalCount(totalCount);
        response.setTotalPages(totalPages);

        return response;
    }

    @Override
    public PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy) {
        int offset = (page - 1) * size;

        int totalCount = attractionMapper.countFilteredAttractions(sidoCode, gugunCode, type, word);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        String sortColumn;
        String sortDirection = switch (sortBy) {
            case "likes" -> {
                sortColumn = "hit";
                yield "DESC";
            }
            case "views" -> {
                sortColumn = "views";
                yield "DESC";
            }
            default -> {
                sortColumn = "no";
                yield "ASC";
            }
        };

        List<AttractionDto> attractionList = attractionMapper.searchAttractions(
                sidoCode, gugunCode, type, word, offset, size, sortColumn, sortDirection
        );
        
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
    public AttractionDetailResponseDto getAttractionDetailWithComments(int attractionId) {
        attractionMapper.updateAttractionViews(attractionId);
        AttractionDetailDto attractionDetailDto = attractionMapper.getAttractionDetail(attractionId);

        String overview = OverviewDataUtil.getOverview(String.valueOf(attractionDetailDto.getContentId()), String.valueOf(attractionDetailDto.getContentTypeId()));
        attractionDetailDto.setOverview(overview);


        List<CommentDto> comments = commentService.getCommentsByAttractionId(attractionId);

        return AttractionDetailResponseDto.builder()
                .attract(attractionDetailDto)
                .commentList(comments)
                .build();
    }

    @Override
    public void incrementHit(int attractionId) {
        attractionMapper.updateHit(attractionId, 1); // hit +1
    }

    @Override
    public void decrementHit(int attractionId) {
        attractionMapper.updateHit(attractionId, -1); // hit -1
    }
}
