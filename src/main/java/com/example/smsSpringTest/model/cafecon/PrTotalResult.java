package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-06
 * comment : 조회된 날짜별 프로모션의 포인트 합계 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrTotalResult {

    private int totalPlusPnt;
    private int totalMiunsPnt;

    private int totalRtPnt; // 프로모션 지급 - 룰렛 총액
    private int totalAtPnt; // 프로모션 지급 - 출석체크 총액
    private int totalRbPnt; // 프로모션 지급 - 랜덤박스 총액
    private int totalDrPnt; // 프로모션 지급 - 주사위 굴리기 총액

    private int totalRtCancelPnt; // 프로모션 취소 - 룰렛 총액
    private int totalAtCancelPnt; // 프로모션 취소 - 출석체크 총액
    private int totalRbCancelPnt; // 프로모션 취소 - 랜덤박스 총액
    private int totalDrCancelPnt; // 프로모션 취소 - 주사위 굴리기 총액

}
