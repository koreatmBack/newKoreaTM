package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-04
 * comment : 조회된 날짜별 포인트 합계 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalResult {

    private int totalPlusPnt;
    private int totalMiunsPnt;
    private int totalApPnt;
    private int totalAdPnt;
    private int totalCpPnt;
    private int totalCePnt;
    private int totalGiPnt;
    private int totalChPnt; // 포인트 충전 총액
    private int totalCaPnt; // 포인트 충전 취소 총액
    private int totalPrPnt; // 프로모션 지급 총액
    private int totalPcPnt; // 프로모션 지급 취소 총액

    private int totalRtPnt; // 프로모션 - 룰렛 총액
    private int totalAtPnt; // 프로모션 - 출석체크 총액
    private int totalRbPnt; // 프로모션 - 랜덤박스 총액
    private int totalDrPnt; // 프로모션 - 주사위 굴리기 총액
}