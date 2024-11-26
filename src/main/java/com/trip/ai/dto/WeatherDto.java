package com.trip.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDto {
    private String date;
    private String location;
    private double highTemperature;
    private double lowTemperature;
    private String weatherCondition;
    private String precipitation;
}
