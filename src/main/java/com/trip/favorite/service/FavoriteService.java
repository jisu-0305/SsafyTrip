package com.trip.favorite.service;

public interface FavoriteService {
    void addFavorite(Long userId, int attractId);
    void removeFavorite(Long userId, int attractId, Long numId);
}
