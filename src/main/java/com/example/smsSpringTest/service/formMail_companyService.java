package com.example.smsSpringTest.service;

import com.example.smsSpringTest.entity.formMail_company;
import com.example.smsSpringTest.mapper.CompanyMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * author : 신기훈
 * date : 2024-09-23
 * comment : 고객사 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class formMail_companyService {

    private final CompanyMapper companyMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    // 고객사 등록
    public ApiResponse addCompany(formMail_company comp) throws Exception{
        log.info("서비스 comp => " + comp);
        ApiResponse apiResponse = new ApiResponse();

        try{
                // cid 시리얼 넘버 생성 -> 중복처리 필요 x
                String serialNumber = UUID.randomUUID().toString();
                comp.setCid(serialNumber);
                int result = companyMapper.addComp(comp);
                log.info("result = " + result);
                if(result == 1){
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("고객사 등록이 완료되었습니다.");
                } else {
                    apiResponse.setCode("C001");
                    apiResponse.setMessage("고객사 등록 실패 !!");
                }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("데이터를 정확히 입력해주세요.");
            log.info(e.getMessage());
        }

        return apiResponse;
    }


    // 전체 고객사 조회
    public CompanyResponse companyList(Paging paging) throws Exception {

        CompanyResponse companyResponse = new CompanyResponse();

        // 캐시 키
        String cacheKey = "companyList_" + paging.getPage() + "_" + paging.getSize();
        long cacheTime = 1000 * 60 * 60; // 만료시간 1시간.

        // Redis에서 데이터가 있는지 확인
//        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//            // Redis에서 데이터 가져오기
//            companyResponse = (CompanyResponse) redisTemplate.opsForValue().get(cacheKey);
//            log.info("Redis에서 고객사 목록을 조회했습니다.");
//        } else {

            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = companyMapper.getCompanyListCount(); //전체 수

            paging.setOffset(offset);
            companyResponse.setCompanyList(companyMapper.companyList(paging));
            log.info(companyMapper.companyList(paging).toString());

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            if (companyResponse.getCompanyList() != null && !companyResponse.getCompanyList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                companyResponse.setTotalPages(totalPages);
                companyResponse.setCode("C001");
                companyResponse.setMessage("고객사 조회 성공");

                // Redis에 데이터 저장
//                redisTemplate.opsForValue().set(cacheKey, companyResponse, cacheTime, TimeUnit.MILLISECONDS);
//                log.info("고객사 목록을 Redis에 캐싱했습니다.");
            } else {
                companyResponse.setCode("E000");
                companyResponse.setMessage("고객사 조회 실패");
            }
//        }

        return companyResponse;
    }


    // 고객사 수정
    public ApiResponse updateCompany(formMail_company comp) throws Exception {

        ApiResponse apiResponse = new ApiResponse();
        log.info("comp = " + comp);
        try{
            int updateCompany = companyMapper.updateCompany(comp);

            if(updateCompany == 1) {
                String pattern = "companyList_*"; // 패턴 정의
                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기

                apiResponse.setCode("C001");
                apiResponse.setMessage("업데이트에 성공했습니다.");

                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
                log.info("Redis에서 고객사 목록 캐시를 삭제했습니다.");
            } else {
                apiResponse.setCode("C004");
                apiResponse.setMessage("업데이트에 실패했습니다.");
            }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("데이터를 다시 입력해주세요.");
            log.info(e.getMessage());
        }

        return apiResponse;
    }


    // 고객사 삭제
    public ApiResponse deleteCompany(formMail_company comp) throws Exception {

        ApiResponse apiResponse = new ApiResponse();
        log.info("comp = "  + comp);

        try {

        int deleteLog = companyMapper.deleteLog(comp); // 삭제 로그 db에 등록하기

        if(deleteLog == 1){
        int deleteCompany = companyMapper.deleteCompany(comp); // 삭제하기
            // 삭제 로그에 등록 성공하면
            if(deleteCompany == 1) {
                // 삭제 성공
                apiResponse.setCode("C001");
                apiResponse.setMessage("삭제 성공");

                String pattern = "companyList_*"; // 패턴 정의
                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기

                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
                log.info("Redis에서 고객사 목록 캐시를 삭제했습니다.");
            } else {
                // 삭제 실패
                apiResponse.setCode("C003");
                apiResponse.setMessage("삭제 실패");
            }

        } else {
            // 삭제 및 삭제 로그 등록 실패
            apiResponse.setCode("C005");
            apiResponse.setMessage("삭제 및 삭제 로그 등록 실패");
        }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("오류입니다.");
            log.info(e.getMessage());
        }

        return apiResponse;
    }


    // cid 일치하는 고객사 정보 반환
    public CompanyResponse findCompany(formMail_company comp) throws Exception {

        CompanyResponse companyResponse = new CompanyResponse();

        try {
            String cid = comp.getCid();
            if(cid == null ){
                companyResponse.setCode("C005");
                companyResponse.setMessage("고객사 조회 실패");
            } else {

                companyResponse.setCompany(companyMapper.findCompany(cid).get(0));
//            companyResponse.setCompanyList(companyMapper.findCompany(comp));
                companyResponse.setCode("C001");
                companyResponse.setMessage("고객사 조회 성공");
            }

        } catch (Exception e) {
            companyResponse.setCode("E001");
            companyResponse.setMessage("고객사 조회 실패");
            log.info(e.getMessage());
        }
        return companyResponse;
    }


}
