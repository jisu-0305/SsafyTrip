package com.trip.attraction.mapper;

import com.trip.attraction.dto.GuGunDto;
import com.trip.attraction.dto.SidoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SidoGunMapper {
    List<SidoDto> getSidoList();
    List<GuGunDto> getGuGunList(int sidoCode);
}
