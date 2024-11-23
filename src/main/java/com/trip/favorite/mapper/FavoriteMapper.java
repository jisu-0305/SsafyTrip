package com.trip.favorite.mapper;


import com.trip.attraction.dto.AttractionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    void insertFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    void deleteFavorite(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    boolean isAlreadyFavorited(@Param("userId") Long userId, @Param("attractionId") int attractionId);
    int countFavoriteAttractions(@Param("userId") Long userId, @Param("word") String word);
    List<AttractionDto> selectFavoriteAttractions(@Param("userId") Long userId, @Param("word") String word, @Param("offset") int offset, @Param("size") int size);
}