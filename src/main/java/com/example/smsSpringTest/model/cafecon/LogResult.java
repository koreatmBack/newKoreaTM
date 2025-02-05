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

    private int plusPnt;
    private int miunsPnt;
    private int cpPnt;
    private int cePnt;
    private int apPnt;
    private int adPnt;
    private int giPnt;

    private String regDate;
}
