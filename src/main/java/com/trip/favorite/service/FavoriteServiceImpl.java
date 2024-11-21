package com.trip.favorite.service;

import com.trip.attraction.service.AttractionService;
import com.trip.favorite.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final AttractionService attractionService;

    @Override
    public void addFavorite(Long userId, int attractionId) {
        boolean alreadyFavorited = favoriteMapper.isAlreadyFavorited(userId, attractionId);

        if (!alreadyFavorited) { //오류 검증
            favoriteMapper.insertFavorite(userId, attractionId);

            attractionService.incrementHit(attractionId);
        } else {
            throw new IllegalStateException("이미 좋아요를 누른 상태입니다.");
        }
    }

    @Override
    public void removeFavorite(Long userId, int attractionId) {
        boolean alreadyFavorited = favoriteMapper.isAlreadyFavorited(userId, attractionId);

        if (alreadyFavorited) { //오류 검증
            favoriteMapper.deleteFavorite(userId, attractionId);

            attractionService.decrementHit(attractionId);
        } else {
            throw new IllegalStateException("좋아요하지 않은 상태에서 삭제 요청을 보낼 수 없습니다.");
        }
    }
}
