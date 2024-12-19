package com.trip.attraction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guguns")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuGun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private int gugunCode;
    private String gugunName;
    private int sidoCode;
}