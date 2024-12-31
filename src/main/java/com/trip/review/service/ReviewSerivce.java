package com.trip.review.service;


import com.trip.review.dto.ReviewRequestDTO;
import com.trip.review.dto.ReviewResponseDTO;
import com.trip.review.dto.S3ResponseDTO;
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
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.sql.Timestamp;
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
    public Page<ReviewResponseDTO>getPagedReviews(int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "created_at"));
        Page<Object[]> reviewPage = reviewRepository.findAllReviewsWithFirstImage(pageable);

        return reviewPage.map(objects -> new ReviewResponseDTO(
                ((Number) objects[0]).longValue(),       // reviewId
                (String) objects[1],                    // email
                (String) objects[2],                    // title
                (String) objects[3],                    // content
                ((Number) objects[4]).intValue(),       // hit
                (objects[5] != null ? ((java.sql.Timestamp) objects[5]).toLocalDateTime() : null), // createdAt
                (objects[6] != null ? ((java.sql.Timestamp) objects[6]).toLocalDateTime() : null), // updatedAt
                (String) objects[7]                     // firstImageUrl
        ));
    }




    public List<ReviewResponseDTO>getPagedReviewsTemp(int page, int size) {

        List<Object[]> result = reviewRepository.findAllreviewsWithFirstImageTemp();

        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        result.stream().forEach(row -> {
            // reviewResponseDTO 생성 및 리스트에 추가
            reviewResponseDTOList.add(
                    new ReviewResponseDTO(
                            (long) row[0],  // reviewId (Long 변환 필요)
                            (String) row[1],                   // email
                            (String) row[2],                   // title
                            (String) row[3],                   // content
                            (int) row[4],  // hit (Integer 변환 필요)
                            ((Timestamp) row[5]).toLocalDateTime(),                   // createdAt
                            ((Timestamp)row[6]).toLocalDateTime(),                   // updatedAt
                            (String) row[7]                    // firstImageUrl (nullable 가능)
                    )
            );
        });

        return reviewResponseDTOList;
    }



    // 특정 게시판 조회
    public ReviewResponseDTO getReviewById(Long reviewId) {

        Optional<Review> review = reviewRepository.findById(reviewId);

        ReviewResponseDTO reviewResponseDTO = review.map(reviewEntity -> ReviewResponseDTO.fromEntity(reviewEntity))
                .orElseThrow(() -> new EntityNotFoundException("review not found"));

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
        review.update(reviewRequestDTO);
        reviewRepository.save(review);


        Optional<Review> savereview = reviewRepository.findById(review.getReviewId());

        ReviewResponseDTO reviewResponseDTO = savereview.map(reviewEntity -> ReviewResponseDTO.fromEntity(reviewEntity))
                .orElseThrow(() -> new EntityNotFoundException("review not found"));

        return reviewResponseDTO;
    }


    // 게시판 삭제
    public void deleteReview(Long reviewId) {

        // 1. 게시글 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + reviewId));

        // 2. s3삭제
        List<String> s3KeyList = reviewImageRepository.findS3KeysByreviewId(reviewId);
        s3KeyList.forEach(s3Util::deleteImage); // 병렬 스트림 대신 일반 스트림 사용

        //3. 게시글 삭제(delete cascade로 image도 같이 삭제)
        reviewRepository.delete(review);
    }


    // s3를 통해 이미지 업로드
    public S3ResponseDTO uploadImage(MultipartRequest request) {

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
