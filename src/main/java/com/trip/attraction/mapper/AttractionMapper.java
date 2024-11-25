package com.trip.attraction.mapper;

import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.AttractionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttractionMapper {
    List<AttractionDto> getAttractions(int offset, int size);
    AttractionDetailDto getAttractionDetail(int attractionId);

    // 조건에 맞는 관광지 리스트 조회
    List<AttractionDto> searchAttractions(
            @Param("sidoCode") Integer sidoCode,
            @Param("gugunCode") Integer gugunCode,
            @Param("type") Integer type,
            @Param("word") String word,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection
    );

    // 조건에 맞는 관광지의 총 갯수 조회
    int countFilteredAttractions(
            @Param("sidoCode") Integer sidoCode,
            @Param("gugunCode") Integer gugunCode,
            @Param("type") Integer type,
            @Param("word") String word
    );

    int countTotalAttractions();

    void updateAttractionViews(@Param("attractionId") int attractionId);

    void updateHit(@Param("attractionId") int attractionId, @Param("amount") int amount);
}

