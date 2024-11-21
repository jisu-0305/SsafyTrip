package com.trip.favorite.service;

public interface FavoriteService {
    void addFavorite(Long userId, int attractionId);
    void removeFavorite(Long userId, int attractionId);
}
