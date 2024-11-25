package com.trip.schedule.mapper;

import com.trip.schedule.dto.ScheduleInformationDto;
import com.trip.schedule.dto.ScheduleInformationResponseDto;
import com.trip.schedule.dto.SchedulePlaceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    void insertSchedule(@Param("userId") Long userId, @Param("schedule") ScheduleInformationDto schedule);

    void insertSchedulePlace(@Param("scheduleId") int scheduleId, @Param("place") SchedulePlaceDto place);

    void deleteSchedule(@Param("userId") Long userId, @Param("scheduleId") int scheduleId);

    List<ScheduleInformationResponseDto> selectAllSchedules(@Param("userId") Long userId);

    ScheduleInformationDto selectScheduleById(@Param("userId") Long userId, @Param("scheduleId") int scheduleId);

    List<SchedulePlaceDto> selectSchedulePlaces(@Param("scheduleId") int scheduleId);
}
