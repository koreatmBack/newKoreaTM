package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-03-05
 * comment : 고객사 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    private String cid; // 고객사 고유 id
    private String comNameAlias; // 고객사 고유번호
    private String managerId;   // 코티 파트너 id
    private String comCenter;   // 사업단
    private String comGubun;  // 보험사 구분 (생명 or 손해)
    private String comName;     // 보험사명
    private String comChannel;  // 채널
    private String comSpot;     // 지점명
    private String comPhone;    // 고객사 연락처
    private String comAddress;  // 근무지 주소
    private String headName;    // 단장님 성함
    private String headPhone;   // 단장님 연락처
    private String leaderName;  // 지점장님 성함
    private String leaderPhone; // 지점장님 연락처
    private String manager;     // 이름 / 직책 / 연락처
    private String manager1;    // 추가 담당자1
    private String manager2;    // 추가 담당자2
    private String manager3;    // 추가 담당자3
    private String genName;     // 총무
    private String genPhone;    // 총무 연락처
    private String affiliation; // 상담사 소속
    private String headCount;   // 직원 수
    private String location;    // 사무실 위치
    private String workStart;   // 근무 시작 시간
    private String workEnd;     // 근무 종료 시간
    private String workTime;    // 총 근무 시간 (근무 종료시간 - 근무 시작시간 - 점심 - 휴식)
    private String lunchTime;   // 점심시간
    private String restTime;    // 휴식시간
    private String workType;    // 근무 형태
    private String park;        // 주차 여부
    private String cafe;        // 근처 커피숍
    private String comPhoto;    // 회사 사진
    private String business;    // 업무 형태
    private String useDb;       // 사용DB
    private String merch;       // 주력 판매상품
    private String interviewTarget; // 면접 가능 대상
    private String reward;      // 면접비 or 면접 선물
    private String intType;     // 면접 진행 방식
    private String intTime;     // 면접 가능 시간대
    private String intCount;    // 동시 면접 가능 인원
    private String eduCost;     // 교육비
    private String eduInst;     // 교육비 분할 개월 수
    private String firstPay;    // 첫 달 수령금액
    private String guarPay;     // 최저 보장 금액
    private String guarPeriod;  // 최저 보장 기간
    private String avePay;      // 평균 급여
    private String topPay;      // 상위자 최대 급여
    private String bonus;       // 성과급(보너스)
    private String promo;       // 프로모션
    private String bond;        // 이행보증 유/무
    private String welfare;     // 복지사항
    private String welfEtc;     // 기타 복지사항
    private String prevEmp;     // 기존 채용 방식
    private String empSch;      // 입과 일정
    private String strength;    // 지점의 강점
    private String propose;     // 기타 제안 사항
    private String suggest;     // 추천인
    private String profile;     // 면접 진행 담당자 사진
    private String empAdmin;    // 관리자 채용 여부 (예/아니오)
    private String visitMeet;   // 방문미팅 희망 여부 (예/아니오)
    private Boolean surveyProceed; // 면접 질의서 진행 여부
    private String regDate;     // 고객사 등록일
    private String uptDate;     // 고객사 수정일


}