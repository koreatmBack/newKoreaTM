package com.example.smsSpringTest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/api/v1/social", "/v1/social"})
@Slf4j
@RequiredArgsConstructor
public class SocialController {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    // -- kakao 로그인 테스트 위한 코드들

    // 로그인 시작
    @GetMapping("/login/kakao/start")
    public String kakaoLoginstart(){
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoClientId);
        url.append("&redirect_uri="+kakaoRedirectUri);
        url.append("&response_type=code");
        return "redirect:"+url.toString();
    }

    // 카카오 소셜 로그인 연동
    @GetMapping("/kakao/integ")
    public String kakaoInteg(){
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoClientId);
        url.append("&redirect_uri=http://localhost:8080/v1/jobsite/user/kakao/integ");
        url.append("&response_type=code");
        return "redirect:"+url.toString();
    }

    // --- 카카오 끝 ----


    // --- NAVER 시작 ---

    // 네이버 로그인 시작
    @GetMapping("/login/naver/start")
    public String naverLoginstart(){
        StringBuffer url = new StringBuffer();
        url.append("https://nid.naver.com/oauth2.0/authorize?");
        url.append("client_id="+naverClientId);
        url.append("&redirect_uri="+naverRedirectUri);
        url.append("&response_type=code");
        return "redirect:"+url.toString();
    }

    // 네이버 소셜 연동
    @GetMapping("/naver/integ")
    public String naverInteg(){
        StringBuffer url = new StringBuffer();
        url.append("https://nid.naver.com/oauth2.0/authorize?");
        url.append("client_id="+naverClientId);
//        url.append("&redirect_uri=http://localhost:8080/v1/jobsite/user/naver/integ");
        url.append("&redirect_uri=https://koti-job.kro.kr/v1/jobsite/user/naver/integ");
        url.append("&response_type=code");
        return "redirect:"+url.toString();
    }

    // --- NAVER 끝 ---


    // google 시작 ---
    @GetMapping("/login/google/start")
    public String googleLoginStart(){
        StringBuffer url = new StringBuffer();
        url.append("https://accounts.google.com/o/oauth2/v2/auth?");
        url.append("client_id="+googleClientId);
        url.append("&redirect_uri="+googleRedirectUri);
        url.append("&response_type=code");
        url.append("&scope=email profile");
        return "redirect:"+url.toString();
    }

    // google 소셜 연동
    @GetMapping("/google/integ")
    public String googleInteg(){
        StringBuffer url = new StringBuffer();
        url.append("https://accounts.google.com/o/oauth2/v2/auth?");
        url.append("client_id="+googleClientId);
//        url.append("&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fv1%2Fjobsite%2Fuser%2Fgoogle%2Finteg");
        url.append("&redirect_uri=https%3A%2F%2Fkoti-job.kro.kr%2Fv1%2Fjobsite%2Fuser%2Fgoogle%2Finteg");
        url.append("&response_type=code");
        url.append("&scope=email profile");
        return "redirect:"+url.toString();
    }

}
