package com.example.smsSpringTest.service;

import com.example.smsSpringTest.config.S3Uploader;
import com.example.smsSpringTest.mapper.S3UploadMapper;
import com.example.smsSpringTest.model.S3Upload;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : S3 업로더 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {

    private final S3Uploader s3Uploader;
    private final S3UploadMapper s3UploadMapper;

    // S3 파일 업로드
    public S3UploadResponse S3Upload(MultipartFile multipartFile) throws Exception {

        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        try {
            String imageUrl = s3Uploader.upload(multipartFile, "images");
            log.info("multipartFile = " + multipartFile);
            log.info("imageUrl = " + imageUrl);
//            int S3UploadFile = s3UploadMapper.S3UploadFile(imageUrl);
            String formatImageUrl = formatImageUrl(imageUrl);
            log.info("formatImageUrl = " + formatImageUrl);
            s3UploadResponse.setUrl(formatImageUrl);
            s3UploadResponse.setCode("C000");
            s3UploadResponse.setMessage("업로드 성공");
        } catch (Exception e) {
            s3UploadResponse.setCode("E001");
            s3UploadResponse.setMessage("업로드 실패");
            log.info(e.getMessage());
        }

        return s3UploadResponse;
    }

    // S3 파일 삭제
    public ApiResponse S3DeleteFile(S3Upload s3URL) throws Exception{
        ApiResponse apiResponse = new ApiResponse();

        try {
            String delUrl = s3URL.getUrl();
//            int S3DeleteFile = s3UploadMapper.S3DeleteFile(delUrl);
//            if(S3DeleteFile == 1) {
             s3Uploader.deleteFile(delUrl);
             log.info("service -> deleteFileKey = "+ delUrl);
             apiResponse.setCode("C000");
             apiResponse.setMessage("S3 파일 삭제 완료");
//            } else {
//                apiResponse.setCode("C004");
//                apiResponse.setMessage("S3 파일 삭제 실패");
//            }
        } catch(Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }

        return apiResponse;
    }

    // S3 파일 조회
    public S3UploadResponse S3FileList() throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        try {
            s3UploadResponse.setS3UploadList(s3UploadMapper.s3UploadList());
            s3UploadResponse.setCode("C000");
            s3UploadResponse.setMessage("파일 조회 성공");
        } catch(Exception e) {
            s3UploadResponse.setCode("E001");
            s3UploadResponse.setMessage("파일 조회 실패");
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
