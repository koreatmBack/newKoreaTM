package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.formmail_vo.Survey;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.SurveyResponse;
import com.example.smsSpringTest.service.formMail_surveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : 설문조사 controller
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping({"/api/v1/formMail_survey", "/v1/formMail_survey"})
public class formMail_surveyController {

    private final formMail_surveyService formMailSurveyService;

    // 설문조사 추가
    @PostMapping("/addSurvey")
    public SurveyResponse addSurvey (@RequestBody Survey surv) throws Exception {
        return formMailSurveyService.addSurvey(surv);
    }

    // 설문조사 전체 조회
    @GetMapping("/surveyList")
    public SurveyResponse surveyList() throws Exception {
        return formMailSurveyService.surveyList();
    }

    // surveyType과 cid 일치하는 설문지 조회
    @PostMapping("/selectSurveyList")
    public SurveyResponse selectSurveyList(@RequestBody Survey surv) throws Exception {
        return formMailSurveyService.selectSurveyList(surv);
    }

    // 설문조사 수정
    @PutMapping("/updateSurvey")
    public ApiResponse updateSurvey(@RequestBody Survey surv) throws Exception {
        return formMailSurveyService.updateSurvey(surv);
    }

    // 설문조사 삭제
    @DeleteMapping("/deleteSurvey")
    public ApiResponse deleteSurvey(@RequestBody Survey surv) throws Exception {
        return formMailSurveyService.deleteSurvey(surv);
    }


}
