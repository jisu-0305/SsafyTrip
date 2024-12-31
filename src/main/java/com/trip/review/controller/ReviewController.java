package com.trip.review.controller;


import com.trip.global.SuccessRes;
import com.trip.review.dto.*;
import com.trip.review.service.ReviewSerivce;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewSerivce reviewService;

    // 1. 게시판 전체 리스트 조회 (GET /reviews)
    @GetMapping
    public ResponseEntity<PagedResponseDTO> getPagedReviews(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        PagedResponseDTO pagedResponseDTO = reviewService.getPagedReviews(page, size);
        return ResponseEntity.ok().body(pagedResponseDTO);

    }

    // 2. 특정 게시판 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable("reviewId") Long reviewId) {

        ReviewResponseDTO reviewResponseDTO = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok().body(reviewResponseDTO);
    }

    // 3. 게시판 작성
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO, HttpSession session) {

        ReviewResponseDTO createdReview = reviewService.createReview(reviewRequestDTO, session);
        return ResponseEntity.ok().body(createdReview);
    }

    // 4. 게시물 수정 ( 이미지를 s3에 저장하는 과정 필요)
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewRequestDTO reviewRequestDTO
    ) {

        ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, reviewRequestDTO);
        return ResponseEntity.ok().body(updatedReview);
    }

    // 5. 게시판 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<SuccessRes> deleteReview(@PathVariable("reviewId") Long reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body(new SuccessRes("게시물이 성공적으로 삭제되었습니다."));
    }



    // 6. S3에 이미지 업로드하기
    @PostMapping("/upload-image")
    // MultipartRequest: 클라이언트로부터 업로드된 여러 파일(이미지)이나 폼 데이터를 처리하기 위해 사용
    public ResponseEntity<ReviewImageResponseDTO> uploadImage(@RequestPart("upload") MultipartFile request, HttpSession session){

        // s3에 이미지 업로드
        S3ResponseDTO s3ResponseDTO = reviewService.uploadImage(request);

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
        session.setAttribute("S3Urls",s3UrlList);

        return ResponseEntity.ok().body(new ReviewImageResponseDTO(s3ResponseDTO.getS3Url(), "success"));
    }


    // 7. S3에 저장된 이미지들 삭제하기(등록 취소 시)
    @DeleteMapping("/clear-session")
    public ResponseEntity<SuccessRes> clearSessionImages(HttpSession session) {

        reviewService.deleteImage(session);

        return ResponseEntity.ok().body(new SuccessRes("세션에 저장된 모든 이미지가 삭제되었습니다."));
    }
}
