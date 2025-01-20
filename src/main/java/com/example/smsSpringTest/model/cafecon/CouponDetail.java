package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-20
 * comment : 기프티콘 핀 상태 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CouponDetail {

    private String goodsCd;
    private String pinStatusCd;
    private String goodsNm;
    private String sellPriceAmt;
    private String senderTelNo;
    private String cnsmPriceAmt;
    private String sendRstCd;
    private String pinStatusNm;
    private String mmsBrandThumImg;
    private String brandNm;
    private String sendRstMsg;
    private String correcDtm;
    private String recverTelNo;
    private String validPrdEndDt;
    private String sendBasicCd;
    private String sendStatusCd;
    private String remainAmt;
}