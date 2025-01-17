package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘 회원 기프티콘 정보 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeCoupon {

    private String userId;
    private String trId;
    private String phone;
    private String goodsCode;
    private String goodsName;
    private String orderNo;
    private String pinNo;
    private String couponImgUrl;
    private String goodsImgB;
    private String successYn;
    private String regDate;
    private String limitDate;
    private String code;
    private String message;
    private String memo;
}