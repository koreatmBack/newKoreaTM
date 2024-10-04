package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.mapper.HolidayMapper;
import com.example.smsSpringTest.model.Holiday;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/formmail_holiday")
@Slf4j
public class formMail_holidayController {

    private final HolidayMapper holidayMapper;

    @Value("${holiday.service-key}")
    private String serviceKey;

    @PostMapping("/addHoliday")
    public ApiResponse addHoliday(@RequestBody Holiday holiday) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {
            String year = holiday.getYear();

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*타입*/
            urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("365", "UTF-8")); /*최대로 출력할 공휴일 수*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { //http status code check
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
//        System.out.println(sb.toString());


            // JSON 파싱 시작
            String jsonResponse = sb.toString();

            // Jackson ObjectMapper를 사용하여 JSON 문자열을 JsonNode로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // "items" 배열 안의 "item" 배열을 가져오기
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");

            // 필요한 값 추출: dateName과 locdate
            for (JsonNode item : items) {
                String dateName = item.path("dateName").asText();
                int locdate = item.path("locdate").asInt();

                // locdate를 LocalDate로 변환
                LocalDate date = convertLocdateToDate(locdate);
                try {
                    int addHoliday = holidayMapper.addHoliday(dateName, date);
                    apiResponse.setCode("C001");
                    apiResponse.setMessage("공휴일 등록 성공");
                } catch (Exception e) {
                    log.info(e.getMessage());
                    apiResponse.setCode("E001");
                    apiResponse.setMessage("이미 등록된 날짜입니다.");
                }

                // 추출한 값 출력
                log.info("dateName: " + dateName + ", locdate: " + date);
            }

            // 연도 int로 변경
            int yearInt = Integer.parseInt(year);
            List<LocalDate> weekends = getWeekendsOfYear(yearInt);
            log.info("주말 날짜: " + weekends);

            for (LocalDate weekend : weekends) {
                // db에 주말 등록
                int addWeekend = holidayMapper.addWeekend(weekend);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
        }
        return apiResponse;
    }


    // locdate를 LocalDate로 변환하는 메서드
    private static LocalDate convertLocdateToDate(int locdate) {
        // locdate를 문자열로 변환 (20240101 -> "20240101")
        String locdateStr = String.valueOf(locdate);
        // yyyyMMdd 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 문자열을 LocalDate로 변환
        return LocalDate.parse(locdateStr, formatter);
    }

    // 해당 연도의 주말 날짜 구하기
    public static List<LocalDate> getWeekendsOfYear(int year) {
        List<LocalDate> weekends = new ArrayList<>();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends.add(date);
            }
        }
        return weekends;
    }
}
