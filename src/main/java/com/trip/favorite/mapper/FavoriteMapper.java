package com.trip.favorite.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    void deleteFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    boolean isAlreadyFavorited(@Param("userId") Long userId, @Param("attractionId") int attractionId);
}