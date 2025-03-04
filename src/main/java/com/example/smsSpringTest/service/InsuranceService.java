package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.InsuranceMapper;
import com.example.smsSpringTest.model.formmail_vo.Insurance;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.formmail.InsuranceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author : 신기훈
 * date : 2025-03-04
 * comment : 보험 관련 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InsuranceService {

    private final InsuranceMapper insuranceMapper;

    // 보험 타입, 보험명 등록하기
    public ApiResponse addInsurance(Insurance ins) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String type = ins.getType();
            int addIns = 0;
            for(String n : ins.getNames()) {
                if(insuranceMapper.dupInsCheck(type,n) == 0) {
                    // 중복 아닐때 값 넣기
                    addIns = insuranceMapper.addInsurance(type, n);
                }
            }
            if(addIns != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("등록 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("등록 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }

    // 보험 정보 하나 삭제
    public ApiResponse deleteInsurance(Insurance ins) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int deleteInsurance = insuranceMapper.deleteInsurance(ins);
            if(deleteInsurance != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("삭제 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // name 검색 기능. 포함된 것
    public InsuranceResponse searchName(Insurance ins) throws Exception {
        InsuranceResponse insuranceResponse = new InsuranceResponse();
        try {
            insuranceResponse.setInsuranceList(insuranceMapper.searchName(ins));
            if(insuranceResponse.getInsuranceList() != null && !insuranceResponse.getInsuranceList().isEmpty()) {
                insuranceResponse.setCode("C000");
                insuranceResponse.setMessage("검색 성공");
            } else {
                insuranceResponse.setCode("E001");
                insuranceResponse.setMessage("검색 실패");
            }
        } catch (Exception e) {
            insuranceResponse.setCode("E001");
            insuranceResponse.setMessage("Error!!!");
        }
        return insuranceResponse;
    }

}
