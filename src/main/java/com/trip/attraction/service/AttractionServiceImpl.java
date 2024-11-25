package com.trip.attraction.service;

import com.trip.attraction.dto.*;

import com.trip.attraction.mapper.*;
import com.trip.attraction.util.OverviewDataUtil;

import com.trip.comment.dto.CommentDto;
import com.trip.comment.service.CommentService;
import com.trip.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;
    private final SidoGunMapper sidoGunMapper;
    private final ContentTypeMapper contentTypeMapper;
    private final CommentService commentService;
    private final FavoriteService favoriteService;
    private final OverviewDataUtil overviewDataUtil;

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
    public AttractionDetailResponseDto getAttractionDetailWithComments(int attractionId, Long userId) {
        attractionMapper.updateAttractionViews(attractionId);
        AttractionDetailDto attractionDetailDto = attractionMapper.getAttractionDetail(attractionId);

        boolean isLike = userId != null && favoriteService.isLikedByUser(userId, attractionId);
        attractionDetailDto.setIsLike(isLike);

        String overview = overviewDataUtil.getOverview(String.valueOf(attractionDetailDto.getContentId()), String.valueOf(attractionDetailDto.getContentTypeId()));
        attractionDetailDto.setOverview(overview);

        List<CommentDto> comments = commentService.getCommentsByAttractionId(attractionId);

        return AttractionDetailResponseDto.builder()
                .attract(attractionDetailDto)
                .commentList(comments)
                .build();
    }

    public AttractionDto enrichWithLikeStatus(AttractionDto attraction, Long userId) {
        boolean isLike = favoriteService.isLikedByUser(userId, attraction.getNo());
        attraction.setIsLike(isLike);
        return attraction;
    }

    public List<AttractionDto> enrichWithLikeStatus(List<AttractionDto> attractions, Long userId) {
        return attractions.stream()
                .map(attraction -> enrichWithLikeStatus(attraction, userId))
                .collect(Collectors.toList());
    }
}
