package com.sparta.post_board.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String uploadImage(Long postId, MultipartFile image) {
        try {
            // 원본 파일명 및 확장자 추출 -> 새로운 파일명, URL 생성
            String filenameExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            String filename = "feed-image/" + postId + "." + filenameExtension;
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;

            // 파일 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filename, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImage(String filename) {
        amazonS3Client.deleteObject(bucket, "feed-image/" + filename);
    }
}
