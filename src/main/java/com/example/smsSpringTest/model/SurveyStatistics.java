package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-25
 * comment : 당일 면접 질의서 통계용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyStatistics {

    private String date;                    // 기준 날짜
    private String managerId;

    // 당일 면접 질의서 현황 통계
    private int totalCompanies; // 진행중 고객사 수
    private int totalApplicants; // 총 대상자 수 (지원자 수)
    private int sentCount; // 발송된 설문 수
    private int pendingCount; // 미발송된 설문 수
    private int completedCount; // 완료된 설문 수
}
