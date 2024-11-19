package com.trip.attraction.mapper;

import com.trip.attraction.dto.ContentTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentTypeMapper {
    List<ContentTypeDto> selectAllContentTypes();
}
