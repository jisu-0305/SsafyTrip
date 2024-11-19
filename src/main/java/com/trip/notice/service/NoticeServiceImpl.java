package com.trip.notice.service;

import com.trip.notice.dto.Notice;
import com.trip.notice.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Notice> getAllNotices() {
        return noticeMapper.findAll();
    }

    @Override
    public List<Notice> getPagedNotices(int page, int size) {
        int offset = page * size;
        return noticeMapper.findPaged(offset, size);
    }

    @Override
    public Notice getNoticeById(int noticeId) {
        return Optional.ofNullable(noticeMapper.findById(noticeId))
                .orElseThrow(() -> new RuntimeException("해당 공지사항을 찾을 수 없습니다."));
    }

    @Override
    public Notice createNotice(Notice notice) {
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        noticeMapper.insert(notice);
        return notice;
    }

    @Override
    public Notice updateNotice(int noticeId, Notice noticeDetails) {
        Notice existingNotice = getNoticeById(noticeId);
        existingNotice.setTitle(noticeDetails.getTitle());
        existingNotice.setContent(noticeDetails.getContent());
        existingNotice.setImageUrl(noticeDetails.getImageUrl());
        noticeMapper.update(existingNotice);
        return existingNotice;
    }

    @Override
    public void deleteNotice(int noticeId) {
        getNoticeById(noticeId); // 존재 여부 확인
        noticeMapper.delete(noticeId);
    }
}
