package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-11-19
 * comment : 급여 계산용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Salary {

    private String type1;
    private String type2;
    private String tax; // 세금 미적용, 9.4%, 3.3%
    private String probationPeriod; // 수습 체크
    private double overtime; // 연장 근무 시간
    private double hourSalary; // 시급
    private double dailySalary; // 일급
    private double weekSalary; // 주급
    private double monthSalary; // 월급
    private double yearSalary; // 연봉
    private double workTime; // 일일 근무시간
    private double weekWorkDay; // 일주 근무일수
    private double weekWorkTime; // 일주일 근무시간 (주휴수당 계산기용)
}
