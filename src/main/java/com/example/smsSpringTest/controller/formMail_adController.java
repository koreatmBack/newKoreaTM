package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.ad.AdImageRequest;
import com.example.smsSpringTest.model.ad.AdRequest;
import com.example.smsSpringTest.model.ad.fmAd;
import com.example.smsSpringTest.model.formMail_file;
import com.example.smsSpringTest.model.response.AdResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.formMail_adService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 controller
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/formMail_ad")
@Slf4j
public class formMail_adController {

    private final formMail_adService formMailAdService;

    // 광고 등록
    @PostMapping("/addAd")
    public ApiResponse addAd(@RequestBody fmAd ad) throws Exception {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailAdService.addAd(ad);
        return apiResponse;
    }

//    // 광고 조회
//    @PostMapping("/fmAdList")
//    public AdResponse fmAdList(@RequestBody Paging paging) throws Exception {
//
//        AdResponse adResponse = new AdResponse();
//        adResponse = formMailAdService.fmAdList(paging);
//        return adResponse;
//    }

    // 고객사(cid) 일치하는 광고 조회
    @PostMapping("/fmAdList")
    public AdResponse fmAdList(@RequestBody AdRequest adRequest) throws Exception {

        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.fmAdList(adRequest);
        return adResponse;
    }

    // 광고 업데이트
    @PutMapping("/updateAd")
    public ApiResponse updateAd(@RequestBody fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailAdService.updateAd(ad);
        return apiResponse;
    }

    // 광고 삭제
    @DeleteMapping("/deleteAd")
    public ApiResponse deleteAd(@RequestBody fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailAdService.deleteAd(ad);
        return apiResponse;
    }

//    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 여러개 ver.
//    @PostMapping("/S3Upload")
//    public S3UploadResponse S3Upload(@RequestParam("file") MultipartFile[] multipartFiles) throws Exception {
//        S3UploadResponse s3UploadResponse = new S3UploadResponse();
//
//        s3UploadResponse = formMailAdService.S3Upload(multipartFiles);
//
//        return s3UploadResponse;
//    }

    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver.
    @PostMapping("/S3Upload")
    public S3UploadResponse S3Upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        s3UploadResponse = formMailAdService.S3Upload(multipartFile);

        return s3UploadResponse;
    }

//    // DB에 광고 이미지 저장
//    @PostMapping("/addAdImg")
//    public ApiResponse addAdImg(@RequestBody fmAdImage adImage) throws Exception {
//        log.info(adImage.toString());
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse = formMailAdService.addAdImg(adImage);
//        return apiResponse;
//    }
//
//    // S3 및 DB에 해당 이미지 삭제
//    @DeleteMapping("/deleteAdImg")
//    public ApiResponse deleteAdImg(@RequestBody fmAdImage adImage) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//        log.info(" 컨틀롤러 "+ adImage.toString());
//        apiResponse = formMailAdService.deleteAdImg(adImage);
//        return apiResponse;
//    }

    // 광고 고유 id (uid) 일치하는 광고 이미지 조회
    @PostMapping("/fmAdImageList")
    public AdResponse fmAdImageList(@RequestBody AdImageRequest adImage) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.fmAdImageList(adImage);
        return adResponse;
    }

    // formmail_file db에서 url 일치하는 데이터 삭제하기
    @DeleteMapping("/deleteFile")
    public ApiResponse deleteFile(@RequestBody formMail_file file) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailAdService.deleteFile(file);
        return apiResponse;
    }

    // 날짜 입력 -> total day 계산 API
    @PostMapping("/totalDay")
    public int totalDay(@RequestBody fmAd ad) throws Exception {
        int onlyTotalDay = formMailAdService.totalDay(ad);
        return onlyTotalDay;
    }
}
