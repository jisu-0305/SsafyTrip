package com.trip.attraction.service;

import com.trip.attraction.dto.*;
import com.trip.attraction.entity.GuGun;
import com.trip.attraction.entity.Sido;
import com.trip.attraction.mapper.AttractionMapper;
import com.trip.attraction.mapper.ContentTypeMapper;
import com.trip.attraction.repository.GuGunRepository;
import com.trip.attraction.repository.SidoRepository;
import com.trip.attraction.util.OverviewDataUtil;
import com.trip.comment.dto.CommentDto;
import com.trip.comment.service.CommentService;
import com.trip.favorite.service.FavoriteService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;
    private final SidoRepository sidoRepository;
    private final GuGunRepository guGunRepository;
    private final ContentTypeMapper contentTypeMapper;
    private final CommentService commentService;
    private final FavoriteService favoriteService;
    private final OverviewDataUtil overviewDataUtil;

    @Override
    public AttractionInitDataResponseDto getAttractionInitialData(int page, int size) {
        int offset = (page - 1) * size;

        int totalCount = attractionMapper.countTotalAttractions();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        List<SidoDto> sidoList = getSidoList();
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

    public List<SidoDto> getSidoList() {
        List<Sido> sidos = sidoRepository.findAll();
        return sidos.stream()
                .map(SidoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<GuGunDto> getGuGunList(int sidoCode) {
        List<GuGun> guGuns = guGunRepository.findBySidoCode(sidoCode);
        return guGuns.stream()
                .map(GuGunDto::fromEntity)
                .collect(Collectors.toList());
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

    @Override
    public Map<String, List<String>> getTitlesByDate(ScheduleDetailDto scheduleDetail) {
        Map<String, List<String>> titlesByDate = new HashMap<>();

        scheduleDetail.getSchedulePlacesByDate().forEach(dateEntry -> {
            String date = dateEntry.getDate();
            List<String> titles = new ArrayList<>();

            dateEntry.getPlaces().forEach(place -> {
                String title = attractionMapper.findTitleByAttractionId(place.getAttractionId());
                if (title != null) {
                    titles.add(title);
                }
            });

            titlesByDate.put(date, titles);
        });

        return titlesByDate;
    }
}
