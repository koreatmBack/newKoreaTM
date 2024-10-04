package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.SurveyMapper;
import com.example.smsSpringTest.model.Survey;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.SurveyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : 설문조사 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class formMail_surveyService {

    private final SurveyMapper surveyMapper;

    // 설문조사 추가
    public SurveyResponse addSurvey(Survey surv) throws Exception {

        SurveyResponse surveyResponse = new SurveyResponse();

        try {
            log.info("survey = " + surv);
            List<Survey> surveyList = surveyMapper.selectSurveyList(surv);

            // 설문 타입, cid 일치하는 설문 있으면 새로 등록 x , survId 반환
            if(surveyList != null && !surveyList.isEmpty()){
                // 이미 값이 있을때
                int survId = surveyList.get(0).getSurvId();
//                // 넣어놓은 리스트 초기화
//                surveyResponse = new SurveyResponse();

                surveyResponse.setSurvId(survId);
                surveyResponse.setCode("C002");
                surveyResponse.setMessage("이미 같은 설문(cid , type)이 존재합니다.");
            } else {
                // 값이 없을때 -> 신규 등록
                int addSurvey = surveyMapper.addSurvey(surv);
                if(addSurvey == 1){
                    surveyResponse.setCode("C001");
                    surveyResponse.setMessage("설문조사 등록 성공");
                } else {
                    surveyResponse.setCode("C004");
                    surveyResponse.setMessage("설문조사 등록 실패");
                }
            }

        } catch (Exception e) {
            surveyResponse.setCode("E001");
            surveyResponse.setMessage("Error");
            log.error(e.getMessage());
        }

        return surveyResponse;
    }

    // 설문조사 전체 조회
    public SurveyResponse surveyList() throws Exception {

        SurveyResponse surveyResponse = new SurveyResponse();

        try{

            log.info(surveyMapper.surveyList().toString());

            surveyResponse.setSurveyList(surveyMapper.surveyList());

            surveyResponse.setCode("C001");
            surveyResponse.setMessage("설문조사 조회 성공");
        }catch (Exception e) {
            surveyResponse.setCode("E001");
            surveyResponse.setMessage("설문조사 조회 실패");
        }

        return surveyResponse;
    }

    // survId와 cid 일치하는 설문지 조회
    public SurveyResponse selectSurveyList(Survey surv) throws Exception {
        SurveyResponse surveyResponse = new SurveyResponse();

        try{
            List<Survey> surveys = surveyMapper.selectSurveyList(surv);

            if (surveys == null || surveys.isEmpty()) {
                throw new Exception(); // 예외를 발생시킴
            }
            log.info(surveyMapper.selectSurveyList(surv).toString());
            surveyResponse.setSurveyList(surveyMapper.selectSurveyList(surv));
            surveyResponse.setCode("C001");
            surveyResponse.setMessage("설문조사 조회 성공");
        }catch (Exception e) {
            surveyResponse.setCode("E001");
            surveyResponse.setMessage("설문조사 조회 실패");
            log.info("에러 : " + e.getMessage());
        }

        return surveyResponse;
    }

    // 설문조사 수정
    public ApiResponse updateSurvey(Survey surv) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {
            int updateSurvey = surveyMapper.updateSurvey(surv);
            if(updateSurvey == 1) {
                apiResponse.setCode("C001");
                apiResponse.setMessage("설문조사 업데이트 완료");
            } else {
                apiResponse.setCode("C001");
                apiResponse.setMessage("설문조사 업데이트 실패");
            }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
            log.error(e.getMessage());
        }

        return apiResponse;
    }


    // 설문조사 삭제
    public ApiResponse deleteSurvey(Survey surv) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int deleteSurvey = surveyMapper.deleteSurvey(surv);
            if(deleteSurvey == 1) {
                apiResponse.setCode("C001");
                apiResponse.setMessage("설문조사 삭제 완료");
            } else {
                apiResponse.setCode("C001");
                apiResponse.setMessage("설문조사 삭제 실패");
            }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
            log.error(e.getMessage());
        }

        return apiResponse;
    }


    // 설문조사의 img S3에 업로드




}
