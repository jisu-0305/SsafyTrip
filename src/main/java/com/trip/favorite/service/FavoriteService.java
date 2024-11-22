package com.trip.favorite.service;

import com.trip.attraction.dto.AttractionDto;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long userId, int attractionId);
    void removeFavorite(Long userId, int attractionId);
    List<AttractionDto> getFavoriteAttractions(Long userId);
}
