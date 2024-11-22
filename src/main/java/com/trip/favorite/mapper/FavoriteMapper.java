package com.trip.favorite.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    void deleteFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    boolean isAlreadyFavorited(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    List<Integer> selectFavoriteAttractionIdsByUserId(@Param("userId") Long userId);
}