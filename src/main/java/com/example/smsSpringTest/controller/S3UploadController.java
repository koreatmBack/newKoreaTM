package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.config.S3Uploader;
import com.example.smsSpringTest.model.S3Upload;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : S3업로드 controller
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/S3Upload")
@Slf4j
public class S3UploadController {

    private final S3Uploader s3Uploader;
    private final S3UploadService s3UploadService;

    @PostMapping("/uploadFile")
    public S3UploadResponse S3Upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        s3UploadResponse = s3UploadService.S3Upload(multipartFile);

        return s3UploadResponse;
    }

    @DeleteMapping("/deleteFile")
    public ApiResponse deleteS3File(@RequestBody S3Upload s3URL) throws Exception  {

        ApiResponse apiResponse = new ApiResponse();

        apiResponse = s3UploadService.S3DeleteFile(s3URL);

        return apiResponse;
    }

    @GetMapping("/fileList")
    public S3UploadResponse S3FileList() throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

//        // db 상관없이 현재 저장된 모든 url 갖고 오려면
//        String folderPrefix = "images/"; // S3 버킷의 폴더 경로
//        List<String> fileKeys = s3Uploader.listFiles(folderPrefix);
//        //------
//        s3UploadResponse.setAllFileList(fileKeys);

        //db 연동된 url 파일들만 갖고 오기
        s3UploadResponse = s3UploadService.S3FileList();

        return s3UploadResponse;
    }


}
