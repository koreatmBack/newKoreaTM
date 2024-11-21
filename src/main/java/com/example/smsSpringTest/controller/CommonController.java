package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.EmailMessage;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.NaverMapResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.CommonService;
import com.example.smsSpringTest.service.EmailService;
import com.example.smsSpringTest.service.NaverMapService;
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

    private final NaverMapService naverMapService;

    private final EmailService emailService;

    private final ObjectMapper objectMapper;

    // S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver.
    @PostMapping("/upload/{folder}")
    public S3UploadResponse S3Upload(@PathVariable("folder") String folder, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        s3UploadResponse = commonService.S3Upload(multipartFile, folder);

        return s3UploadResponse;
    }

    @GetMapping("/ip")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = commonService.getClientIp(request);
        return "Client IP: " + clientIp;
    }

    // 주소로 위도 경도 찾기
    @GetMapping("/get/coordinates")
    public NaverMapResponse getCoordinates(@RequestParam String address) {
        return naverMapService.getCoordinates(address);
    }

    // 이메일 전송 , 이메일 인증시 사용
    @PostMapping("/send/email")
    public ApiResponse sendEmail(@RequestBody EmailMessage emailMessage) throws MessagingException {
        return emailService.sendEmail(emailMessage);
    }


//    @GetMapping("/get/coordinates")
//    public Mono<String> getCoordinates(@RequestParam String address) {
//        return naverMapService.getCoordinates(address);
//    }

//    @GetMapping("/nearby-stations")
//    public Mono<String> getNearbyStations(@RequestParam double latitude, @RequestParam double longitude) {
//       log.info("Latitude: " + latitude + ", Longitude: " + longitude);
//        return naverMapService.searchNearbyStations(latitude, longitude)
//                .map(response -> {
//                    try {
//                        // JSON 응답을 파싱하여 역 이름, 거리, 도보 시간 계산
//                        JsonNode jsonResponse = objectMapper.readTree(response);
////                        JsonNode stationsArray = jsonResponse.get("items");
//                        JsonNode stationsArray = jsonResponse.path("items");
//                        List<JsonNode> resultArray = new ArrayList<>();
//                        log.info("jsonResponse = " + jsonResponse);
//                        log.info("stationarray = " + stationsArray);
//                        // 역 정보 파싱 및 처리
//                        for (JsonNode station : stationsArray) {
//                            String stationName = station.get("title").asText();
//                            double stationLat = station.get("mapy").asDouble();
//                            double stationLon = station.get("mapx").asDouble();
//
//                            // 거리 및 도보 시간 계산
//                            double distance = DistanceCalculator.calculateDistance(latitude, longitude, stationLat, stationLon);
//                            String walkingTime = DistanceCalculator.getWalkingTime(distance);
//
//                            // 결과 저장
//                            JsonNode stationInfo = objectMapper.createObjectNode()
//                                    .put("name", stationName)
//                                    .put("distance", String.format("%.0f m", distance))
//                                    .put("walkingTime", walkingTime);
//
//                            resultArray.add(stationInfo);
//                        }
//
//                        // 최종 결과를 JSON 문자열로 변환
//                        JsonNode result = objectMapper.createObjectNode().set("stations", objectMapper.valueToTree(resultArray));
//
//                        return result.toString();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return "{}"; // 예외가 발생하면 빈 JSON 객체 반환
//                    }
//                });
//    }

    @GetMapping("/search")
    public String searchStations(@RequestParam double latitude, @RequestParam double longitude) {
        // 서비스에서 API 호출 후 결과 반환
        log.info("controller : latitud = " + latitude + " longitude = " + longitude);
//        return naverMapService.searchNearbyStations(latitude, longitude);
        return naverMapService.searchNearbyStations(latitude, longitude);
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
