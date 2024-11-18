package com.trip.attraction.dto;

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
    private String homepage;
    private String overview;
    private int hit;
    private int views;
}
