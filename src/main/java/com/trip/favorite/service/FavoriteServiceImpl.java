package com.trip.favorite.service;

import com.trip.attraction.dto.AttractionDto;
import com.trip.attraction.dto.PagedAttractionResponseDto;
import com.trip.favorite.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public void addFavorite(Long userId, int attractionId) {
        boolean alreadyFavorited = favoriteMapper.isAlreadyFavorited(userId, attractionId);

        if (!alreadyFavorited) {
            favoriteMapper.insertFavorite(userId, attractionId);

            favoriteMapper.updateHit(attractionId, 1);
        } else {
            throw new IllegalStateException("이미 좋아요를 누른 상태입니다.");
        }
    }

    @Override
    public void removeFavorite(Long userId, int attractionId) {
        boolean alreadyFavorited = favoriteMapper.isAlreadyFavorited(userId, attractionId);

        if (alreadyFavorited) {
            favoriteMapper.deleteFavorite(userId, attractionId);

            favoriteMapper.updateHit(attractionId, -1);
        } else {
            throw new IllegalStateException("좋아요하지 않은 상태에서 삭제 요청을 보낼 수 없습니다.");
        }
    }

    @Override
    public PagedAttractionResponseDto getFavoriteAttractions(Long userId, int page, int size, String word) {
        int offset = (page - 1) * size;

        int totalCount = favoriteMapper.countFavoriteAttractions(userId, word);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        List<AttractionDto> attractionList = favoriteMapper.selectFavoriteAttractions(userId, word, offset, size);

        PagedAttractionResponseDto responseDto = new PagedAttractionResponseDto();
        responseDto.setAttractionList(attractionList);
        responseDto.setTotalCount(totalCount);
        responseDto.setTotalPages(totalPages);

        return responseDto;
    }

    public boolean isLikedByUser(Long userId, int attractionId) {
        if (userId == null) {
            return false; // 비로그인 사용자는 false 반환
        }
        return favoriteMapper.isLikedAttraction(userId, attractionId);
    }
}
