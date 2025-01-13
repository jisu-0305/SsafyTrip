package com.trip.review.repository;

import com.trip.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 네이티브 쿼리(nativeQuery = true)를 사용 시, JPA가 엔티티 필드 타입(LocalDateTime)으로 자동 매핑해주지 않음.
    // 대신, 데이터베이스에서 반환된 결과는 JDBC의 기본 타입인 java.sql.Timestamp로 제공
    @Query(value = "SELECT b.review_id AS reviewId, b.email AS email, b.title AS title, " +
            "b.content AS content, b.hit AS hit, b.created_at AS createdAt, " +
            "b.updated_at AS updatedAt, " +
            "(SELECT bi.image_url " +
            " FROM review_images bi " +
            " WHERE bi.review_id = b.review_id " +
            " ORDER BY bi.image_id ASC " +
            " LIMIT 1) AS firstImageUrl " +
            "FROM reviews b",
            nativeQuery = true)
    List<Object[]>  findAllreviewsWithFirstImageTemp();

    @Query(value = "SELECT b.review_id AS reviewId, b.email AS email, b.title AS title, " +
            "b.content AS content, b.hit AS hit, b.created_at AS createdAt, " +
            "b.updated_at AS updatedAt, " +
            "(SELECT bi.image_url " +
            " FROM review_images bi " +
            " WHERE bi.review_id = b.review_id " +
            " ORDER BY bi.image_id ASC " +
            " LIMIT 1) AS firstImageUrl " +
            "FROM reviews b",
            countQuery = "SELECT COUNT(*) FROM reviews b",
            nativeQuery = true)
    Page<Object[]> findAllReviewsWithFirstImage(Pageable pageable);
}
