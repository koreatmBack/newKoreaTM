package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.formmail_vo.Insurance;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.formmail.InsuranceResponse;
import com.example.smsSpringTest.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2025-03-04
 * comment : 보험 controller
 */

@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/v1/formMail/insurance", "/v1/formMail/insurance"})
@Slf4j
public class InsuranceController {

    private final InsuranceService insuranceService;

    // 보험 정보 등록
    @PostMapping("/add")
    public ApiResponse addInsurance(@RequestBody Insurance ins) throws Exception {
        return insuranceService.addInsurance(ins);
    }

    // 보험 정보 하나 삭제
    @DeleteMapping("/delete")
    public ApiResponse deleteInsurance(@RequestBody Insurance ins) throws Exception {
        return insuranceService.deleteInsurance(ins);
    }

    // name 검색 기능. 포함된 것
    @PostMapping("/search/name")
    public InsuranceResponse searchName(@RequestBody Insurance ins) throws Exception {
        return insuranceService.searchName(ins);
    }
}
