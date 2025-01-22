package com.example.smsSpringTest.model.cafecon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-22
 * comment : 카페콘 입금 vo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deposit {

    private String num; // 거래번호
    private String userId;  // 회원 아이디
    private int chargePoint;    // 충전 포인트
    private int depositAmount;  // 입금 금액
    private String depositorName;   // 입금자명
    private String status;  // 상태 (충전예정, 보류확인, 충전완료)
    private String chargeRequest;   // 충전요청 (신청완료, 재신청)
    private String invoice; // 거래명세서
    private String regDate; // 등록일
}
