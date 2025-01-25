package com.trip.notice.service;

import com.trip.notice.dto.*;
import com.trip.notice.mapper.NoticeMapper;
import com.trip.review.dto.S3ResponseDTO;
import com.trip.review.util.S3Util;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private static final int MIN_PAGE = 1;
    private final S3Util s3Util;
    private final String FOLDER_NAME  = "notice";

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
        noticeMapper.insertNotice(notice);
        return NoticeCreateResponseDto.of();
    }

    @Override
    @Transactional
    public NoticeUpdateResponseDto updateNotice(int noticeId, NoticeUpdateRequestDto dto) {
        getNoticeById(noticeId); 
        noticeMapper.updateNotice(noticeId, dto); 
        return NoticeUpdateResponseDto.of();
    }

    @Override
    @Transactional
    public NoticeDeleteResponseDto deleteNotice(int noticeId) {
        getNoticeById(noticeId);
        noticeMapper.deleteNotice(noticeId);
        return NoticeDeleteResponseDto.of();
    }

    // s3를 통해 이미지 업로드
    public S3ResponseDTO uploadImage(MultipartFile request) {

        S3ResponseDTO s3ResponseDTO= null;
        try {
            s3ResponseDTO =s3Util.imageUpload(request, FOLDER_NAME);
        } catch (IOException e) {

        }

        return s3ResponseDTO;
    }

    // s3에 저장된 이미지 삭제하기
    public void deleteImage(HttpSession session){

        List<String> s3KeyList = (List<String>) session.getAttribute("S3Keys");
        if (s3KeyList != null) {
            s3KeyList.parallelStream().forEach(key -> s3Util.deleteImage(key, FOLDER_NAME));
        }

        session.removeAttribute("S3Keys");
        session.removeAttribute("S3Urls");
    }
}
