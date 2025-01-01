package com.trip.review.service;


import com.trip.review.dto.*;
import com.trip.review.entity.Review;
import com.trip.review.entity.ReviewImage;
import com.trip.review.repository.ReviewImageRepository;
import com.trip.review.repository.ReviewRepository;
import com.trip.review.util.S3Util;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewSerivce {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final S3Util s3Util;


    // 게시판 리스트 얻어오기
    public PagedResponseDTO getPagedReviews(int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "updated_at"));
        Page<Object[]> reviewPageObject = reviewRepository.findAllReviewsWithFirstImage(pageable);

        Page<ReviewResponseDTO> reviewPage = reviewPageObject.map(objects -> new ReviewResponseDTO(
                ((Number) objects[0]).longValue(),       // reviewId
                (String) objects[1],                    // email
                (String) objects[2],                    // title
                (String) objects[3],                    // content
                ((Number) objects[4]).intValue(),       // hit
                (objects[5] != null ? ((java.sql.Timestamp) objects[5]).toLocalDateTime() : null), // createdAt
                (objects[6] != null ? ((java.sql.Timestamp) objects[6]).toLocalDateTime() : null), // updatedAt
                (String) objects[7]                     // firstImageUrl
        ));

        return PagedResponseDTO.builder().content(reviewPage.getContent())
                .totalPages(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .build();
    }


    // 특정 게시판 조회
    public ReviewResponseDTO getReviewById(Long reviewId) {

        Optional<Review> review = reviewRepository.findById(reviewId);

        ReviewResponseDTO reviewResponseDTO = review.map(reviewEntity -> {
            reviewEntity.setHit(reviewEntity.getHit() + 1); // 조회수 증가
            reviewRepository.save(reviewEntity);
            return ReviewResponseDTO.fromEntity(reviewEntity);
        }).orElseThrow(() -> new EntityNotFoundException("review not found"));

        return reviewResponseDTO;
    }


    // 게시판 작성(게시판 + s3이미지) 반환값 작성하기
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO, HttpSession session) {

        Review review = reviewRepository.save(reviewRequestDTO.toEntity());

        List<String> s3KeyList = (List<String>) session.getAttribute("S3Keys");
        List<String> s3UrlList = (List<String>) session.getAttribute("S3Urls");


        if (s3KeyList != null && s3UrlList != null && s3KeyList.size() == s3UrlList.size()) {
            List<ReviewImage> reviewImageList = new ArrayList<>();
            for(int i=0; i<s3KeyList.size(); i++){
                ReviewImage reviewImage = ReviewImage.builder().review(review)
                        .imageUrl(s3UrlList.get(i))
                        .s3Key(s3KeyList.get(i))
                        .build();

                reviewImageList.add(reviewImage);
            }
            reviewImageRepository.saveAll(reviewImageList);

            session.removeAttribute("S3Keys");
            session.removeAttribute("S3Urls");
        }

        return ReviewResponseDTO.fromEntity(review);
    }


    // 게시판 수정
    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO reviewRequestDTO) {

        Review review = reviewRepository.findById(reviewId).get();
        if(!review.getEmail().equals(reviewRequestDTO.getEmail())){
            throw new IllegalArgumentException("이메일 불일치 email: " + reviewRequestDTO.getEmail());
        }

        review.update(reviewRequestDTO);
        reviewRepository.save(review);

        Optional<Review> saveReview = reviewRepository.findById(review.getReviewId());

        ReviewResponseDTO reviewResponseDTO = saveReview.map(reviewEntity -> ReviewResponseDTO.fromEntity(reviewEntity))
                .orElseThrow(() -> new EntityNotFoundException("review not found"));

        return reviewResponseDTO;
    }


    // 게시판 삭제
    public void deleteReview(Long reviewId, DeleteRequestDTO deleteRequestDTO) {

        // 1. 게시글 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + reviewId));

        if(!review.getEmail().equals(deleteRequestDTO.getEmail())){
            throw new IllegalArgumentException("이메일 불일치 email: " + deleteRequestDTO.getEmail());
        }

        // 2. s3삭제
        List<String> s3KeyList = reviewImageRepository.findS3KeysByreviewId(reviewId);
        s3KeyList.forEach(s3Util::deleteImage); // 병렬 스트림 대신 일반 스트림 사용

        //3. 게시글 삭제(delete cascade로 image도 같이 삭제)
        reviewRepository.delete(review);
    }


    // s3를 통해 이미지 업로드
    public S3ResponseDTO uploadImage(MultipartFile request) {

        S3ResponseDTO s3ResponseDTO= null;
        try {
            s3ResponseDTO =s3Util.imageUpload(request);
        } catch (IOException e) {

        }

        return s3ResponseDTO;
    }


    // s3에 저장된 이미지 삭제하기
    public void deleteImage(HttpSession session){

        List<String> s3KeyList = (List<String>) session.getAttribute("S3Keys");
        if (s3KeyList != null) {
            s3KeyList.parallelStream().forEach(key -> s3Util.deleteImage(key));
        }

        session.removeAttribute("S3Keys");
        session.removeAttribute("S3Urls");
    }
}
