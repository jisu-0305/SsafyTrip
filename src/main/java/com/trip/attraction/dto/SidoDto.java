package com.trip.attraction.dto;

import com.trip.attraction.entity.Sido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SidoDto {
    private int sidoCode;
    private String sidoName;

    public static SidoDto fromEntity(Sido sido) {
        return new SidoDto(sido.getSidoCode(), sido.getSidoName());
    }
}
