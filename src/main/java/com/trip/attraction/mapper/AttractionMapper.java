package com.trip.attraction.mapper;

import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.AttractionListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttractionMapper {
    List<AttractionListDto> selectAllAttractions(
            @Param("areaCode") Integer areaCode,
            @Param("sigunguCode") Integer sigunguCode,
            @Param("contentTypeId") Integer contentTypeId,
            @Param("keyword") String keyword
    );

    AttractionDetailDto selectAttractionById(@Param("contentId") int contentId); // 추가된 메서드
}
