package com.example.smsSpringTest.model.response.cafecon;

import com.example.smsSpringTest.model.cafecon.Coupon;
import com.example.smsSpringTest.model.cafecon.CouponDetail;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-01-20
 * comment : 쿠폰 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponResponse extends ApiResponse {

    private List<Coupon> couponList;
    private CouponDetail couponDetail;
    private String trId;
    private int point;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int resendCnt;

}
