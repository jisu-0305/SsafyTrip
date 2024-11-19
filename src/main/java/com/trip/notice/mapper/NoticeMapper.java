package com.trip.notice.mapper;

import com.trip.notice.dto.NoticeListResponseDto;
import com.trip.notice.dto.NoticeUpdateRequestDto;
import com.trip.notice.dto.NoticeCreateRequestDto;
import com.trip.notice.dto.NoticeDetailDto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface NoticeMapper {
    List<NoticeListResponseDto> selectNotices(
        @Param("offset") int offset, 
        @Param("size") int size, 
        @Param("keyword") String keyword
    );
    int selectNoticesTotalCount(@Param("keyword") String keyword);
    NoticeDetailDto selectById(@Param("noticeId") int noticeId);
    void insert(NoticeCreateRequestDto notice);
    void update(@Param("noticeId") int noticeId, 
                @Param("notice") NoticeUpdateRequestDto notice);
    void delete(int noticeId);
}
