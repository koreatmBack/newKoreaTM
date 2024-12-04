package com.example.smsSpringTest.service;

import com.example.smsSpringTest.model.MapVO;
import com.example.smsSpringTest.model.response.MapResponse;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class MapService {

    private final WebClient mapWebClient;
    private final WebClient searchWebClient;

    @Value("${naver.map.client-id}")
    private String mapClientId;
    @Value("${naver.map.client-secret}")
    private String mapClientSecret;

    // 네이버 지도 기본 틀
    @Autowired
    public MapService(WebClient.Builder webClientBuilder,
                      @Value("${naver.map.client-id}") String mapClientId,
                      @Value("${naver.map.client-secret}") String mapClientSecret,
                      @Value("${naver.search.client-id}") String searchClientId,
                      @Value("${naver.search.client-secret}") String searchClientSecret
                           ) {
//        log.info("Client ID: {}", mapClientId);
//        log.info("Client Secret: {}", mapClientSecret);
//        log.info("Search Client ID: {}", searchClientId);
//        log.info("Search Client Secret: {}", searchClientSecret);

        this.mapWebClient = webClientBuilder.baseUrl("https://naveropenapi.apigw.ntruss.com")  // 변경된 URL로 수정
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", mapClientId)  // 인증 헤더 수정
                .defaultHeader("X-NCP-APIGW-API-KEY", mapClientSecret)  // 인증 헤더 수정
                .build();

        // 검색 API용 WebClient 설정
        this.searchWebClient = webClientBuilder
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("X-Naver-Client-Id", searchClientId)
                .defaultHeader("X-Naver-Client-Secret", searchClientSecret)
                .build();
    }



    // 주소로 받아올때. ex : 서울 광진구 자양로 95
//    public Mono<String> getCoordinates(String address) {
//        return this.mapWebClient.get()
//                .uri("/map-geocode/v2/geocode?query={address}", address)
//                .retrieve()
//                .bodyToMono(String.class);
//    }

//    public Mono<String> searchNearbyStations(double latitude, double longitude) {
//        String query = "지하철";
//        String url = String.format("/v1/search/local.json?query=%s&coordinate=%f,%f&radius=1000", "지하철역", latitude, longitude);
//        log.info("Request URL: " + url);
//        return this.searchWebClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/v1/search/local.json")
////                .uri(uriBuilder -> uriBuilder.path("/map-geocode/v2/geocode")
//                        .queryParam("query", query)
//                        .queryParam("coordinate", latitude + "," + longitude)
//                        .queryParam("radius", 5000) // 반경 5km 내에서 검색
//                        .queryParam("display", 10)
//                        .build())
//                .retrieve()
//                .bodyToMono(String.class);
//    }


    // 위도 경도 찾는 메서드
    public MapResponse getCoordinates(String address) {
        MapResponse mapResponse = new MapResponse();
        try {
            log.info("service : address = " + address);
            String query = URLEncoder.encode(address, "UTF-8");
            String urlString = String.format("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=%s", query);

            log.info("URL = " + urlString);
            // URL 객체 생성
            URL url = new URL(urlString);
            // HttpURLConnection 객체 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP 요청 설정
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", mapClientId);
            connection.setRequestProperty("X-NCP-APIGW-API-KEY", mapClientSecret);

            // 요청이 성공했는지 확인
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // 응답 본문을 읽어서 반환하여 디버깅에 도움을 줄 수 있도록
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                throw new IOException("Failed to get response from API: " + statusCode + " - " + errorResponse.toString());
            }

            // 정상적인 응답을 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // 연결 종료
            reader.close();
            connection.disconnect();

            log.info(response.toString());

            JsonObject keys = (JsonObject) JsonParser.parseString(response.toString());

            log.info("keys addresses = " + keys.get("addresses").toString());
            JsonArray addresses = keys.getAsJsonArray("addresses");

            if (addresses.size() > 0) {
                JsonObject firstAddress = addresses.get(0).getAsJsonObject();
                String y = firstAddress.get("x").getAsString();
                String x = firstAddress.get("y").getAsString();

                log.info("x = " + x);
                log.info("y = " + y);
                mapResponse.setX(x);
                mapResponse.setY(y);
                mapResponse.setCode("C000");
                mapResponse.setMessage("위도 경도 반환 성공");
            } else {
                mapResponse.setCode("C003");
                mapResponse.setMessage("위도 경도 반환 실패");
            }

        } catch (IOException e) {

            mapResponse.setCode("E001");
            mapResponse.setMessage("Error!!!");
        }
        return mapResponse;
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    private final RestTemplate restTemplate = new RestTemplate();

    // 주변 지하철역 검색 및 대학교 검색
    public MapResponse findNearbySubwayStations(double y, double x) throws Exception {
        MapResponse mapResponse = new MapResponse();
    try{

        String schoolURL = String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SC4&x=%f&y=%f&radius=3000",
                y, x);
        String url = String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SW8&x=%f&y=%f&radius=1500"
                , y, x);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoClientId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        ResponseEntity<String> schoolResponse = restTemplate.exchange(schoolURL, HttpMethod.GET, entity, String.class);

        log.info(response.toString());

        // JSON 문자열로 가져오기
        String jsonResponse = response.getBody();
        String schoolJsonResponse = schoolResponse.getBody();
        System.out.println("Response JSON: " + jsonResponse);
        System.out.println("SCHOOL JSON: " + schoolJsonResponse);

        // Gson 객체 생성
        Gson gson = new Gson();

        // JSON 문자열을 JsonObject로 변환
        JsonObject responseJson = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject schoolResponseJson = gson.fromJson(schoolJsonResponse, JsonObject.class);

        // "documents" 배열 추출
        JsonArray documents = responseJson.getAsJsonArray("documents");
        JsonArray schoolDocuments = schoolResponseJson.getAsJsonArray("documents");

        // 대학교 저장할 리스트
        List<JsonObject> schoolDocumentsList = new ArrayList<>();

        // "category_name"에 "대학교"가 포함된 데이터만 필터링
        for (JsonElement element : schoolDocuments) {
            JsonObject document = element.getAsJsonObject();
            String categoryName = document.get("category_name").getAsString();
            if (categoryName.contains("대학교")) {
                schoolDocumentsList.add(document);
            }
        }
        System.out.println("SchoolDocumentsList:");
        String university = "";
        for (JsonObject doc : schoolDocumentsList) {
            university = doc.get("place_name").getAsString();
            System.out.println("Place Name: " + doc.get("place_name").getAsString());
            if(StringUtils.hasText(university)){
                break;
            }

        }
        List<MapVO> mapInfoList = new ArrayList<>();
        log.info(documents.toString());
        // 지하철역 결과 처리
        for (JsonElement element : documents) {
            JsonObject document = element.getAsJsonObject();
            // 필요한 데이터 추출
            String placeName = document.get("place_name").getAsString();
            String subwayY = String.valueOf(document.get("x").getAsDouble()); // 위도
            String subwayX = String.valueOf(document.get("y").getAsDouble());  // 경도
            int distance = Integer.parseInt(document.get("distance").getAsString()); // 거리 km
//            int durationTime = Integer.parseInt(document.get("distance").getAsString()); // 걸리는 시간
            int durationTime = distance; // 걸리는 시간

            durationTime /= 80;
            String newDurationTime = String.valueOf(durationTime);

            log.info(String.format("Place: %s, Longitude: %s, Latitude: %s, durationTime: %s ,Distance: %dm",
                    placeName, subwayX, subwayY, newDurationTime ,distance));

            double result = (double) distance / 1000;
            String KmDistance = String.valueOf(Math.round((result * 10)) / 10.0);
            mapInfoList.add(new MapVO(subwayX , subwayY, "", placeName, "걸어서 "+ newDurationTime+"분" , KmDistance+"km"));
        }
        mapResponse.setUniversity(university);
        mapResponse.setMapInfoList(mapInfoList);
        mapResponse.setCode("C000");
        mapResponse.setMessage("성공");
    } catch (Exception e) {
        mapResponse.setCode("E001");
        mapResponse.setMessage(" Error !!! ");
        log.info(e.getMessage());
    }
        return mapResponse;
    }

    // 도보 시간 계산
    public double calculateWalkingTime(double distanceMeters) {
        double walkingSpeedMetersPerSecond = 1.4; // 평균 도보 속도 (1.4 m/s)
        return distanceMeters / walkingSpeedMetersPerSecond / 60; // 분 단위
    }


}
