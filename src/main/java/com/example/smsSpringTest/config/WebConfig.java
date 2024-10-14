package com.example.smsSpringTest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : cors 설정
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
            .allowedOrigins("http://localhost:3013", "http://192.168.0.34:3013","http://localhost:3014", "http://192.168.0.34:3014", "http://koti-jobsite.s3-website.ap-northeast-2.amazonaws.com/" ) // 허용할 도메인
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
            .allowedHeaders("*") // 허용할 헤더
            .allowCredentials(true) // 자격 증명 허용 (예: 쿠키)
            .maxAge(3600); // preflight 요청을 캐싱하는 시간 (초 단위)
}
}