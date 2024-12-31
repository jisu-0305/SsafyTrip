package com.trip.review.repository;

import com.trip.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    // 특정 게시글의 모든 이미지 삭제
    void deleteByreview_reviewId(Long reviewId);

    // review_id에 해당하는 모든 s3_key 값을 가져오는 메서드
    // JPQL은 SQL과 달리 데이터베이스 컬럼명이 아닌, 엔티티 클래스와 필드명
    @Query("SELECT ri.s3Key FROM ReviewImage ri WHERE ri.review.reviewId = :reviewId")
    List<String> findS3KeysByreviewId(@Param("reviewId") Long reviewId);

    // 네이티브 쿼리(nativeQuery = true)**를 사용할 때는 스네이크 표기법을 그대로 사용 가능
    @Query(value = "SELECT s3_key FROM review_image WHERE review_id = :reviewId", nativeQuery = true)
    List<String> findS3KeysByreviewId2(@Param("reviewId") Long reviewId);
}
