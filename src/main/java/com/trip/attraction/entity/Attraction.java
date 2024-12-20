package com.trip.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attractions")
@Getter
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private int contentId;
    private String title;
    private int contentTypeId;
    private int areaCode;
    private int siGunGuCode;
    private double latitude;
    private double longitude;
    private String addr1;
    private String addr2;
    private String firstImage1;
    private String firstImage2;
    private int mapLevel;
    private String tel;
    @Column(length = 2000)
    private String overview;
    @Column(length = 500)
    private String homepage;
    private int hit;
    private int views;
}
