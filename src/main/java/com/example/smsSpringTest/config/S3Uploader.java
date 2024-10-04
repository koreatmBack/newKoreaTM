package com.example.smsSpringTest.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : S3 파일 업로더
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드 파일 변환
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        log.info("uploadFile = " + uploadFile);
        return upload(uploadFile, dirName);
    }

    // 업로드 파일 정보 조회
    private String upload(File uploadFile, String dirName) {
        String ext = getFileNameWithoutExtension(uploadFile.getName());

        log.info("ext = " + ext);
        String key = dirName +"/" + UUID.randomUUID().toString() + "." + ext;

        log.info("uploadFile key = " + key);
        String uploadImageUrl = putS3(uploadFile, key);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // s3에 파일 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // s3에 있는 파일 삭제
    public void deleteFile(String filePath) throws Exception {
        log.info("filePath ?? " + filePath);
        String key = filePath;
        // 만약에 https://  포함한 긴 url 일때. -> fileList에서 삭제 할 때 images/... 로 나오기 때문에.
        if(filePath.contains("https://")){
            // 삭제할 때는 url에서 s3의 버킷 내 경로만 추출해야함. 전체 url x
            String splitStr = ".com/";
            key = filePath.substring(filePath.lastIndexOf(splitStr) + splitStr.length());
        } else if(filePath.startsWith("/")){
            key = filePath.substring(1);
        }

        log.info("key = " + key);

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
            log.info("S3Uploader -> deleteFile key = " + key);

        } catch (AmazonServiceException e) {
            log.info("error = " + e.getErrorMessage());
        }
    }

    // 로컬에 저장된 파일 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("로컬에 저장된 파일이 삭제되었습니다.");
        } else {
            log.info("로컬에 저장된 파일이 삭제되지 못했습니다.");
        }
    }


    // 로컬에 파일 업로드 및 변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    // 파일 확장자 조회 ( jpg, gif, png 등 )
    private static String getFileNameWithoutExtension(String fileName) {

        int lastDotIndex = fileName.lastIndexOf(".");
        log.info(String.valueOf(lastDotIndex));
        return lastDotIndex != -1 ? fileName.substring(lastDotIndex + 1) : "";
    }


    // 파일리스트
    public List<String> listFiles(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(prefix); // 지정된 폴더 내의 파일들만 조회
        ListObjectsV2Result result = amazonS3Client.listObjectsV2(req);
        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}
