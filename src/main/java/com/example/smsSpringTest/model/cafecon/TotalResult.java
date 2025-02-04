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
}