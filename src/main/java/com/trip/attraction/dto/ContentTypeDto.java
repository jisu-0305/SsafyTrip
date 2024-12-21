package com.trip.attraction.dto;

import com.trip.attraction.entity.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentTypeDto {
    private int contentTypeId;
    private String contentTypeName;

    public static ContentTypeDto fromEntity(ContentType contentType) {
        return new ContentTypeDto(contentType.getContentTypeId(), contentType.getContentTypeName());
    }
}