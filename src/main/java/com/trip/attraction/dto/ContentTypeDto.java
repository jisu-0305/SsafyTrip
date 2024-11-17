package com.trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentTypeDto {
    private int contentTypeId;   // content_type_id
    private String contentTypeName; // content_type_name
}