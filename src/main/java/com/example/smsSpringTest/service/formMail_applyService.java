package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.AdminMapper;
import com.example.smsSpringTest.mapper.ApplyMapper;
import com.example.smsSpringTest.mapper.CompanyMapper;
import com.example.smsSpringTest.model.*;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.ApplyResponse;
import com.example.smsSpringTest.model.response.InterviewMemoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private final AdminMapper adminMapper;
    private final CompanyMapper companyMapper;

    // 지원자 등록
    public ApiResponse addApply(Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {

            // 블랙 리스트 체크
            int blackListCheck = applyMapper.blackListCheck(apply);
            if(blackListCheck != 0) {
                apiResponse.setCode("E001");
                apiResponse.setMessage("블랙리스트 지원자입니다.");
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



            // 시리얼 넘버 생성
            String serialNumber = UUID.randomUUID().toString().substring(0, 8);
            int dupAidCheck = applyMapper.dupAidCheck(serialNumber);
            if(dupAidCheck != 0) {
                // 중복이라면 다시
                serialNumber = UUID.randomUUID().toString().substring(0, 8);
            }
            apply.setApplyId(serialNumber);

            // 면접 질의서 진행중인 고객사인지 체크
//            Apply findOne = applyMapper.findOne(apply);
            String cid = apply.getCid();
            Company comp = companyMapper.findOneComp(cid);
            if(comp.getSurveyProceed()) {
                // 면접 질의서 진행중임
                apply.setSurveyTarget(true);
            }

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

            Apply findOne = applyMapper.findOne(apply);
            if(apply.getInterviewTime() != null && findOne.getSurveyTarget()) {
                // 면접 시간이 변경 될 때, 지원 고객사가 면접 질의 고객사이고
                if(!StringUtils.hasText(findOne.getSurveyStatus())){
                    // 아직 미발송, 발송, 완료 상태가 아닐 때 미발송 추가해주기
                    apply.setSurveyStatus("미발송");
                }
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

            if(StringUtils.hasText(apply.getManagerId())){
                // 만약 매니저 id 값이 요청 값에 있으면
                FormMailAdmin fm = adminMapper.findOneAdmin(apply.getManagerId());
                applyResponse.setFormNo(fm.getFormNo());
                applyResponse.setRName(fm.getRName());
                applyResponse.setUserName(fm.getUserName());
                applyResponse.setRank(fm.getRank());

//                totalCount = applyMapper.applyListSameManagerIdCount(apply);
//                applyResponse.setApplyList(applyMapper.applyListSameManagerId(apply));
//                log.info(applyMapper.applyListSameManagerId(apply).toString());
            }

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

    // 면접일 갱신 버튼 클릭 -> 일괄 변경
    public ApiResponse updateAllInterview() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            // 만약 오늘 이전인데, 당일 면접이 남아 있으면
            int checkTodayInterview = applyMapper.checkTodayInterview();
            if(checkTodayInterview > 0) {
                apiResponse.setCode("E003");
                apiResponse.setMessage("[당일면접] 현황이 남아있습니다.");
                return apiResponse;
            }

            int updateAllInterview = applyMapper.updateAllInterview();
            if(updateAllInterview != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("면접일 갱신 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("면접일 갱신 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 지원자 삭제하기 (일괄 삭제까지 가능)
    public ApiResponse deleteApply(ApplyRequest applyRequest) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int deleteApply = applyMapper.deleteApply(applyRequest.getApplyIds());
            if(deleteApply != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("지원자 삭제 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("지원자 삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }

    // 면접 시간 설정에 따라 채용현황 자동 변환 (당일면접, 익일면접, 면접예정)
    // 면접 시간 선택 후 설정 버튼 클릭시 사용할 API임
    public ApiResponse editInterviewTime(Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String applyStatus = getInterviewStatus(apply.getInterviewTime());
            apply.setApplyStatus(applyStatus);
            int editInterviewTime = applyMapper.editInterviewTime(apply);
            if(editInterviewTime != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("면접 시간 설정 및 채용 현황 변경 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("면접 시간 설정 및 채용 현황 변경 실패");
            }
        } catch (Exception e) {
                apiResponse.setCode("E001");
                apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }

    //면접 질의서 url 저장
    public ApiResponse addSurvey(Apply apply) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int addSurvey = applyMapper.addSurvey(apply);
            if(addSurvey > 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("면접 질의서 url 저장 성공");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("면접 질의서 url 저장 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }

    // 면접 질의서 있는지 체킹
    public ApplyResponse findSurvey(Apply apply) throws Exception {
        ApplyResponse applyResponse = new ApplyResponse();
        try {
            String survey = applyMapper.survey(apply);
            if (StringUtils.hasText(survey)) {
                applyResponse.setCode("C000");
                applyResponse.setMessage("면접 질의서 있음");
                applyResponse.setSurvey(survey);
            } else {
                applyResponse.setCode("E001");
                applyResponse.setMessage("면접 질의서 없음");
            }
        } catch (Exception e) {
            applyResponse.setCode("E001");
            applyResponse.setMessage("Error!!!");
        }
        return applyResponse;
    }

    // 면접 메모 등록하는 API
    public ApiResponse addInterviewMemo(InterviewMemo im) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            // 시리얼 넘버 생성
            String serialNumber = UUID.randomUUID().toString().substring(0, 8);
            int dupMidCheck = applyMapper.dupMidCheck(serialNumber);
            if(dupMidCheck != 0) {
                // 중복이라면 다시
                serialNumber = UUID.randomUUID().toString().substring(0, 8);
            }
            im.setMid(serialNumber);
            int addInterviewMemo = applyMapper.addInterviewMemo(im);
            if(addInterviewMemo > 0) {
                // 테이블에 등록 성공
                // 지원자 테이블에도 추가하기
                int addAndUptInterviewMemo = applyMapper.addAndUptInterviewMemo(im);
                if(addAndUptInterviewMemo > 0) {
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("면접 메모 테이블 및 지원자 테이블 등록 성공");
                } else {
                    apiResponse.setCode("E001");
                    apiResponse.setMessage("면접 메모 테이블에는 성공 , 지원자 테이블에는 실패");
                }
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("모두 저장 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 면접 메모 조회하기
    public InterviewMemoResponse findAllnterviewMemo(InterviewMemo im) throws Exception {
        InterviewMemoResponse interviewMemoResponse = new InterviewMemoResponse();
        try {
            int page = im.getPage();
            int size = im.getSize();
            int offset = (page - 1) * size ;
            int totalCount = applyMapper.interviewMemoListCount();
            im.setOffset(offset);

            interviewMemoResponse.setInterviewMemoList(applyMapper.interviewMemoList(im));
            if(interviewMemoResponse.getInterviewMemoList() != null &&
            !interviewMemoResponse.getInterviewMemoList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                interviewMemoResponse.setTotalCount(totalCount);
                interviewMemoResponse.setTotalPages(totalPages);
                interviewMemoResponse.setCode("C000");
                interviewMemoResponse.setMessage("면접 메모 조회 성공");
            } else {
                interviewMemoResponse.setCode("E001");
                interviewMemoResponse.setMessage("면접 메모 조회 실패");
            }
        } catch (Exception e) {
            interviewMemoResponse.setCode("E001");
            interviewMemoResponse.setMessage("Error");
            log.info(e.getMessage());
        }
        return interviewMemoResponse;
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



}


