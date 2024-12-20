package com.trip.attraction.repository;

import com.trip.attraction.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttractionRepository extends JpaRepository<Attraction, Integer> {

    // 조건에 맞는 관광지 리스트 조회
    @Query("""
        SELECT a FROM Attraction a 
        LEFT JOIN ContentType c ON a.contentTypeId = c.contentTypeId 
        WHERE (:sidoCode IS NULL OR a.areaCode = :sidoCode)
          AND (:gugunCode IS NULL OR a.siGunGuCode = :gugunCode)
          AND (:type IS NULL OR a.contentTypeId = :type)
          AND (:word IS NULL OR a.title LIKE %:word%)
    """)
    Page<Attraction> searchAttractions(
            @Param("sidoCode") Integer sidoCode,
            @Param("gugunCode") Integer gugunCode,
            @Param("type") Integer type,
            @Param("word") String word,
            Pageable pageable
    );

    // 조건에 맞는 관광지 총 갯수 조회
    @Query("""
        SELECT COUNT(a) FROM Attraction a 
        WHERE (:sidoCode IS NULL OR a.areaCode = :sidoCode)
          AND (:gugunCode IS NULL OR a.siGunGuCode = :gugunCode)
          AND (:type IS NULL OR a.contentTypeId = :type)
          AND (:word IS NULL OR a.title LIKE %:word%)
    """)
    int countFilteredAttractions(
            @Param("sidoCode") Integer sidoCode,
            @Param("gugunCode") Integer gugunCode,
            @Param("type") Integer type,
            @Param("word") String word
    );

    // 관광지 상세 정보 조회
    @Query("SELECT a FROM Attraction a WHERE a.no = :attractionId")
    Attraction findAttractionDetail(@Param("attractionId") int attractionId);

    // 관광지 조회수 업데이트
    @Query("UPDATE Attraction a SET a.views = a.views + 1 WHERE a.no = :attractionId")
    void updateAttractionViews(@Param("attractionId") int attractionId);

    // 제목 조회
    @Query("SELECT a.title FROM Attraction a WHERE a.no = :attractionId")
    String findTitleByAttractionId(@Param("attractionId") int attractionId);
}
