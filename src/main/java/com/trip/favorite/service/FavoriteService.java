package com.trip.favorite.service;

import com.trip.attraction.dto.PagedAttractionResponseDto;

public interface FavoriteService {
    void addFavorite(Long userId, int attractionId);
    void removeFavorite(Long userId, int attractionId);
    PagedAttractionResponseDto getFavoriteAttractions(Long userId, int page, int size, String word);
}
