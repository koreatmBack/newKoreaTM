package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-04
 * comment : 회원 날짜별 포인트 결과값 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogResult {

    private int plusPnt; // 지급액
    private int miunsPnt; // 차감액
    private int cpPnt; // 쿠폰 구매
    private int cePnt; // 쿠폰 구매 취소
    private int apPnt; // 관리자 임의 지금
    private int adPnt; // 관리자 임의 차감
    private int giPnt; // 쿠폰 선물
    private int chPnt; // 포인트 충전
    private int caPnt; // 포인트 충전 취소
    private int prPnt; // 프로모션 지급
    private int pcPnt; // 프로모션 지급 취소

    private String regDate;

    private int rtPnt; // 룰렛
    private int atPnt; // 출첵
    private int rbPnt; // 랜덤박스
    private int drPnt; // 주사위 굴리기
}
