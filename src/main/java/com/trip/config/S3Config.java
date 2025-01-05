package com.trip.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration: 스프링 컨테이너에 빈(Bean)을 등록하기 위한 설정 클래스, 하나 이상의 @Bean 메서드를 포함하여 빈 정의
@Configuration
public class S3Config {

    //lombok.Value가 아닌, beans.factory.annotation.Value class 사용하기
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'에서 제공하는 class
    // AWS에 접근할 수 있는 객체, 해당 객체를 통해 S3에 파일을 저장하거나 삭제하는 등의 작업 가능.
    @Bean
    public AmazonS3Client amazonS3Client() {

        // accessKey, secretKey는 IAM 사용자가 AWS 리소스에 접근하기 위해 발급받은 자격증명.
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }

}



