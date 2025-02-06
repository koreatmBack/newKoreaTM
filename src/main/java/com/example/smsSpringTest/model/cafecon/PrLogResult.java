package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-06
 * comment : 회원 날짜별 프로모션 포인트 결과값 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrLogResult {

    private int plusPnt; // 지급액
    private int miunsPnt; // 차감액
    private String regDate;

    private int rtPnt; // 프로모션 지급 - 룰렛
    private int atPnt; // 프로모션 지급 - 출첵
    private int rbPnt; // 프로모션 지급 - 랜덤박스
    private int drPnt; // 프로모션 지급 - 주사위 굴리기

    private int rtCancelPnt; // 프로모션 취소 - 룰렛
    private int atCancelPnt; // 프로모션 취소 - 출첵
    private int rbCancelPnt; // 프로모션 취소 - 랜덤박스
    private int drCancelPnt; // 프로모션 취소 - 주사위 굴리기

}
