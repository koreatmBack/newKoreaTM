package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.Paging;
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
@RequestMapping({"/api/v1/formMail_ad", "/v1/formMail_ad"})
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
    public Integer totalDay(@RequestBody fmAd ad) throws Exception {
        Integer onlyTotalDay = formMailAdService.totalDay(ad);
        return onlyTotalDay;
    }

    // 폼메일용 광고 목록 전체 조회
    @PostMapping("/allAdList")
    public AdResponse allAdList(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.allAdList(paging);
        return adResponse;
    }

    // 폼메일용 adNum 일치하는 광고 전체 조회
    @PostMapping("/searchAdNumList")
    public AdResponse searchAdNumList(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.searchAdNumList(ad);
        return adResponse;
    }

    // 폼메일용 aid 일치하는 광고 상세 조회
    @PostMapping("/findOneAd")
    public AdResponse findOneAd(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.findOneAd(ad);
        return adResponse;
    }

    // 폼메일용 title이 포함된 광고 조회
    @PostMapping("/searchTitleAd")
    public AdResponse searchTitleAd(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.searchTitleAd(ad);
        return adResponse;
    }

    // ---------------------------------------------------------


    // 잡사이트용 광고 목록 전체 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/allJobsiteList")
    public AdResponse allJobsiteList(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.allJobsiteList(paging);
        return adResponse;
    }

    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/searchTitleList")
    public AdResponse searchTitleList(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.searchTitleJobsite(ad);
        return adResponse;
    }

    // 잡사이트용 aid 일치하는 광고 상세 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/findOneJobsite")
    public AdResponse findOneJobsite(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.findOneJobsite(ad);
        return adResponse;
    }

    // 잡사이트용 등록일순으로 광고 조회
    @PostMapping("/orderByCreated")
    public AdResponse orderByCreated(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.orderByCreated(paging);
        return adResponse;
    }

    // 잡사이트용 근무일수 적은순으로 광고 조회
    @PostMapping("/orderByWorkDay")
    public AdResponse orderByWorkDay(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.orderByWorkDay(paging);
        return adResponse;
    }

    // 잡사이트용 급여 높은 순으로 광고 조회
    @PostMapping("/orderByMaxPay")
    public AdResponse orderByMaxPay(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.orderByMaxPay(paging);
        return adResponse;
    }

    // 잡사이트용 근무시간 짧은 순으로 광고 조회
    @PostMapping("/orderByWorkTime")
    public AdResponse orderByWorkTime(@RequestBody Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.orderByWorkTime(paging);
        return adResponse;
    }

    // -------------------------

    // aid가 일치하는 고객사 정보 반환 + 정보로 찾은 cid -> user 정보 까지 반환
    @PostMapping("/findCompanyAndUser")
    public AdResponse findCompanyAndUser(@RequestBody fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        adResponse = formMailAdService.findCompanyAndUser(ad);
        return adResponse;
    }

    // get 테스트용 -> 삭제 예정
    @GetMapping("/jobSiteListTest")
    public AdResponse jobSiteListTest() throws Exception {
        return formMailAdService.jobSiteListTest();
    }
}
