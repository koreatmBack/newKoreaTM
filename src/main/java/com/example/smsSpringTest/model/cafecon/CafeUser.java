package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 회원 정보 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeUser {

    private String userId;
    private String userPwd;
    private String companyName; // 업체명
    private String managerName; // 담당자명
    private String phone;
    private int point; // 포인트

    private String businessNo; // 사업자 등록번호
    private String businessName; // 사업자명
    private String businessEmail; // 계산서발행 이메일
    private String businessLicense; //사업자 등록증 첨부 url

    private String agreeTerms; // 이용약관 동의 Y/N
    private String agreePrivacy; // 개인정보 수집 및 이용 동의 Y/N
    private String agreeMarketing; // 마케팅 및 이벤트 동의 Y/N
}