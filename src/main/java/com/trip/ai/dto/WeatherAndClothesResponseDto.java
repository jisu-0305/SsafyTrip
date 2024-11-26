package com.trip.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAndClothesResponseDto {
    private List<WeatherDto> weatherList;
    private String clothesURL;
    private String supplies;
}