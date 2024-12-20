package com.trip.attraction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "contenttypes")
@Getter
public class ContentType {

    @Id
    private int contentTypeId;

    private String contentTypeName;
}