package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 공통 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/formMail_common")
@Slf4j
public class CommonController {

    private final CommonService commonService;

    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver.
    @PostMapping("/S3Upload")
    public S3UploadResponse S3Upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        s3UploadResponse = commonService.S3Upload(multipartFile);

        return s3UploadResponse;
    }


}
