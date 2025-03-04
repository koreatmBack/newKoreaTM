package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.jobsite.EmailMessage;
import com.example.smsSpringTest.model.S3Upload;
import com.example.smsSpringTest.model.UrlShorten;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.MapResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.model.response.UrlResponse;
import com.example.smsSpringTest.service.CommonService;
import com.example.smsSpringTest.service.EmailService;
import com.example.smsSpringTest.service.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 공통 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/common", "/v1/common"})
@Slf4j
public class CommonController {

    private final CommonService commonService;

    private final MapService mapService;

    private final EmailService emailService;

    private final ObjectMapper objectMapper;

    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver.
    @PostMapping("/upload/{folder}")
    public S3UploadResponse S3Upload(@PathVariable("folder") String folder, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        return commonService.S3Upload(multipartFile, folder);
    }

    @DeleteMapping("/delete/file")
    public ApiResponse deleteS3File(@RequestBody S3Upload s3URL) throws Exception  {
        return commonService.S3DeleteFile(s3URL);
    }

    @GetMapping("/ip")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = commonService.getClientIp(request);
        return "Client IP: " + clientIp;
    }

    // 주소로 위도 경도 찾기
    @GetMapping("/get/coordinates")
    public MapResponse getCoordinates(@RequestParam String address) {
        return mapService.getCoordinates(address);
    }

    // 이메일 전송 , 이메일 인증시 사용
    @PostMapping("/send/email")
    public ApiResponse sendEmail(@RequestBody EmailMessage emailMessage) throws MessagingException {
        return emailService.sendEmail(emailMessage);
    }

    // 카카오 API로 지하철역 찾고 역과 고객사의 거리 구하기
    @GetMapping("/find/subway")
    public MapResponse findSubways(@RequestParam double y, @RequestParam double x) throws Exception{
        return mapService.findNearbySubwayStations(y, x);
    }

    // url 단축하기
    @PostMapping("/change/url/short")
    public UrlResponse generateShortenUrl(@RequestBody UrlShorten url) throws Exception {
        return commonService.generateShortenUrl(url);
    }

    // 단축 URL을 통해 리다이렉트 처리 ( 다시 원래 url 리턴)
    @PostMapping("/change/url/original")
    public UrlResponse redirect(@RequestBody UrlShorten url) throws Exception {

        return commonService.getOriginalUrlByShortUrl(url);
    }


}

//class DistanceCalculator {
//
//    // 평균 도보 속도 (단위: m/s)
//    private static final double WALKING_SPEED = 1.4;
//
//    // 좌표 간 거리 계산 (하버사인 공식 사용)
//    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        final int EARTH_RADIUS = 6371; // 지구 반지름 (단위: km)
//
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = EARTH_RADIUS * c * 1000; // 거리(m)로 변환
//
//        return distance;
//    }
//
//    // 도보 예상 시간 계산
//    public static String getWalkingTime(double distance) {
//        int walkingMinutes = (int) Math.ceil(distance / (WALKING_SPEED * 60)); // 분 단위
//        return "도보 " + walkingMinutes + "분";
//    }
//}
