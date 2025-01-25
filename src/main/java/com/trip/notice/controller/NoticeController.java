package com.trip.notice.controller;

import java.util.ArrayList;
import java.util.List;

import com.trip.global.SuccessRes;
import com.trip.review.dto.ReviewImageResponseDTO;
import com.trip.review.dto.S3ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trip.notice.dto.NoticeCreateRequestDto;
import com.trip.notice.dto.NoticeCreateResponseDto;
import com.trip.notice.dto.NoticeDeleteResponseDto;
import com.trip.notice.dto.NoticeListResponseDto;
import com.trip.notice.dto.NoticeUpdateRequestDto;
import com.trip.notice.dto.NoticeUpdateResponseDto;
import com.trip.notice.dto.NoticeDetailDto;
import com.trip.notice.dto.PagedNoticeResponseDto;
import com.trip.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notices")
@Tag(name = "Notice", description = "공지사항 API")
@Slf4j
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "모든 공지사항 조회 (페이징)", description = "페이징 처리를 통해 공지사항 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<PagedNoticeResponseDto> getPagedNotices(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "검색어 (선택적)", example = "")
        @RequestParam(required = false) String keyword) {  
    
        PagedNoticeResponseDto pagedNotices = noticeService.getPagedNotices(page, size, keyword);
        return ResponseEntity.ok(pagedNotices);
    }
    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailDto> getNoticeById(
            @Parameter(description = "조회할 공지사항의 ID", example = "1")
            @PathVariable int noticeId) {
        NoticeDetailDto notice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(notice);
    }

    @Operation(summary = "공지사항 작성", description = "새로운 공지사항을 작성합니다.")
    @PostMapping
    public ResponseEntity<NoticeCreateResponseDto> createNotice(
            @Valid @RequestBody NoticeCreateRequestDto requestDto, 
            HttpSession session) {
        //수정 필요
        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }
        
        NoticeCreateResponseDto response = noticeService.createNotice(requestDto);

        session.removeAttribute("S3Keys");
        session.removeAttribute("S3Urls");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "공지사항 수정", description = "ID로 특정 공지사항을 수정합니다.")
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeUpdateResponseDto> updateNotice(
            @Parameter(description = "수정할 공지사항의 ID", example = "1")
            @PathVariable int noticeId,
            @Valid @RequestBody NoticeUpdateRequestDto requestDto,
            HttpSession session) {

        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }

        NoticeUpdateResponseDto updatedNotice = noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(updatedNotice);
    }

    @Operation(summary = "공지사항 삭제",
            description = "ID로 특정 공지사항을 삭제합니다. 삭제를 위해 로그인이 필요합니다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<NoticeDeleteResponseDto> deleteNotice(
            @Parameter(description = "삭제할 공지사항의 ID", example = "1")
            @PathVariable int noticeId,
            HttpSession session) {

        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }
        
        NoticeDeleteResponseDto response = noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "S3에 이미지 업로드", description = "이미지를 S3에 업로드하고 URL 반환, 파라미터명 보이는 것과 같이 upload로!")
    @PostMapping("/upload-image")
    public ResponseEntity<ReviewImageResponseDTO> uploadImage(@RequestPart("upload") MultipartFile request, HttpSession session) {
        // s3에 이미지 업로드
        S3ResponseDTO s3ResponseDTO = noticeService.uploadImage(request);

        // 세션에서 기존 URL, key List를 가져오기
        List<String> s3KeyList = (List<String>) session.getAttribute("S3Keys");
        List<String> s3UrlList = (List<String>) session.getAttribute("S3Urls");

        if (s3KeyList == null) {
            s3KeyList = new ArrayList<>();
            s3UrlList = new ArrayList<>();
        }

        // 업로드된 URL을 세션에 추가
        s3KeyList.add(s3ResponseDTO.getS3Key());
        s3UrlList.add(s3ResponseDTO.getS3Url());

        session.setAttribute("S3Keys", s3KeyList);
        session.setAttribute("S3Urls", s3UrlList);

        return ResponseEntity.ok().body(new ReviewImageResponseDTO(s3ResponseDTO.getS3Url(), "success"));
    }


    @Operation(summary = "S3 이미지 삭제", description = "세션에 저장된 모든 이미지를 삭제, Quill Editor에서 저장하지 않고 이미지만 업로드 시, 반드시 호출해주기!")
    @DeleteMapping("/clear-session")
    public ResponseEntity<SuccessRes> clearSessionImages(HttpSession session) {
        noticeService.deleteImage(session);
        return ResponseEntity.ok().body(new SuccessRes("세션에 저장된 모든 이미지가 삭제되었습니다."));
    }
}