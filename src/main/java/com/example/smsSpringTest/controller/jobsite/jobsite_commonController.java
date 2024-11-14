package com.example.smsSpringTest.controller.jobsite;

import com.example.smsSpringTest.model.Apply;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.service.formMail_applyService;
import com.example.smsSpringTest.service.jobsite.jobsite_commonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-10-30
 * comment : 잡사이트 공통 controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/jobsite/common", "/v1/jobsite/common"})
@Slf4j
public class jobsite_commonController {

    private final jobsite_commonService jobsiteCommonService;
    private final formMail_applyService formMailApplyService;

    // 쿠키 만료시간 보내주기
    @GetMapping("/exper_cookie")
    @Operation(summary = "쿠키 만료 시간 조회", description="쿠키 남은 유효 시간을 보내줍니다.")
    public AccessResponse experCookie() throws Exception {
        return jobsiteCommonService.experCookie();
    }

//    // 리프레쉬 토큰 + 어쎄스 토큰 재발급
//    @PostMapping("/reissu/RefreshToken")
//    public RefResponse reissuRefreshToken(@RequestBody RefToken refToken) throws Exception {
//        return jobsiteCommonService.reissuToken(refToken);
//    }

    // access 토큰 재발급 및 쿠키 재발급
    @GetMapping("/reissu/AccessToken")
    @Operation(summary = "Access Token 재발급 및 쿠키 재발급", description="토큰과 쿠키를 재발급합니다.")
    public ApiResponse reissuAccessToken() throws Exception {
        return jobsiteCommonService.reissuAccessToken();
    }

    // 지원자 등록하기 (회원 , 비회원 둘다 가능)
    @PostMapping("/addApply")
    @Operation(summary = "지원자 등록", description="신규 지원자를 등록합니다.")
    public ApiResponse addApply(@RequestBody Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailApplyService.addApply(apply);
        return apiResponse;
    }

}
