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

}