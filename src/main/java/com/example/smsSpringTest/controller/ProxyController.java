package com.example.smsSpringTest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping
    public ResponseEntity<String> convertToHttps(@RequestParam String url) {
        try {
            // 1. HTTP URL을 URL 인코딩
            String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.name());

            // 2. 자체 서버의 HTTPS로 변환된 URL을 생성
            String httpsUrl = "https://cafecon.co.kr/redirect?url=" + encodedUrl;

            // 3. 변환된 HTTPS URL을 클라이언트에 반환
            return ResponseEntity.ok(httpsUrl);

        } catch (Exception e) {
            // 예외 처리 (잘못된 URL, 인코딩 오류 등)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the URL");
        }
    }
}