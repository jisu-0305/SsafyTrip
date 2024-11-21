package com.trip.favorite.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(@Param("userId") Long userId, @Param("attractId") int attractId);
    void deleteFavorite(@Param("userId") Long userId, @Param("attractId") int attractId, @Param("numId") Long numId);
}