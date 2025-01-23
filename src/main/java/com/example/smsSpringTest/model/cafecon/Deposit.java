package com.example.smsSpringTest.model.cafecon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime regDate; // 등록일

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int offset;

    // 조인 테이블로 값 추출 위한 변수들
    private String companyName; // 업체명
    private String managerName; // 담당자명
    private String phone; // 회원 연락처



}
