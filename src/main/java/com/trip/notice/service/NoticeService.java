package com.trip.notice.service;

import com.trip.notice.dto.Notice;
import java.util.List;

public interface NoticeService {
    List<Notice> getAllNotices();
    Notice getNoticeById(int noticeId);
    List<Notice> getPagedNotices(int page, int size);
    Notice createNotice(Notice notice);
    Notice updateNotice(int noticeId, Notice noticeDetails);
    void deleteNotice(int noticeId);
}
