package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.jobsite.Salary;
import com.example.smsSpringTest.model.response.jobsite.SalaryResponse;
import com.example.smsSpringTest.service.SalaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : 신기훈
 * date : 2024-11-19
 * comment : 급여 계산기 controller
 */
@RestController
@RequestMapping({"/api/v1/calculate/salary", "/v1/calculate/salary"})
@Slf4j
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    // 급여 계산기
    @PostMapping("")
    public SalaryResponse salaryCalculation(@RequestBody Salary salary) throws Exception {
        return salaryService.salaryCalculation(salary);
    }

    // 주휴수당 계산기
    @PostMapping("/weekHoliday")
    public SalaryResponse weekHolidayPayCalculation(@RequestBody Salary salary) throws Exception {
        return salaryService.weekHolidayPayCalculation(salary);
    }


}
