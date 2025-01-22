package com.example.smsSpringTest.controller.cafecon;

import com.example.smsSpringTest.model.cafecon.Deposit;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.service.cafecon.CafeconDepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2025-01-22
 * comment : 카페콘 입금 controller
 */
@RestController
@RequestMapping({"/api/v1/cafecon/deposit", "/v1/cafecon/deposit"})
@Slf4j
@RequiredArgsConstructor
public class CafeconDepositController {

    private final CafeconDepositService cafeconDepositService;

    // 입금 정보 등록하기
    @PostMapping("/add")
    public ApiResponse addDepositInfo(@RequestBody Deposit deposit) throws Exception {
        return cafeconDepositService.addDepositInfo(deposit);
    }

    // 상태 변경하기 (보류확인 or 충전완료)
    @PutMapping("/change/status")
    public ApiResponse changeStatus(@RequestBody Deposit deposit) throws Exception {
        return cafeconDepositService.changeStatus(deposit);
    }

}
