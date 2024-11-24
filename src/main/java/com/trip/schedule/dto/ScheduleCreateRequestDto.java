package com.trip.schedule.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class ScheduleCreateRequestDto {
    private ScheduleInformationDto scheduleInformation;
    private List<SchedulePlaceDto> schedulePlaces;
}