package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-20
 * comment : 통계용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics {

//    private int no;
    private String date;                    // 기준 날짜
    private int totalApply;                 // 총 지원자 수
    private int totalAvailable;             // 총 가용 수
    private int todayInterviewExpect;       // 당일 면접 예정 수
    private int yesterdayInterviewAttend;   // 전일 면접 참석 수
    private int tomorrowInterviewExpect;    //익일 면접 예정 수

    private int totalApplyChangeValue; // 총 지원자 증감
    private int totalAvailableChangeValue; // 총 가용수 증감
    private int todayInterviewExpectChangeValue; // 당일 면접 예정 증감
    private int yesterdayInterviewAttendChangeValue; // 전일 면접 참석 증감
    private int tomorrowInterviewExpectChangeValue; // 익일 면접 예정 증감

    private String managerId;

}
