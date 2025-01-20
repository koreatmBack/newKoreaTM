package com.example.smsSpringTest.controller.cafecon;

import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.service.cafecon.CafeconCommonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 Common controller
 */
@RestController
@RequestMapping({"/api/v1/cafecon/common", "/v1/cafecon/common"})
@Slf4j
@RequiredArgsConstructor
public class CafeconCommonController {

    private final CafeconCommonService cafeconCommonService;

    // 쿠키 만료시간 보내주기
    @GetMapping("/exper_cookie")
    public AccessResponse experCookie() throws Exception {
        return cafeconCommonService.experCookie();
    }

    // access 토큰 재발급 및 쿠키 재발급
    @GetMapping("/reissu/AccessToken")
    @Operation(summary = "Access Token 재발급 및 쿠키 재발급", description="토큰과 쿠키를 재발급합니다.")
    public ApiResponse reissuAccessToken() throws Exception {
        return cafeconCommonService.reissuAccessToken();
    }

    

}
