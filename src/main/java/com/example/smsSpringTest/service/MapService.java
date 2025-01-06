package com.example.smsSpringTest.service;

import com.example.smsSpringTest.model.MapVO;
import com.example.smsSpringTest.model.response.MapResponse;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class MapService {

//    private final WebClient mapWebClient;
//    private final WebClient searchWebClient;
    @Value("${naver.map.client-id}")
    private String mapClientId;
    @Value("${naver.map.client-secret}")
    private String mapClientSecret;

    private static final Map<String, String> subwayColors = new HashMap<>();
    static {
        subwayColors.put("경의중앙","#77C4A3");
        subwayColors.put("수인분당","#F5A200");
        subwayColors.put("경강","#003DA5");
        subwayColors.put("대경","#003DA5");
        subwayColors.put("경춘","#0C8E72");
        subwayColors.put("1","#0052A4"); // 서울 1호선
        subwayColors.put("2","#00A84D"); // 서울 2호선
        subwayColors.put("3","#EF7C1C"); // 서울 3호선
        subwayColors.put("4","#00A5DE"); // 서울 4호선
        subwayColors.put("5","#996CAC"); // 서울 5호선
        subwayColors.put("6","#CD7C2F"); // 서울 6호선
        subwayColors.put("7","#747F00"); // 서울 7호선
        subwayColors.put("8","#E6186C"); // 서울 8호선
        subwayColors.put("9","#BB8336"); // 서울 9호선
        subwayColors.put("김포골드","#A17800");
        subwayColors.put("우이신설","#B0CE18");
        subwayColors.put("신림","#6789CA");
        subwayColors.put("인천1","#7CA8D5");
        subwayColors.put("인천2","#ED8B00");
        subwayColors.put("자기부상철도","#FFCD12");
        subwayColors.put("공항철도","#0090D2");
        subwayColors.put("의정부경전철","#FDA600");
        subwayColors.put("에버라인","#509F22");
        subwayColors.put("신분당","#D4003B");
        subwayColors.put("서해","#81A914");
        subwayColors.put("부산1","#F06A00");
        subwayColors.put("부산2","#81BF48");
        subwayColors.put("부산3","#BB8C00");
        subwayColors.put("부산4","#217DCB");
        subwayColors.put("부산5","#D6406A");
        subwayColors.put("부산김해경전철","#8652A1");
        subwayColors.put("대구1","#D93F5C");
        subwayColors.put("대구2","#00AA80");
        subwayColors.put("대구3","#FFB100");
        subwayColors.put("대구4","#0099CC");
        subwayColors.put("광주1","#009088");
        subwayColors.put("광주2","#0471C3");
        subwayColors.put("대전1","#007448");
        subwayColors.put("대전2","#40E0D0");
    }

//    // 네이버 지도 기본 틀
//    @Autowired
//    public MapService(WebClient.Builder webClientBuilder,
//                      @Value("${naver.map.client-id}") String mapClientId,
//                      @Value("${naver.map.client-secret}") String mapClientSecret,
//                      @Value("${naver.search.client-id}") String searchClientId,
//                      @Value("${naver.search.client-secret}") String searchClientSecret
//                           ) {
////        log.info("Client ID: {}", mapClientId);
////        log.info("Client Secret: {}", mapClientSecret);
////        log.info("Search Client ID: {}", searchClientId);
////        log.info("Search Client Secret: {}", searchClientSecret);
//
//        this.mapWebClient = webClientBuilder.baseUrl("https://naveropenapi.apigw.ntruss.com")  // 변경된 URL로 수정
//                .defaultHeader("X-NCP-APIGW-API-KEY-ID", mapClientId)  // 인증 헤더 수정
//                .defaultHeader("X-NCP-APIGW-API-KEY", mapClientSecret)  // 인증 헤더 수정
//                .build();
//
//        // 검색 API용 WebClient 설정
//        this.searchWebClient = webClientBuilder
//                .baseUrl("https://openapi.naver.com")
//                .defaultHeader("X-Naver-Client-Id", searchClientId)
//                .defaultHeader("X-Naver-Client-Secret", searchClientSecret)
//                .build();
//    }


    // 위도 경도 찾는 메서드
    public MapResponse getCoordinates(String address) {
        MapResponse mapResponse = new MapResponse();
        try {
//            log.info("service : address = " + address);
            String query = URLEncoder.encode(address, "UTF-8");
            String urlString = String.format("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=%s", query);

//            log.info("URL = " + urlString);
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

//            log.info(response.toString());

            JsonObject keys = (JsonObject) JsonParser.parseString(response.toString());

//            log.info("keys addresses = " + keys.get("addresses").toString());
            JsonArray addresses = keys.getAsJsonArray("addresses");

            if (addresses.size() > 0) {
                JsonObject firstAddress = addresses.get(0).getAsJsonObject();
                double y = Double.parseDouble(firstAddress.get("x").getAsString());
                double x = Double.parseDouble(firstAddress.get("y").getAsString());

//                log.info("x = " + x);
//                log.info("y = " + y);
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

    // 위도, 경도  찾기 -> 공고 수정시 사용할 API
    // 위도 경도 찾는 메서드
    public List<Double> addCoordinates(String address) {

        List<Double> coordinate = null;
        try {
//            log.info("service : address = " + address);
            String query = URLEncoder.encode(address, "UTF-8");
            String urlString = String.format("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=%s", query);

//            log.info("URL = " + urlString);
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

//            log.info(response.toString());

            JsonObject keys = (JsonObject) JsonParser.parseString(response.toString());

//            log.info("keys addresses = " + keys.get("addresses").toString());
            JsonArray addresses = keys.getAsJsonArray("addresses");

            if (addresses.size() > 0) {
                JsonObject firstAddress = addresses.get(0).getAsJsonObject();
                double y = Double.parseDouble(firstAddress.get("x").getAsString());
                double x = Double.parseDouble(String.valueOf(firstAddress.get("y")));

//                log.info("x = " + x);
//                log.info("y = " + y);

                coordinate = new ArrayList<>();
                coordinate.add(0, x);
                coordinate.add(1, y);

//                log.info(coordinate.toString());
            }

        } catch (IOException e) {

            log.info(e.getMessage());
        }
        return coordinate;
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 지하철 색 갖고 오기
    public String getSubwayColor(String line) {
        return subwayColors.getOrDefault(line, "#000000"); // 기본값: 검정색
    }

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

//        log.info(response.toString());

        // JSON 문자열로 가져오기
        String jsonResponse = response.getBody();
        String schoolJsonResponse = schoolResponse.getBody();
//        System.out.println("Response JSON: " + jsonResponse);
//        System.out.println("SCHOOL JSON: " + schoolJsonResponse);

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
//        System.out.println("SchoolDocumentsList:");
        String university = "";
        for (JsonObject doc : schoolDocumentsList) {
            university = doc.get("place_name").getAsString();
//            System.out.println("Place Name: " + doc.get("place_name").getAsString());
            if(StringUtils.hasText(university)){
                break;
            }

        }
        List<MapVO> mapInfoList = new ArrayList<>();
//        log.info(documents.toString());

        // 중복 제거를 위한 임시 Map (placeName을 키로 사용)
        LinkedHashMap<String, MapVO> uniqueMap = new LinkedHashMap<>();

        // 지하철역 결과 처리
        for (JsonElement element : documents) {
            JsonObject document = element.getAsJsonObject();
            // 필요한 데이터 추출
            String fullPlaceName = document.get("place_name").getAsString(); // 전체 이름 (예: 군자역 7호선)
            // 역 이름과 노선 이름 분리
//            log.info("full = " + fullPlaceName);
            String[] parts = fullPlaceName.split(" "); // 공백 기준으로 분리
//                String stationName = fullPlaceName.split(" ")[0]; // 역 이름만 추출 (예: 군자역)
            String stationName = parts[0]; // 역 이름만 추출 (예: 군자역)
            // "호선" 또는 "선" 앞부분 추출
            String line = "";
//            log.info("parts[1] = " + parts[1]);
            if (parts.length > 1) { // 노선 이름이 있는 경우 처리
                String rawLine = parts[1]; // 노선 이름 부분 (예: 경의중앙선, 2호선 등)
                log.info("rawline = " + rawLine);
                if (rawLine.contains("호선")) { // "숫자 + 호선" 패턴 확인
                    line = rawLine.split("호선")[0].trim();
                } else if (rawLine.contains("선")) { // "선" 포함된 경우
                    line = rawLine.split("선")[0].trim();
                } else if (rawLine.contains("에버라인")){
                    line = "에버라인";
                } else if (rawLine.contains("라인")){
                    line = rawLine.split("라인")[0].trim();
                } else if (rawLine.contains("자기부상철도[운행중단]")){
                    line = "자기부상철도";
                } else {
                    line = rawLine;
                }
            }
//            log.info("line = " + line);
            double subwayY = document.get("x").getAsDouble(); // 위도
            double subwayX = document.get("y").getAsDouble(); // 경도
            int  distance = Integer.parseInt(document.get("distance").getAsString()); // 거리 (m)
            int durationTime = distance / 65; // 걸리는 시간 (분 단위로 변환)

            String newDurationTime = String.valueOf(durationTime);

//            log.info(String.format("Place: %s, Longitude: %s, Latitude: %s, durationTime: %s ,Distance: %dm",
//                    fullPlaceName, subwayX, subwayY, newDurationTime ,distance));

            double result = (double) distance / 1000;
            String KmDistance = String.format("%.1f", result); // 소수점 1자리까지 유지

            String subwayColor = null;
            subwayColor = getSubwayColor(line);
            MapVO mapVO = new MapVO(subwayX, subwayY, "", stationName, "걸어서 " + newDurationTime + "분", KmDistance + "km", line, fullPlaceName, subwayColor);

            // 중복 제거: 역 이름(stationName)을 기준으로 중복 제거
            uniqueMap.putIfAbsent(stationName, mapVO);

        }

        // 중복 제거된 리스트를 가져옴
        List<MapVO> uniqueMapInfoList = new ArrayList<>(uniqueMap.values());

        // 거리(distance)가 작은 순서대로 정렬 후 상위 3개만 선택
        List<MapVO> sortedMapInfoList = uniqueMapInfoList.stream()
                .sorted(Comparator.comparingDouble(vo -> Double.parseDouble(vo.getDistance().replace("km", "").trim())))
                .limit(3)
                .collect(Collectors.toList());
//        log.info("sortedMap = " + sortedMapInfoList);
        mapResponse.setUniversity(university);
        mapResponse.setMapInfoList(sortedMapInfoList);
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


//    // 공고 수정시 주변 정보 찾는 API
//    public List<MapVO> findNearInfo(double y, double x) throws Exception {
//
//        List<MapVO> sortedMapInfoList = null;
//        try {
//
//            String schoolURL = String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SC4&x=%f&y=%f&radius=3000",
//                    y, x);
//            String url = String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SW8&x=%f&y=%f&radius=1500"
//                    , y, x);
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "KakaoAK " + kakaoClientId);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//            ResponseEntity<String> schoolResponse = restTemplate.exchange(schoolURL, HttpMethod.GET, entity, String.class);
//
//            log.info(response.toString());
//
//            // JSON 문자열로 가져오기
//            String jsonResponse = response.getBody();
//            String schoolJsonResponse = schoolResponse.getBody();
//            System.out.println("Response JSON: " + jsonResponse);
//            System.out.println("SCHOOL JSON: " + schoolJsonResponse);
//
//            // Gson 객체 생성
//            Gson gson = new Gson();
//
//            // JSON 문자열을 JsonObject로 변환
//            JsonObject responseJson = gson.fromJson(jsonResponse, JsonObject.class);
//            JsonObject schoolResponseJson = gson.fromJson(schoolJsonResponse, JsonObject.class);
//
//            // "documents" 배열 추출
//            JsonArray documents = responseJson.getAsJsonArray("documents");
//            JsonArray schoolDocuments = schoolResponseJson.getAsJsonArray("documents");
//
//            // 대학교 저장할 리스트
//            List<JsonObject> schoolDocumentsList = new ArrayList<>();
//
//            // "category_name"에 "대학교"가 포함된 데이터만 필터링
//            for (JsonElement element : schoolDocuments) {
//                JsonObject document = element.getAsJsonObject();
//                String categoryName = document.get("category_name").getAsString();
//                if (categoryName.contains("대학교")) {
//                    schoolDocumentsList.add(document);
//                }
//            }
//            System.out.println("SchoolDocumentsList:");
//            String university = "";
//            for (JsonObject doc : schoolDocumentsList) {
//                university = doc.get("place_name").getAsString();
//                System.out.println("Place Name: " + doc.get("place_name").getAsString());
//                if (StringUtils.hasText(university)) {
//                    break;
//                }
//
//            }
//            List<MapVO> mapInfoList = new ArrayList<>();
//            log.info(documents.toString());
//
//            // 중복 제거를 위한 임시 Map (placeName을 키로 사용)
//            LinkedHashMap<String, MapVO> uniqueMap = new LinkedHashMap<>();
//
//            // 지하철역 결과 처리
//            for (JsonElement element : documents) {
//                JsonObject document = element.getAsJsonObject();
//                // 필요한 데이터 추출
//                String fullPlaceName = document.get("place_name").getAsString(); // 전체 이름 (예: 군자역 7호선)
//                // 역 이름과 노선 이름 분리
//                log.info("full = " + fullPlaceName);
//                String[] parts = fullPlaceName.split(" "); // 공백 기준으로 분리
////                String stationName = fullPlaceName.split(" ")[0]; // 역 이름만 추출 (예: 군자역)
//                String stationName = parts[0]; // 역 이름만 추출 (예: 군자역)
//                // "호선" 또는 "선" 앞부분 추출
//                String line = "";
//                log.info("parts = " + parts.toString());
//                if (parts.length > 1) { // 노선 이름이 있는 경우 처리
//                    String rawLine = parts[1]; // 노선 이름 부분 (예: 경의중앙선, 2호선 등)
//                    log.info("rawline = " + rawLine);
//                    if (rawLine.contains("호선")) { // "숫자 + 호선" 패턴 확인
//                        line = fullPlaceName.split("호선")[0].trim();
//                    } else if (rawLine.contains("선")) { // "선" 포함된 경우
//                        line = fullPlaceName.split("선")[0].trim();
//                    }
//                }
//                log.info("line = " + line);
//                double subwayY = document.get("x").getAsDouble(); // 위도
//                double subwayX = document.get("y").getAsDouble(); // 경도
//                int distance = Integer.parseInt(document.get("distance").getAsString()); // 거리 (m)
//                int durationTime = distance / 65; // 걸리는 시간 (분 단위로 변환)
//
//                String newDurationTime = String.valueOf(durationTime);
//
////                log.info(String.format("Place: %s, Longitude: %s, Latitude: %s, durationTime: %s ,Distance: %dm",
////                        fullPlaceName, subwayX, subwayY, newDurationTime, distance));
//
//                double result = (double) distance / 1000;
//                String KmDistance = String.format("%.1f", result); // 소수점 1자리까지 유지
//
//                String subwayColor = null;
//                MapVO mapVO = new MapVO(subwayX, subwayY, "", fullPlaceName, "걸어서 " + newDurationTime + "분", KmDistance + "km", line , fullPlaceName, subwayColor);
//
//                // 중복 제거: 역 이름(stationName)을 기준으로 중복 제거
//                uniqueMap.putIfAbsent(stationName, mapVO);
//
//            }
//
//            // 중복 제거된 리스트를 가져옴
//            List<MapVO> uniqueMapInfoList = new ArrayList<>(uniqueMap.values());
//
//            // 거리(distance)가 작은 순서대로 정렬 후 상위 3개만 선택
//            sortedMapInfoList = uniqueMapInfoList.stream()
//                    .sorted(Comparator.comparingDouble(vo -> Double.parseDouble(vo.getDistance().replace("km", "").trim())))
//                    .limit(3)
//                    .collect(Collectors.toList());
//            log.info("sortedMap = " + sortedMapInfoList);
//
//        } catch (Exception e) {
//
//            log.info(e.getMessage());
//        }
//        return sortedMapInfoList;
//    }

}
