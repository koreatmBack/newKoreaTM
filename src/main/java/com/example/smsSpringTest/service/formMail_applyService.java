package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.ApplyMapper;
import com.example.smsSpringTest.model.Apply;
import com.example.smsSpringTest.model.ApplyRequest;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.ApplyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

            // 블랙 리스트 체크
            int blackListCheck = applyMapper.blackListCheck(apply);
            if(blackListCheck != 0) {
                apiResponse.setCode("E001");
                apiResponse.setMessage("블랙리스트입니다.");
                return apiResponse;
            }

            apply.setApplyStatus("신규DB");

            // 지원이력 체크
            int dupApplyCheck = applyMapper.dupApplyCheck(apply);
            if(dupApplyCheck != 0) {
                apply.setApplyStatus("중복지원");
            }

            // 중복 DB 체크

            // 나이제한 , 성별제한 체크



            // 시리얼 넘버 생성 -> 중복처리 필요 x
            String serialNumber = UUID.randomUUID().toString();
            apply.setApplyId(serialNumber);
            int addApply = applyMapper.addApply(apply);
            if(addApply == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("지원자 등록 성공");
            } else {
                apiResponse.setCode("E004");
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

            if(apply.getInterviewTime() != null && apply.getApplyStatus() == null) {
                // 면접시간이 변경되며 채용현황은 변경하지 않았을때
                // 자동으로 당일면접, 익일면접, 면접예정으로 변환
                String applyStatus = getInterviewStatus(apply.getInterviewTime());
                apply.setApplyStatus(applyStatus);
            }

            int updateApply = applyMapper.updateApply(apply);
            if(updateApply == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("지원자 수정 성공");
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("지원자 수정 실패");
            }
        }catch(Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 오늘 날짜와 비교하여 당일면접, 익일면접, 면접예정 체크 후 반환
    public static String getInterviewStatus(String applyDateStr) {
        // 날짜 형식 지정 (문자열을 날짜로 변환)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime applyDateTime = LocalDateTime.parse(applyDateStr, formatter);

        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        LocalDate applyDate = applyDateTime.toLocalDate();

        // 면접 상태 판별
        if (applyDate.isEqual(today)) {
            return "당일면접";
        } else if (applyDate.isEqual(today.plusDays(1))) {
            return "익일면접";
        } else if (applyDate.isAfter(today)) {
            return "면접예정";
        }
        return "기타"; // 혹시 모를 예외 처리
    }

    // 지원자 전체 조회
    public ApplyResponse applyList(Apply apply) throws Exception {
        ApplyResponse applyResponse = new ApplyResponse();

        try {
            int page = apply.getPage(); // 현재 페이지
            int size = apply.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = applyMapper.applyListCount(apply); //전체 수
            apply.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            applyResponse.setApplyList(applyMapper.applyList(apply));

            if(applyResponse.getApplyList() != null && !applyResponse.getApplyList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size );
                applyResponse.setTotalPages(totalPages);
                applyResponse.setTotalCount(totalCount);
                applyResponse.setCode("C000");
                applyResponse.setMessage("지원자 전제 조회 성공");
            } else {
                applyResponse.setCode("E004");
                applyResponse.setMessage("지원자 전제 조회 실패");
            }
        } catch (Exception e) {
            applyResponse.setCode("E001");
            applyResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }

        return applyResponse;
    }


    // 지원자 한명 조회 (apply_id 일치하는)
    public ApplyResponse findOneApply(Apply apply) throws Exception {

        ApplyResponse applyResponse = new ApplyResponse();

        try {
            applyResponse.setApplyList(applyMapper.findOneApply(apply));
            if(applyResponse.getApplyList() != null && !applyResponse.getApplyList().isEmpty()){
                applyResponse.setCode("C000");
                applyResponse.setMessage("지원자 한명 조회 성공");
            } else {
                applyResponse.setCode("E004");
                applyResponse.setMessage("지원자 한명 조회 실패");
            }
        } catch (Exception e) {
            applyResponse.setCode("E001");
            applyResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }

        return applyResponse;
    }

    // 지원자 입력시, 지원자 이력 반환
    public ApplyResponse applyHistory(Apply apply) throws Exception {
        ApplyResponse applyResponse = new ApplyResponse();
        try {
            applyResponse.setApplyList(applyMapper.applyHistory(apply));
            if(applyResponse.getApplyList() != null && !applyResponse.getApplyList().isEmpty()) {
                applyResponse.setCode("C000");
                applyResponse.setMessage("지원자 이력 조회 성공");
            } else {
                applyResponse.setCode("E001");
                applyResponse.setMessage("지원자 이력 조회 실패");
            }
        } catch (Exception e) {
                applyResponse.setCode("E001");
                applyResponse.setMessage("Error!!!");
        }
        return applyResponse;
    }

    // 지원자 채용 현황 변경 버튼 클릭 -> 변경
    // 일괄 수정도 가능
    public ApiResponse updateApplyStatus(@RequestBody ApplyRequest applyRequest) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int updateApplyStatus = applyMapper.updateApplyStatus(applyRequest.getApplyStatus(), applyRequest.getApplyIds());
            if(updateApplyStatus != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("채용 현황 변경 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("채용 현황 변경 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }



}


