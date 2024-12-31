package com.trip.review.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.trip.config.S3Config;
import com.trip.review.dto.S3ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Util {
    private final S3Config s3Config;

    private String localLocation = "C:\\Users\\ucs21\\"; //현재는 window 기준.

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3ResponseDTO imageUpload(MultipartRequest request) throws IOException {
        // 이미지 파일 추출
        MultipartFile file = request.getFile("upload"); //upload는 이미지 파일을 보낼 때 사용한 name 속성의 값


        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        // 해당 이름이 S3에 저장된 객체의 키(Key)
        String uuidFileName = UUID.randomUUID() + ext;


        // 로컬에 저장
        String localPath = localLocation + uuidFileName;
        File localFile = new File(localPath);
        file.transferTo(localFile);

        // S3에 이미지 저장하기, PutObjectRequest("버킷명", "파일명", "서버에 저장할 파일"), withCannedAcl는 접근 제어 정책 관련 메소드
        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));

        // 로컬에 저장한 파일 지우기
        localFile.delete();

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
