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
    @GetMapping("/login/naver/start")
    public String naverLoginstart(){
        StringBuffer url = new StringBuffer();
        url.append("https://nid.naver.com/oauth2.0/authorize?");
        url.append("client_id="+naverClientId);
        url.append("&redirect_uri="+naverRedirectUri);
        url.append("&response_type=code");
        return "redirect:"+url.toString();
    }



    // --- NAVER 끝 ---
}
