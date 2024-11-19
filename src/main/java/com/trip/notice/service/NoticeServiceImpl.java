package com.trip.notice.service;

import com.trip.notice.dto.*;
import com.trip.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private static final int MIN_PAGE = 1;

    @Override
    public PagedNoticeResponseDto getPagedNotices(int page, int size, String keyword) {
        if (page < MIN_PAGE) {
            throw new RuntimeException("페이지 번호는 1 이상이어야 합니다."); // 커스텀 예외 처리 고려
        }

        int offset = (page - 1) * size;
        int totalCount = noticeMapper.selectNoticesTotalCount(keyword);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        List<NoticeListResponseDto> notices = noticeMapper.selectNotices(offset, size, keyword);

        page = Math.min(page, totalPages);

        return PagedNoticeResponseDto.builder()
                .noticeList(notices)
                .totalPages(totalPages)
                .totalElements(totalCount)
                .pageNumber(page)
                .pageSize(size)
                .build();
    }
    

    @Override
    public NoticeDetailDto getNoticeById(int noticeId) {
        return Optional.ofNullable(noticeMapper.selectById(noticeId))
                .orElseThrow(() -> new RuntimeException("해당 공지사항을 찾을 수 없습니다.")); // 커스텀예외 처리 고려
    }

    @Override
    @Transactional
    public NoticeCreateResponseDto createNotice(NoticeCreateRequestDto notice) {
        noticeMapper.insert(notice);
        return NoticeCreateResponseDto.of();
    }

    @Override
    @Transactional
    public NoticeUpdateResponseDto updateNotice(int noticeId, NoticeUpdateRequestDto dto) {
        getNoticeById(noticeId); 
        noticeMapper.update(noticeId, dto); 
        return NoticeUpdateResponseDto.of();
    }

    @Override
    @Transactional
    public NoticeDeleteResponseDto deleteNotice(int noticeId) {
        getNoticeById(noticeId);
        noticeMapper.delete(noticeId);
        return NoticeDeleteResponseDto.of();
    }
}
