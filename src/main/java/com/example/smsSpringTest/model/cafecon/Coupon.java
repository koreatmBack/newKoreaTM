package com.example.smsSpringTest.model.cafecon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-20
 * comment : 회원 기프티콘 구매 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 응답에서 제외
public class Coupon {

    private String userId;
    private String trId;
    private String phone;
    private String goodsCode;
    private String userName;
    private String goodsName;
    private String orderNo;
    private String pinNo;
    private String couponImgUrl;
    private String goodsImgB;
    private String successYn;
    private String regDate;
    private String limitDate;
    private String cancelDate;
    private String resendCnt;
    private String code;
    private String message;
}