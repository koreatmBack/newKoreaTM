package com.example.smsSpringTest.model.cafecon;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 응답에서 제외
public class CafeUser {

    private String userId;
    private String userPwd;
    private String companyName; // 업체명
    private String managerName; // 담당자명
    private String phone;
    private int point; // 포인트
    private String useStatus; // 회원 상태 Y : 기본 ,  N : 탈퇴

    private String businessNo; // 사업자 등록번호
    private String businessName; // 사업자명
    private String businessEmail; // 계산서발행 이메일
    private String businessLicense; //사업자 등록증 첨부 url
    private String businessLicenseName; // 사업자 등록증명
    private String businessStatus; // 업태
    private String businessSector; // 업종
    private String businessAddress; // 사업장 주소
    private String ownerName; // 대표명

    private String agreeTerms; // 이용약관 동의 Y/N
    private String agreePrivacy; // 개인정보 수집 및 이용 동의 Y/N
    private String agreeMarketing; // 마케팅 및 이벤트 동의 Y/N
    private String createdAt; // 가입일

    private String role;
    private String userNewPwd; // 새로운 비밀번호 (비밀번호 변경용)

    private String logType; // 관리자 지급인지(AP) , 관리자 차감인지 (AD)

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int offset;

    private String searchType;  // 검색 타입
    private String searchKeyword; // 검색어
    private String startDate;
    private String endDate;
}