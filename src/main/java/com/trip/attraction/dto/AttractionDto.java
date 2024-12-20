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
public class AttractionDto {
	private int no;
    private int contentId;
    private String title;
    private String contentType;
    private Boolean isLike;
    private double latitude;
    private double longitude;
    private String address;
    private String firstImage1;
    private int hit;
    private int views;

    public static AttractionDto fromEntity(Attraction attraction, String contentType, Boolean isLike) {
        return new AttractionDto(
                attraction.getNo(),
                attraction.getContentId(),
                attraction.getTitle(),
                contentType,
                isLike,
                attraction.getLatitude(),
                attraction.getLongitude(),
                attraction.getAddr1(),
                attraction.getFirstImage1(),
                attraction.getHit(),
                attraction.getViews()
        );
    }
}