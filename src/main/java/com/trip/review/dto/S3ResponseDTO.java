package com.trip.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class S3ResponseDTO {
    private String s3Url;
    private String s3Key;

    public S3ResponseDTO(String s3Url, String s3Key) {
        this.s3Url = s3Url;
        this.s3Key = s3Key;
    }
}
