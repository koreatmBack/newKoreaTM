package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.ApplyMapper;
import com.example.smsSpringTest.model.Apply;
import com.example.smsSpringTest.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 지원자 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class formMail_applyService {

    private final ApplyMapper applyMapper;

    // 지원자 등록
    public ApiResponse addApply(Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            // cid 시리얼 넘버 생성 -> 중복처리 필요 x
            String serialNumber = UUID.randomUUID().toString();
            apply.setApplyId(serialNumber);
            int addApply = applyMapper.addApply(apply);
            if(addApply == 1) {
                apiResponse.setCode("C001");
                apiResponse.setMessage("지원자 등록 성공");
            } else {
                apiResponse.setCode("C004");
                apiResponse.setMessage("지원자 등록 실패");
            }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 지원자 수정
    public ApiResponse updateApply(Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try{

            int updateApply = applyMapper.updateApply(apply);
            if(updateApply == 1) {
                apiResponse.setCode("C001");
                apiResponse.setMessage("지원자 수정 성공");
            } else {
                apiResponse.setCode("C004");
                apiResponse.setMessage("지원자 수정 실패");
            }

        }catch(Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

}
