package com.example.smsSpringTest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-11-19
 * comment : 급여 계산 결과값 리턴
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalaryResponse extends ApiResponse{

    private int hourSalary; // 시급
    private int dailySalary; // 일급
    private int weekSalary; // 주급
    private int monthSalary; // 월급
    private int yearSalary; // 연봉
    private int weekHolidayPay; // 주휴 수당
    private int totalSalary; // 최종 급여 금액
    private int overtimePay; // 연장 수당

}
