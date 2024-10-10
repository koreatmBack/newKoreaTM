package com.example.smsSpringTest.service;

import com.example.smsSpringTest.config.S3Uploader;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 공통 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommonService {

    private final S3Uploader s3Uploader;
    private final CommonMapper commonMapper;

    // S3에 이미지 파일 업로드 ( db에 저장은 X) --> 파일 1개 ver.
    public S3UploadResponse S3Upload(MultipartFile multipartFile) throws Exception {

        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        try {
            String imageUrl = s3Uploader.upload(multipartFile, "images");
            log.info("multipartFile = " + multipartFile);
            log.info("imageUrl = " + imageUrl);
//            int S3UploadFile = s3UploadMapper.S3UploadFile(imageUrl);

            // url 상대경로로 변환
            String formatImageUrl = formatImageUrl(imageUrl);
            log.info("formatImageUrl = " + formatImageUrl);

            s3UploadResponse.setUrl(formatImageUrl);
            s3UploadResponse.setCode("C001");
            s3UploadResponse.setMessage("S3에 업로드 성공");
        } catch (Exception e){
            s3UploadResponse.setCode("E001");
            s3UploadResponse.setMessage("업로드 실패");
            log.info(e.getMessage());
        }
        return s3UploadResponse;
    }

    // 파일 url 상대경로
    public static String formatImageUrl(String url) {
        // 'com' 다음 인덱스 찾기
        int index = url.indexOf(".com");
        if (index != -1) {
            // 'com' 다음 인덱스 + 4 ('.com' 길이)부터 반환
            return url.substring(index + 4);
        }
        return ""; // 유효한 URL이 아니면 빈 문자열 반환
    }

}
