package com.trip.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchedulePlaceDto {
    private int attractionId;
    private LocalDateTime visitTime;
    private String memo;
    private int cost;
    private int visitOrder;
}
