package com.trip.review.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.trip.config.S3Config;
import com.trip.review.dto.S3ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Util {
    private final S3Config s3Config;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3ResponseDTO imageUpload(MultipartFile file) throws IOException {

        // 파일 이름에서 확장자 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        // S3에 저장할 파일 이름(UUID 기반)
        String uuidFileName = UUID.randomUUID() + ext;

        // InputStream으로 파일 내용 읽기
        InputStream inputStream = file.getInputStream();


        // 파일 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize()); // 파일 크기
        metadata.setContentType(file.getContentType()); // 파일 타입

        // S3에 파일 업로드
        s3Config.amazonS3Client().putObject(
                new PutObjectRequest(bucket, uuidFileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead) // 공개 접근 권한 설정
        );


        // S3에 저장된 이미지의 Url 주소 가져오기, getUrl("버킷명", "서버에 저장한 파일명")
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        return new S3ResponseDTO(s3Url, uuidFileName);
    }


    // S3에서 이미지 삭제
    public void deleteImage(String fileName) {
        try {
            // 삭제 시에는 S3에 저장된 객체의 키(Key)를 넣기. url이 아닌.
            s3Config.amazonS3Client().deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.");
        }
    }

}
