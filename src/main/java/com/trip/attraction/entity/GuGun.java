package com.trip.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "guguns")
@Getter
public class GuGun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private int gugunCode;
    private String gugunName;
    private int sidoCode;
}