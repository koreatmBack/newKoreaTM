package com.example.smsSpringTest.controller.cafecon;

import com.example.smsSpringTest.model.cafecon.BizApi;
import com.example.smsSpringTest.model.cafecon.Coupon;
import com.example.smsSpringTest.model.cafecon.PointLog;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.cafecon.CouponResponse;
import com.example.smsSpringTest.service.cafecon.CafeconCommonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    // 기프티콘 쿠폰 전송하기
    @PostMapping("/goods/send")
    public CouponResponse sendGoodsToBiz(@RequestBody BizApi bizApi) throws Exception {
        return cafeconCommonService.cafeConSendGiftToBiz(bizApi);
    }

    // 기프티콘 쿠폰 취소하기
    @PostMapping("/cancel/bizapi")
    public ApiResponse cancelCouponToBizapi(@RequestBody BizApi bizApi) throws Exception {
        return cafeconCommonService.cancelCouponToBizapi(bizApi);
    }

    // 기프티콘 취소 내역 있는지 조회
    @PostMapping("/find/cancelLog")
    public ApiResponse findCancelLog(@RequestBody PointLog pl) throws Exception {
        return cafeconCommonService.findCancelLog(pl);
    }

    // 기프티콘 쿠폰 상세 정보
    @PostMapping("/goods/coupons")
    public CouponResponse getCouponsDetailData(@RequestBody Coupon coupon) throws Exception {
//
//        CouponResponse couponResponse = new CouponResponse();
//
//        if(authentication.getName() != null && !authentication.getName().isBlank()) {
//            String userId = authentication.getName();
//            coupon.setUserId(userId);
//            couponResponse = cafeConService.getCouponsDetailData(coupon);
//
//        } else {
//            couponResponse.setCode("E001");
//            couponResponse.setMessage("로그인 후 이용하세요.");
//        }

        return cafeconCommonService.getCouponsDetailData(coupon);
    }

}
