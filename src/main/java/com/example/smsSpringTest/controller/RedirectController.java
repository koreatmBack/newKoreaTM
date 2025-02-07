package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.service.RedirectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * author : 신기훈
 * date : 2024-12-30
 * comment : 혼합 컨텐츠(http -> https) 폼메일 이미지 url 변경 controller
 */
@RestController
@RequestMapping("/redirect")
@Slf4j
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping
    public ResponseEntity<byte[]> fetchResource(@RequestParam String url,
                                                @RequestParam(required = false) String page) {
        try {
            // URL 디코딩
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.name());

            if (StringUtils.hasText(page)) {
                // 페이지 값 있으면 조회수 추가
                redirectService.countRedirect(page, decodedUrl);
                log.info("여기 들어옴?");
            }

            // HTTP URL에서 리소스 가져오기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(decodedUrl, byte[].class);

            // 필요한 헤더와 데이터를 응답으로 리턴
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(response.getHeaders().getContentType());
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 처리 (잘못된 URL, 연결 오류 등)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}