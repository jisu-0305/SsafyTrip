package com.trip.favorite.service;

import com.trip.favorite.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public void addFavorite(Long userId, int attractId) {
        favoriteMapper.insertFavorite(userId, attractId);
    }

    @Override
    public void removeFavorite(Long userId, int attractId, Long numId) {
        favoriteMapper.deleteFavorite(userId, attractId, numId);
    }
}
