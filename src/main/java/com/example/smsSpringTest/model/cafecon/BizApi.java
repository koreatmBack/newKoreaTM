package com.example.smsSpringTest.model.cafecon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 기프티쇼 api vo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizApi {

    private String apiCode;
    private String customAuthCode;
    private String customAuthToken;
    private String devYn;
    private String size;
    private String start;
    private String goodsCode;
    private String userId;
    private String trId;
    private String orderNo;
    private String callbackNo;
    private String phoneNo;
    private String mmsTitle;
    private String mmsMsg;
    private String templateId;
    private String bannerId;
    private String bizId;
    private String gubun;
    private String revInfoYn;
    private String memo;
    private String smsFlag;

    private int realPrice; // 결제 가격
    private String userPoint; // 유저의 현재 포인트
    private String limitDay; // 만료일
    private String goodsImgB; // 상품 이미지
    private String goodsName; // 상품명
    private int remaining; // 유저 포인트 - 결제가격 = 현재 유저의 포인트
    private int discountPrice; // 할인 금액
}