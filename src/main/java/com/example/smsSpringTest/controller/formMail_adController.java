package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.Regions;
import com.example.smsSpringTest.model.ad.AdImageRequest;
import com.example.smsSpringTest.model.ad.AdNearInfo;
import com.example.smsSpringTest.model.ad.AdRequest;
import com.example.smsSpringTest.model.ad.fmAd;
import com.example.smsSpringTest.model.formMail_file;
import com.example.smsSpringTest.model.response.AdCountResponse;
import com.example.smsSpringTest.model.response.AdResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.formMail_adService;
import io.swagger.v3.oas.annotations.Operation;
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

//    // 고객사(cid) 일치하는 광고 조회
//    @PostMapping("/fmAdList")
//    @Operation(summary = "고객사 일치하는 광고 조회", description="필수 값 : cid")
//    public AdResponse fmAdList(@RequestBody AdRequest adRequest) throws Exception {
//        return formMailAdService.fmAdList(adRequest);
//    }

    // 광고 업데이트
    @PutMapping("/updateAd")
    public ApiResponse updateAd(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.updateAd(ad);
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

    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver. // 12-27 사용 x
    @PostMapping("/S3Upload")
    @Operation(summary = "AWS S3에 이미지 업로드", description="db에는 저장 x , file , MultipartFile")
    public S3UploadResponse S3Upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        return formMailAdService.S3Upload(multipartFile);
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
        return formMailAdService.deleteFile(file);
    }

    // 날짜 입력 -> total day 계산 API
    @PostMapping("/totalDay")
    @Operation(summary = "광고 일 수 계산하기 (평일만)", description="필수 값 : startDate , endDate")
    public Integer totalDay(@RequestBody fmAd ad) throws Exception {
        Integer onlyTotalDay = formMailAdService.totalDay(ad);
        return onlyTotalDay;
    }

    // 폼메일용 광고 목록 전체 조회
    @PostMapping("/allAdList")
    @Operation(summary = "광고 목록 전체 조회", description="페이징 처리, 필수 값 : page, size")
    public AdResponse allAdList(@RequestBody Paging paging) throws Exception {

        return formMailAdService.allAdList(paging);
    }

    // 폼메일용 adNum 일치하는 광고 전체 조회
    @PostMapping("/searchAdNumList")
    @Operation(summary = "광고번호 일치하는 광고 전체 조회", description="필수 값 : adNum")
    public AdResponse searchAdNumList(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.searchAdNumList(ad);
    }

    // 폼메일용 adNum 일치하는 광고 전체 조회
    @PostMapping("/search/one/num")
    public AdResponse searchOneAd(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.searchOneAd(ad);
    }

    // 폼메일용 aid 일치하는 광고 상세 조회
    @PostMapping("/findOneAd")
    @Operation(summary = "광고 고유id 일치하는 광고 조회", description="필수 값 : aid")
    public AdResponse findOneAd(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.findOneAd(ad);
    }

    // 폼메일용 title이 포함된 광고 조회
    @PostMapping("/searchTitleAd")
    @Operation(summary = "검색한 내용이 제목에 포함된 광고 조회", description="필수 값 : title")
    public AdResponse searchTitleAd(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.searchTitleAd(ad);
    }

//    // 폼메일용 hashtag 일치하는 광고 조회
//    @PostMapping("/searchHashtagAd")
//    @Operation(summary = "해시태그 일치하는 광고 조회", description="필수 값 : hashtag")
//    public AdResponse searchHashtagAd(@RequestBody fmAd ad) throws Exception {
//        return formMailAdService.searchHashtagAd(ad);
//    }

    // 폼메일용 sido (필수) , sigungu (필수아님) 일치하는 광고 찾기
    @Operation(summary = "sido, sigungu 일치하는 광고 조회", description="sido는 필수, sigungu는 필수 아님")
    @PostMapping("/searchAddressAd")
    public AdResponse searchAddressAd(@RequestBody fmAd ad) throws Exception{
        return formMailAdService.searchAddressAd(ad);
    }

    // 관리자페이지 -> 공고관리 조건들
    @PostMapping("/statusList")
    public AdResponse statusList(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.statusList(ad);
    }

    // 시도 -> 시,군,구 목록 조회
    @PostMapping("/sigunguList")
    public AdResponse sigunguList(@RequestBody Regions re) throws Exception {
        return formMailAdService.sigunguList(re);
    }

    // 시,군,구 -> 동,읍,면 목록 조회
    @PostMapping("/dongEubMyunList")
    public AdResponse dongEubMyunList(@RequestBody Regions re) throws Exception {
        return formMailAdService.dongEubMyunList(re);
    }

    // 마감 버튼 클릭시 마감 처리할 수 있는 API (오직 마감 기능만)
    @PutMapping("/update/close")
    public ApiResponse updateAdClose(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.updateAdClose(ad);
    }

    // 유료상품 등급 수정 API
    @PutMapping("/update/grade")
    public ApiResponse updateGrade(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.updateGrade(ad);
    }

    // 한번에 전체, 진행중, 대기중, 종료 공고 개수 반환하는 API
    @GetMapping("/count/ads")
    public AdCountResponse countAds() throws Exception {
        return formMailAdService.countAds();
    }

    // ---------------------------------------------------------


    // 잡사이트용 광고 목록 전체 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/allJobsiteList")
    @Operation(summary = "광고 목록 전체 조회 (종료기간 끝난 것 조회 X)", description="페이징 처리, 필수 값 : page, size")
    public AdResponse allJobsiteList(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.allJobsiteList(ad);
    }

    // 잡사이트용 grade에 따른 공고 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/searchGradeList")
    @Operation(summary = "grade(플래티넘, 골드, 라이트) 유료 광고 조회", description="필수 값 : grade")
    public AdResponse searchGradeList(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.searchGradeJobsite(ad);
    }

    @PostMapping("/searchFocusList")
    public AdResponse searchFocusList(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.searchFocusJobsite(ad);
    }

    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/searchTitleList")
    @Operation(summary = "검색한 단어가 제목에 포함된 광고 조회", description="필수 값 : title")
    public AdResponse searchTitleList(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.searchTitleJobsite(ad);
    }

    // 잡사이트용 aid 일치하는 광고 상세 조회 ( 종료기간 끝난것 조회 x )
    @PostMapping("/findOneJobsite")
    @Operation(summary = "광고 고유id 일치하는 광고 상세 조회(종료기간 끝난 것 조회 X)", description="필수 값 : aid")
    public AdResponse findOneJobsite(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.findOneJobsite(ad);
    }

    // 잡사이트용 aid 일치하는 광고 상세 조회 -> 지원 방법만 뽑기
    // 이거 나중에 login한 유저만 접근 가능하게 막기 위해서임
    @PostMapping("/find/applyMethod")
    public AdResponse findApplyMethod(@RequestBody fmAd ad) throws Exception {
        return formMailAdService.findApplyMethod(ad);
    }

    // 잡사이트용 등록일순으로 광고 조회
    @PostMapping("/orderByCreated")
    @Operation(summary = "등록일 내림차순 광고 전체 조회", description="페이징 처리, 필수 값 : page, size")
    public AdResponse orderByCreated(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.orderByCreated(ad);
    }

    // 잡사이트용 근무일수 적은순으로 광고 조회
    @PostMapping("/orderByWorkDay")
    @Operation(summary = "근무일수 적은 순으로 광고 조회", description="페이징 처리, 필수 값 : page, size")
    public AdResponse orderByWorkDay(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.orderByWorkDay(ad);
    }

    // 잡사이트용 급여 높은 순으로 광고 조회
    @PostMapping("/orderByMaxPay")
    @Operation(summary = "급여 높은 순으로 광고 조회", description="페이징 처리, 필수 값 : page, size")
    public AdResponse orderByMaxPay(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.orderByMaxPay(ad);
    }

//    // 잡사이트용 근무시간 짧은 순으로 광고 조회
//    @PostMapping("/orderByWorkTime")
//    @Operation(summary = "근무시간 짧은 순으로 광고 조회", description="페이징 처리, 필수 값 : page, size")
//    public AdResponse orderByWorkTime(@RequestBody Paging paging) throws Exception {
//        AdResponse adResponse = new AdResponse();
//        adResponse = formMailAdService.orderByWorkTime(paging);
//        return adResponse;
//    }

    // 11-29 ~

//    // 등록일, 정렬 조건 없이 시/도, 시/군/구 , 동/읍/면에 대해서만
//    @PostMapping("/selectByRegions")
//    public AdResponse selectByRegions(@RequestBody AdRequest ad) throws Exception {
//        return formMailAdService.selectByRegions(ad);
//    }

    // 등록일, 정렬 조건 없이 시/도, 시/군/구 , 동/읍/면에 대해서만
    // 정렬 조건 추가
    @PostMapping("/selectByRegions/sort")
    public AdResponse selectByRegionsSort(@RequestBody AdRequest ad) throws Exception {
        return formMailAdService.selectByRegionsSort(ad);
    }

//    // 잡사이트용 급구 공고 (마감기한 3일이하로 남은 애들)
//    @PostMapping("/hurriedAdList")
//    public AdResponse hurriedAdList(@RequestBody AdRequest ad) throws Exception {
//        return formMailAdService.hurriedAdList(ad);
//    }

    // aid 일치하는 주변 역 정보 추출
     @PostMapping("/find/nearInfo")
     public AdResponse nearInfoList(@RequestBody AdNearInfo near) throws Exception {
         return formMailAdService.nearInfoList(near);
     }

    // -------------------------

//    // aid가 일치하는 고객사 정보 반환 + 정보로 찾은 cid -> user 정보 까지 반환
//    @PostMapping("/findCompanyAndUser")
//    @Operation(summary = "광고 고유id 일치하는 고객사 조회 -> 찾은 cid로 user 정보까지 조회", description="필수 값 : aid")
//    public AdResponse findCompanyAndUser(@RequestBody fmAd ad) throws Exception {
//        return formMailAdService.findCompanyAndUser(ad);
//    }

    // get 테스트용 -> 삭제 예정
    @GetMapping("/jobSiteListTest")
    @Operation(summary = "테스트용", description="")
    public AdResponse jobSiteListTest() throws Exception {
        log.info("jobSiteListTest 컨트롤러 들어옴");
        return formMailAdService.jobSiteListTest();
    }

//    // 주변 역 담는 API, 비동기로 실행시킬 것 선택, 선택해제시
//    @PostMapping("/choose/type")
//    public ApiResponse chooseType(@RequestBody AdNearInfo near) throws Exception {
//        return formMailAdService.chooseType(near);
//    }
}
