package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.Apply;
import com.example.smsSpringTest.model.ApplyRequest;
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
        return formMailApplyService.addApply(apply);
    }

    // 지원자 수정
    @PutMapping("/updateApply")
    public ApiResponse updateApply(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.updateApply(apply);
    }

    // 지원자 전체 조회
    @PostMapping("/applyList")
    public ApplyResponse applyList(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.applyList(apply);
    }

    // 지원자 한명 조회 (apply_id 일치하는)
    @PostMapping("/findOneApply")
    public ApplyResponse findOneApply(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.findOneApply(apply);
    }

    // 지원자 입력시, 지원자 이력 반환
    @PostMapping("/find/history")
    public ApplyResponse applyHistory(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.applyHistory(apply);
    }

    // 지원자 채용 현황 변경 버튼 클릭 -> 변경
    @PutMapping("/update/status")
    public ApiResponse updateApplyStatus(@RequestBody ApplyRequest applyRequest) throws Exception {
        return formMailApplyService.updateApplyStatus(applyRequest);
    }

    // 면접일 갱신 버튼 클릭 -> 일괄 변경
    @PutMapping("/update/all/interviewTime")
    public ApiResponse updateAllInterview() throws Exception {
        return formMailApplyService.updateAllInterview();
    }

    // 지원자 삭제하기 (일괄 삭제까지 가능)
    @DeleteMapping("/delete")
    public ApiResponse deleteApply(@RequestBody ApplyRequest applyRequest) throws Exception {
        return formMailApplyService.deleteApply(applyRequest);
    }

    // 면접 시간 설정에 따라 채용현황 자동 변환 (당일면접, 익일면접, 면접예정)
    // 면접 시간 수정 및 면접 시간 선택 후 설정 버튼 클릭시 사용할 API임
    @PutMapping("/edit/interviewTime")
    public ApiResponse editInterviewTime(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.editInterviewTime(apply);
    }

    //면접 질의서 url 저장
    @PutMapping("/add/survey")
    public ApiResponse addSurvey(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.addSurvey(apply);
    }

    // 면접 질의서 있는지 체킹
    @PostMapping("/find/survey")
    public ApplyResponse findSurvey(@RequestBody Apply apply) throws Exception {
        return formMailApplyService.findSurvey(apply);
    }

}
