package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.Apply;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.ApplyResponse;
import com.example.smsSpringTest.service.formMail_applyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 지원자 controller
 */

@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/v1/formMail_apply", "/v1/formMail_apply"})
@Slf4j
public class formMail_applyController {

    private final formMail_applyService formMailApplyService;

    // 지원자 등록
    @PostMapping("/addApply")
    public ApiResponse addApply(@RequestBody Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailApplyService.addApply(apply);
        return apiResponse;
    }

    // 지원자 수정
    @PutMapping("/updateApply")
    public ApiResponse updateApply(@RequestBody Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse = formMailApplyService.updateApply(apply);
        return apiResponse;
    }

    // 지원자 전체 조회
    @PostMapping("/applyList")
    public ApplyResponse applyList(@RequestBody Paging paging) throws Exception {
        ApplyResponse applyResponse = new ApplyResponse();
        applyResponse = formMailApplyService.applyList(paging);
        return applyResponse;
    }

    // 지원자 한명 조회 (apply_id 일치하는)
    @PostMapping("/findOneApply")
    public ApplyResponse findOneApply(@RequestBody Apply apply) throws Exception {
        ApplyResponse applyResponse = new ApplyResponse();
        applyResponse = formMailApplyService.findOneApply(apply);
        return applyResponse;
    }
}
