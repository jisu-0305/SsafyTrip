package com.trip.attraction.dto;

import com.trip.attraction.entity.Attraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDetailDto {
    private int no;
    private int contentId;
    private String title;
    private int contentTypeId;
    private int areaCode;
    private int siGunGuCode;
    private String firstImage1;
    private String firstImage2;
    private int mapLevel;
    private double latitude;
    private double longitude;
    private String tel;
    private String addr1;
    private String addr2;
    private String overview;
    private int hit;
    private int views;
    private Boolean isLike;

    // Attraction 엔티티를 AttractionDetailDto로 변환
    public static AttractionDetailDto fromEntity(Attraction attraction, Boolean isLike) {
        return new AttractionDetailDto(
                attraction.getNo(),
                attraction.getContentId(),
                attraction.getTitle(),
                attraction.getContentTypeId(),
                attraction.getAreaCode(),
                attraction.getSiGunGuCode(),
                attraction.getFirstImage1(),
                attraction.getFirstImage2(),
                attraction.getMapLevel(),
                attraction.getLatitude(),
                attraction.getLongitude(),
                attraction.getTel(),
                attraction.getAddr1(),
                attraction.getAddr2(),
                attraction.getOverview(),
                attraction.getHit(),
                attraction.getViews(),
                isLike
        );
    }
}
