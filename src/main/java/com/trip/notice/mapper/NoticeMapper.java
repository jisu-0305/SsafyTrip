package com.trip.notice.mapper;

import com.trip.notice.dto.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> findAll();
    List<Notice> findPaged(int offset, int size);
    Notice findById(int noticeId);
    void insert(Notice notice);
    void update(Notice notice);
    void delete(int noticeId);
}
