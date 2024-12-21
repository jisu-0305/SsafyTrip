package com.trip.attraction.service;

import com.trip.attraction.dto.*;
import com.trip.attraction.entity.Attraction;
import com.trip.attraction.repository.AttractionRepository;
import com.trip.attraction.repository.ContentTypeRepository;
import com.trip.attraction.repository.GuGunRepository;
import com.trip.attraction.repository.SidoRepository;
import com.trip.attraction.util.OverviewDataUtil;
import com.trip.comment.dto.CommentDto;
import com.trip.comment.service.CommentService;
import com.trip.favorite.service.FavoriteService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;
    private final SidoRepository sidoRepository;
    private final GuGunRepository guGunRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final CommentService commentService;
    private final FavoriteService favoriteService;
    private final OverviewDataUtil overviewDataUtil;

    @Override
    public AttractionInitDataResponseDto getAttractionInitialData(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Attraction> attractionsPage = attractionRepository.findAll(pageable);

        Map<Integer, String> contentTypeMap = getContentTypeList().stream()
                .collect(Collectors.toMap(ContentTypeDto::getContentTypeId, ContentTypeDto::getContentTypeName));

        List<AttractionDto> attractList = attractionsPage.getContent().stream()
                .map(attraction -> {
                    String contentTypeName = contentTypeMap.get(attraction.getContentTypeId());
                    //boolean isLike = userId != null && favoriteService.isLikedByUser(userId, attraction.getNo());
                    return AttractionDto.fromEntity(attraction, contentTypeName, null); // isLike는 이후 처리
                })
                .collect(Collectors.toList());

        List<SidoDto> sidoList = getSidoList();
        List<ContentTypeDto> contentTypeList = getContentTypeList();

        return new AttractionInitDataResponseDto(
                sidoList,
                contentTypeList,
                attractList,
                (int) attractionsPage.getTotalElements(),
                attractionsPage.getTotalPages()
        );
    }

    @Override
    public PagedAttractionResponseDto searchAttractions(Integer sidoCode, Integer gugunCode, Integer type, String word, int page, int size, String sortBy) {
        // 정렬 조건 설정
        Sort sort = switch (sortBy) {
            case "likes" -> Sort.by(Sort.Direction.DESC, "hit");
            case "views" -> Sort.by(Sort.Direction.DESC, "views");
            default -> Sort.by(Sort.Direction.ASC, "no");
        };

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Attraction> attractionsPage = attractionRepository.searchAttractions(sidoCode, gugunCode, type, word, pageable);

        Map<Integer, String> contentTypeMap = getContentTypeList().stream()
                .collect(Collectors.toMap(ContentTypeDto::getContentTypeId, ContentTypeDto::getContentTypeName));

        List<AttractionDto> attractionList = attractionsPage.getContent().stream()
                .map(attraction -> {
                    String contentTypeName = contentTypeMap.get(attraction.getContentTypeId());
                    //boolean isLike = userId != null && favoriteService.isLikedByUser(userId, attraction.getNo());
                    return AttractionDto.fromEntity(attraction, contentTypeName, null);}) //IsLike는 별도 처리
                .collect(Collectors.toList());

        // 응답 생성
        return new PagedAttractionResponseDto(
                attractionList,
                (int) attractionsPage.getTotalElements(),
                attractionsPage.getTotalPages()
        );
    }

    public List<SidoDto> getSidoList() {
        return sidoRepository.findAll().stream()
                .map(SidoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<GuGunDto> getGuGunList(int sidoCode) {
        return guGunRepository.findBySidoCode(sidoCode).stream()
                .map(GuGunDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ContentTypeDto> getContentTypeList() {
        return contentTypeRepository.findAll().stream()
                .map(ContentTypeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AttractionDetailResponseDto getAttractionDetailWithComments(int attractionId, Long userId) {
        attractionRepository.updateAttractionViews(attractionId);

        Attraction attraction = Optional.ofNullable(attractionRepository.findAttractionDetail(attractionId))
                .orElseThrow(() -> new RuntimeException("Attraction not found"));

        boolean isLike = userId != null && favoriteService.isLikedByUser(userId, attractionId);

        AttractionDetailDto attractionDetailDto = AttractionDetailDto.fromEntity(attraction, isLike);

        //상세 정보 추가
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
            List<String> titles = dateEntry.getPlaces().stream()
                    .map(place -> attractionRepository.findTitleByAttractionId(place.getAttractionId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            titlesByDate.put(date, titles);
        });

        return titlesByDate;
    }
}
