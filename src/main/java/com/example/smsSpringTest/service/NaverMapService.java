package com.example.smsSpringTest.service;

import com.example.smsSpringTest.model.response.NaverMapResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@Slf4j
@Transactional
public class NaverMapService {

    private final WebClient mapWebClient;
    private final WebClient searchWebClient;

    @Value("${naver.map.client-id}")
    private String mapClientId;
    @Value("${naver.map.client-secret}")
    private String mapClientSecret;

    // 네이버 지도 기본 틀
    @Autowired
    public NaverMapService(WebClient.Builder webClientBuilder,
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
    public NaverMapResponse getCoordinates(String address) {
        NaverMapResponse naverMapResponse = new NaverMapResponse();
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
                String x = firstAddress.get("x").getAsString();
                String y = firstAddress.get("y").getAsString();

                log.info("x = " + x);
                log.info("y = " + y);
                naverMapResponse.setX(x);
                naverMapResponse.setY(y);
                naverMapResponse.setCode("C000");
                naverMapResponse.setMessage("위도 경도 반환 성공");
            } else {
                naverMapResponse.setCode("C003");
                naverMapResponse.setMessage("위도 경도 반환 실패");
            }

        } catch (IOException e) {

            naverMapResponse.setCode("E001");
            naverMapResponse.setMessage("Error!!!");
        }
        return naverMapResponse;
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Value("${naver.search.client-id}")
    private String searchClientId;

    @Value("${naver.search.client-secret}")
    private String searchClientSecret;

    // 지하철역을 검색하는 메서드
    public String searchNearbyStations(double latitude, double longitude) {
        try {
            log.info("service : latitud = " + latitude + " longitude = " + longitude);
            // 쿼리 파라미터 URL 인코딩
            String query = URLEncoder.encode("지하철역", "UTF-8");
            // naver의 coordinate에서는 long이 먼저,
            String urlString = String.format("https://openapi.naver.com/v1/search/local.json?query=%s&coordinate=%f,%f&radius=5000&display=10&start=1&sort=random", query, longitude, latitude);

            log.info("URL = " + urlString);
            // URL 객체 생성
            URL url = new URL(urlString);
            // HttpURLConnection 객체 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP 요청 설정
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", searchClientId);
            connection.setRequestProperty("X-Naver-Client-Secret", searchClientSecret);

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

            // 응답 본문 반환
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while calling the API: " + e.getMessage();
        }
    }
}
