package com.example.smsSpringTest.service;

import com.example.smsSpringTest.config.S3Uploader;
import com.example.smsSpringTest.mapper.AdMapper;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.HolidayMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.ad.AdImageRequest;
import com.example.smsSpringTest.model.ad.AdNearInfo;
import com.example.smsSpringTest.model.ad.AdRequest;
import com.example.smsSpringTest.model.ad.fmAd;
import com.example.smsSpringTest.model.formMail_file;
import com.example.smsSpringTest.model.response.AdResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.S3UploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class formMail_adService {

    private final AdMapper adMapper;
    private final CommonMapper commonMapper;
    private final HolidayMapper holidayMapper;
    private final S3Uploader s3Uploader;
    private final MapService mapService;
//    private final RedisTemplate<String, Object> redisTemplate;

    // 광고 등록
    @Transactional
    public ApiResponse addAd(fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            log.info("ad = " + ad);
            String serialNumber = UUID.randomUUID().toString().substring(0, 8);
            log.info("serialNumber = " + serialNumber);

            ad.setAid(serialNumber);
            int addAd = adMapper.addAd(ad);
            if(addAd == 1){
//                ad.setTotalDay(totalDay);
                int totalDay = holidayMapper.totalDay(ad, serialNumber);
                log.info("total Day = " + totalDay);

                int addTotalDay = adMapper.addTotalDay(totalDay, serialNumber);

                // formmail_file에 url 등록 -> 광고 이미지에서 등록 실패시 삭제
                int dupImgurl = adMapper.dupImgUrl(ad);
                if(dupImgurl == 0){
                    int addUrl = commonMapper.addUrl(ad);
                }

                // getCoordinates로 위도 , 경도 구하고
                // 주변 정보 찾는 api 실행하여야함
                // 클라이언트에서 할 부분


                // 선택한 정보들 담기
                if(ad.getNearInfoList() != null) {
                    for(AdNearInfo near : ad.getNearInfoList()) {
                        near.setAid(ad.getAid());
                        adMapper.addNearInfo(near);
                    }
                }

                apiResponse.setCode("C000");
                apiResponse.setMessage("광고 등록 성공");
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("광고 등록 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 날짜 입력 -> total day 계산 API
    public Integer totalDay(fmAd ad) throws Exception {

        try {
            Integer onlyTotalDay = holidayMapper.onlyTotalDay(ad);

            return onlyTotalDay;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    // 주변 역 담는 api 비동기로 실행시킬 것
//    public ApiResponse chooseType(AdNearInfo near) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//
//        try {
//            // 디폴트 상태 N
//            near.setStatus("N");
//
//            if("선택".equals(near.getType())){
//                // 선택
//
//                // 값이 있는지 체크
//                int dupChkNearInfo = adMapper.dupChkNearInfo(near);
//                if(dupChkNearInfo == 0) {
//                    // 값이 없으면 새로 추가
//                    int addNearInfo = adMapper.addNearInfo(near);
//                } else {
//                    // 값이 있으면 수정
//
//                }
//                apiResponse.setCode("C000");
//                apiResponse.setMessage("선택");
//            } else {
//                // 선택해제
//
//                // 값이 있는지 체크
//                int dupChkNearInfo = adMapper.dupChkNearInfo(near);
//                if(dupChkNearInfo == 1) {
//                    // 값이 있으면 값 삭제
//                    int deleteOneNearInfo = adMapper.deleteOneNearInfo(near);
//                }
//                apiResponse.setCode("C000");
//                apiResponse.setMessage("선택 해제");
//            }
//        } catch (Exception e) {
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("Error!!!");
//            log.info(e.getMessage());
//        }
//
//        return apiResponse;
//    }


    // 광고 조회
    public AdResponse fmAdList(AdRequest adRequest) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {

            // 캐시 키
            String cacheKey = "fmAdList_" + adRequest.getPage() + "_" + adRequest.getSize();
            long cacheTime = 1000 * 60 * 60; // 만료시간 : 1시간

//            // Redis에 데이터가 있는지 확인
//            if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//                // Redis에서 데이터 가져오기
//                adResponse = (AdResponse) redisTemplate.opsForValue().get(cacheKey);
//                log.info("Redis에서 광고 목록을 조회했습니다.");
//            } else {

                int page = adRequest.getPage(); // 현재 페이지
                int size = adRequest.getSize(); // 한 페이지에 표시할 수
                int offset = (page - 1) * size; // 시작 위치
                int totalCount = adMapper.getFmAdListCount(adRequest); //전체 수
                adRequest.setOffset(offset);
                log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
                adResponse.setFindFmAdList(adMapper.findFmAdList(adRequest));
                if (adResponse.getFindFmAdList() != null && !adResponse.getFindFmAdList().isEmpty()) {
                    int totalPages = (int) Math.ceil((double) totalCount / size);
                    log.info("totalPages = " + totalPages);
                    adResponse.setTotalPages(totalPages);
                    adResponse.setCode("C000");
                    adResponse.setMessage("광고 목록 조회 성공");

//                    // Redis에 데이터 저장
//                    redisTemplate.opsForValue().set(cacheKey, adResponse, cacheTime, TimeUnit.MILLISECONDS);
//                    log.info("광고 목록을 Redis에 캐싱했습니다.");
                } else {
                    adResponse.setCode("E005");
                    adResponse.setMessage("광고 조회 실패");
                }
//            }
        } catch(Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("광고 조회 실패");
            log.error("광고 목록 조회 중 오류 발생: ", e);
        }
        return adResponse;
    }

    // 광고 수정
    public ApiResponse updateAd(fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
             List<fmAd> findOneAd = adMapper.findOneAd(ad);

            if(ad.getStartDate() != null && ad.getEndDate() != null){
                // 만약 둘 다 바꿀경우
                Integer newTotalDate = holidayMapper.onlyTotalDay(ad);
                log.info(String.valueOf(newTotalDate));
                adMapper.addTotalDay(newTotalDate, ad.getAid());
            } else if (ad.getStartDate() != null || ad.getEndDate() != null) {
                // 만약 start_date 혹은 end_date를 수정하면 평일 광고 일수도 수정해야지
                if(ad.getStartDate() != null){
                    // 만약 startDate를 수정한다면
                    LocalDate newStartDate = ad.getStartDate();
                    LocalDate originEndDate = findOneAd.get(0).getEndDate();
                    Integer newTotalDate = holidayMapper.newTotalDay(newStartDate, originEndDate);
                    adMapper.addTotalDay(newTotalDate, ad.getAid());
                    log.info(String.valueOf(newTotalDate));
                } else if(ad.getEndDate() != null){
                    // 만약 endDate를 수정한다면
                    LocalDate originStartDate = findOneAd.get(0).getStartDate();
                    LocalDate newEndDate = ad.getEndDate();
                    Integer newTotalDate = holidayMapper.newTotalDay(originStartDate, newEndDate);
                    adMapper.addTotalDay(newTotalDate, ad.getAid());
                    log.info(String.valueOf(newTotalDate));
                }

            }

//            // 지역 수정하면
//            if(ad.getAddress() != null){
////                // 지역 정보 전체 삭제
////                int deleteNearInfo = adMapper.deleteNearInfo(ad);
////                List<Double> getCoordinate = mapService.addCoordinates(ad.getAddress());
////                double x = getCoordinate.get(0);
////                double y = getCoordinate.get(1);
////                log.info("수정시 x = " + x + " y = " +  y);
////                ad.setX(x);
////                ad.setY(y);
////
////                // 주변 정보 저장하기
////                String university = null;
////                AdNearInfo adNearInfo = new AdNearInfo();
////                adNearInfo.setAid(ad.getAid());
////                List<MapVO> newNearInfo = mapService.findNearInfo(y, x);
////                for(MapVO mapVO : newNearInfo) {
////                    adNearInfo.setNearStation(mapVO.getSubway());
////                    adNearInfo.setDistance(mapVO.getDistance());
////                    adNearInfo.setDurationTime(mapVO.getDurationTime());
////                    university = mapVO.getUniversity();
////                    int addNearInfo = adMapper.addNearInfo(adNearInfo);
////                }
////                ad.setNearUniversity(university);
//
//
//                // 지역 정보 전체 삭제
//                int deleteNearInfo = adMapper.deleteNearInfo(ad);
//
//                // getCoordinates로 위도 , 경도 구하고
//                // 주변 정보 찾는 api 실행하여야함
//                // 클라이언트에서 할 부분
//
//
//                // 선택한 정보들 담기
//                if(ad.getNearInfoList() != null) {
//                    for(AdNearInfo near : ad.getNearInfoList()) {
//                        near.setAid(ad.getAid());
//                        adMapper.addNearInfo(near);
//                    }
//                }
//            }

            int updateAd = adMapper.updateAd(ad);
            log.info("updateAd = " + updateAd);
            if(updateAd == 1){
                apiResponse.setCode("C000");
                apiResponse.setMessage("광고 업데이트 성공");

//                String pattern = "fmAdList_*"; // 패턴 정의
//                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기
//
//                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
//                log.info("Redis에서 광고 목록 캐시를 삭제했습니다.");

                // 지역 수정하면
//                if(ad.getAddress() != null){
//                    // 지역 정보 전체 삭제
//                    int deleteNearInfo = adMapper.deleteNearInfo(ad);

                    // getCoordinates로 위도 , 경도 구하고
                    // 주변 정보 찾는 api 실행하여야함
                    // 클라이언트에서 할 부분

                    if (ad.getNearInfoList() != null && !ad.getNearInfoList().isEmpty()) {
                        // 기존 지역 정보 삭제
                        int deleteCount = adMapper.deleteNearInfo(ad);

                        // 새로운 지역 정보 삽입
                        for (AdNearInfo near : ad.getNearInfoList()) {
                            near.setAid(ad.getAid()); // aid 설정
                            adMapper.addNearInfo(near);
                        }
                    }
//                }
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("광고 업데이트 실패");
            }
        }catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 광고 삭제
    public ApiResponse deleteAd(fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int deleteAd = adMapper.deleteAd(ad);
            if(deleteAd == 1){
                apiResponse.setCode("C000");
                apiResponse.setMessage("광고 삭제 성공");

//                String pattern = "fmAdList_*"; // 패턴 정의
//                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기
//
//                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
//                log.info("Redis에서 광고 목록 캐시를 삭제했습니다.");
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("광고 삭제 실패");
            }
        }catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 폼메일용 start ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 폼메일용 광고 목록 전체 조회
    public AdResponse allAdList(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allAdListCount(); //전체 수
            paging.setOffset(offset);
            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setFmAdList(adMapper.allAdList(paging));
            if (adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("광고 목록 전체 조회 성공");
            } else {
                adResponse.setCode("E005");
                adResponse.setMessage("광고 목록 전체 조회 실패");
            }
        } catch (Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 폼메일용 adNum 일치하는 광고 전체 조회
    public AdResponse searchAdNumList(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setFmAdList(adMapper.searchAdNumList(ad));
            if (adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()) {
                adResponse.setCode("C000");
                adResponse.setMessage("광고 목록 조회 성공");
            } else {
                adResponse.setCode("E005");
                adResponse.setMessage("광고 조회 실패");
            }
        } catch (Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 폼메일용 aid 일치하는 광고 상세 조회
    public AdResponse findOneAd(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setFmAdList(adMapper.findOneAd(ad));
            if(adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("aid 일치하는 폼메일용 광고 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("aid 일치하는 폼메일용 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 폼메일용 title이 포함된 광고 조회
    public AdResponse searchTitleAd(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setFmAdList(adMapper.searchTitleAd(ad));
            if(adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("제목 포함하는 폼메일용 광고 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("제목 포함하는 폼메일용 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 폼메일용 hashtag 일치하는 광고 조회
    public AdResponse searchHashtagAd(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setFmAdList(adMapper.searchHashtagAd(ad));
            if(adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("hashtag 일치하는 폼메일용 광고 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("hashtag 일치하는 폼메일용 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }

        return adResponse;
    }

    // 폼메일용 sido (필수) , sigungu (필수아님) 일치하는 광고 찾기
    public AdResponse searchAddressAd(fmAd ad) throws Exception{
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setFmAdList(adMapper.searchAddressAd(ad));
            if (adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()) {
                adResponse.setCode("C000");
                adResponse.setMessage("주소 일치하는 폼메일용 광고 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("주소 일치하는 폼메일용 광고 조회 실패");
            }
        } catch (Exception e){
        adResponse.setCode("E001");
        adResponse.setMessage("ERROR");
        }

        return adResponse;
    }

    // 관리자페이지 -> 공고관리 조건들
    public AdResponse statusList(AdRequest ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = ad.getPage(); // 현재 페이지
            int size = ad.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.statusListCount(ad); //전체 수
            ad.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setFmAdList(adMapper.statusList(ad));
            log.info(adMapper.statusList(ad).toString());
            if(adResponse.getFmAdList() != null && !adResponse.getFmAdList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("조회 성공");
            } else {
                adResponse.setCode("C003");
                adResponse.setMessage("조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }

        return adResponse;
    }


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 폼메일용 end ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 잡사이트용 start ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 잡사이트용 광고 목록 전체 조회 (페이징 처리, 종료기간 끝난것 조회 x)
    public AdResponse allJobsiteList(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allJobsiteListCount(); //전체 수
            paging.setOffset(offset);
            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setJobSiteList(adMapper.allJobsiteList(paging));
            if (adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("광고 목록 전체 조회 성공");
            } else {
                adResponse.setCode("E005");
                adResponse.setMessage("광고 목록 전체 조회 실패");
            }
        } catch (Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }
        return adResponse;
    }

    // get 테스트용 -> 삭제 예정
    public AdResponse jobSiteListTest() throws Exception {
            AdResponse adResponse = new AdResponse();
        try {
            adResponse.setJobSiteList(adMapper.jobSiteListTest());
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("광고 목록 전체 조회 성공");
            } else {
                adResponse.setCode("E005");
                adResponse.setMessage("광고 목록 전체 조회 실패");
            }

        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 잡사이트용 grade에 따른 공고 조회 ( 종료기간 끝난것 조회 x )
    public AdResponse searchGradeJobsite(AdRequest ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = ad.getPage(); // 현재 페이지
            int size = ad.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.searchGradeJobsiteCount(ad); //전체 수
            ad.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setJobSiteList(adMapper.searchGradeJobsite(ad));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("유료 잡사이트 목록 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("유료 잡사이트 목록 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    public AdResponse searchTitleJobsite(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setJobSiteList(adMapper.searchTitleJobsite(ad));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("제목 포함하는 잡사이트 목록 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("제목 포함하는 잡사이트 목록 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 잡사이트용 aid 일치하는 광고 상세 조회 ( 종료기간 끝난것 조회 x )
    public AdResponse findOneJobsite(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setJobSiteList(adMapper.findOneJobsite(ad));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("aid 일치하는 잡사이트 광고 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("aid 일치하는 잡사이트 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 잡사이트용 등록일순으로 광고 조회
    public AdResponse orderByCreated(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {

            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allJobsiteListCount(); //전체 수
            paging.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);

            adResponse.setJobSiteList(adMapper.orderByCreated(paging));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("등록일 최신순으로 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("등록일 최신순으로조회 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }

        return adResponse;
    }

    // 잡사이트용 급여 높은 순으로 광고 조회
    public AdResponse orderByMaxPay(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allJobsiteListCount(); //전체 수
            paging.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);

            adResponse.setJobSiteList(adMapper.orderByMaxPay(paging));

            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("급여 높은 순으로 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("급여 높은 순으로 조회 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }

        return adResponse;
    }


    // 잡사이트용 근무일수 적은순으로 광고 조회
    public AdResponse orderByWorkDay(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allJobsiteListCount(); //전체 수
            paging.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setJobSiteList(adMapper.orderByWorkDay(paging));

            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("근무일수 적은순으로 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("근무일수 적은순으로 조회 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }

        return adResponse;
    }


    // 잡사이트용 근무시간 짧은 순으로 광고 조회
    public AdResponse orderByWorkTime(Paging paging) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.allJobsiteListCount(); //전체 수
            paging.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
            adResponse.setJobSiteList(adMapper.orderByWorkTime(paging));

            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("근무시간 짧은 순으로 조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("근무시간 짧은 순으로 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }

        return adResponse;
    }


    // aid가 일치하는 고객사 정보 반환 + 정보로 찾은 cid -> user 정보 까지 반환
    public AdResponse findCompanyAndUser(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();
        try {

            String cid = adMapper.findCid(ad);

            adResponse.setFindCompanyAndUserList(adMapper.findCompanyAndUser(cid));
            log.info(adMapper.findCompanyAndUser(cid).toString());
            if(adResponse.getFindCompanyAndUserList() != null && !adResponse.getFindCompanyAndUserList().isEmpty()){
                adResponse.setCode("C000");
                adResponse.setMessage("조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }
        return adResponse;
    }

// 11-29 ~
//    // 일단 정렬 , 등록일 조건 없이 시/도, 시,군,구 / 동,읍,면에 대해서만
//    public AdResponse selectByRegions(AdRequest ad) throws Exception {
//        AdResponse adResponse = new AdResponse();
//        log.info(String.valueOf(ad));
//        try {
//            int page = ad.getPage(); // 현재 페이지
//            int size = ad.getSize(); // 한 페이지에 표시할 수
//            int offset = (page - 1) * size; // 시작 위치
////            int totalCount = adMapper.allJobsiteListCount(); //전체 수
//            ad.setOffset(offset);
//
////            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
//            log.info("page = " + page + " size = " + size + " offset = " + offset);
//            adResponse.setJobSiteList(adMapper.selectByRegions(ad));
//
//            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
////                adResponse.setTotalPages(totalCount);
//                adResponse.setCode("C000");
//                adResponse.setMessage("지역 선택 조회 성공");
//            } else {
//                adResponse.setCode("E004");
//                adResponse.setMessage("지역 선택 조회 실패");
//            }
//        } catch (Exception e){
//            adResponse.setCode("E001");
//            adResponse.setMessage("ERROR");
//            log.info(e.getMessage());
//        }
//
//        return adResponse;
//    }


    // 등록일, 정렬 조건 없이 시/도, 시/군/구 , 동/읍/면에 대해서만
    // 정렬 조건 추가
    public AdResponse selectByRegionsSort(AdRequest ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            int page = ad.getPage(); // 현재 페이지
            int size = ad.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adMapper.selectByRegionsSortCount(ad); //전체 수
            ad.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);

            if("최신등록순".equals(ad.getSortType())){
                ad.setSortType("최신등록순");
            } else if("시급높은순".equals(ad.getSortType())) {
                ad.setSalaryType("시급");
                ad.setSortType("salary");
                // 샐러리 타입 (시급, 주급, 일급 ,월급, 연봉)
                // 샐러리 타입 조건으로 주고, salary DESC; 하면 될듯?
            } else if("주급높은순".equals(ad.getSortType())){
                ad.setSalaryType("주급");
                ad.setSortType("salary");
            } else if("일급높은순".equals(ad.getSortType())){
                ad.setSalaryType("일급");
                ad.setSortType("salary");
            } else if("월급높은순".equals(ad.getSortType())){
                ad.setSalaryType("월급");
                ad.setSortType("salary");
            } else if("연봉높은순".equals(ad.getSortType())){
                ad.setSalaryType("연봉");
                ad.setSortType("salary");
            } else if("건별높은순".equals(ad.getSortType())){
                // 나중에 건 별로 공고 등록 추가할 때 사용할 코드
                ad.setSalaryType("건별");
                ad.setSortType("salary");
            }
            adResponse.setJobSiteList(adMapper.selectByRegionsSort(ad));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                adResponse.setTotalPages(totalPages);
                adResponse.setCode("C000");
                adResponse.setMessage("조회 성공");
            } else {
                adResponse.setCode("E004");
                adResponse.setMessage("조회 실패");
            }
        } catch (Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
            log.info(e.getMessage());
        }

        return adResponse;
    }

//    // 잡사이트용 급구 공고 (마감기한 3일이하로 남은 애들)
//    public AdResponse hurriedAdList(AdRequest ad) throws Exception {
//        AdResponse adResponse = new AdResponse();
//
//        try {
//            int page = ad.getPage(); // 현재 페이지
//            int size = ad.getSize(); // 한 페이지에 표시할 수
//            int offset = (page - 1) * size; // 시작 위치
//            int totalCount = adMapper.hurriedAdListCount(ad); //전체 수
//            ad.setOffset(offset);
//
//            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
//
//            if("최신등록순".equals(ad.getSortType())){
//                ad.setSortType("최신등록순");
//            } else if("시급높은순".equals(ad.getSortType())) {
//                ad.setSalaryType("시급");
//                ad.setSortType("salary");
//                // 샐러리 타입 (시급, 주급, 일급 ,월급, 연봉)
//                // 샐러리 타입 조건으로 주고, salary DESC; 하면 될듯?
//            } else if("주급높은순".equals(ad.getSortType())){
//                ad.setSalaryType("주급");
//                ad.setSortType("salary");
//            } else if("일급높은순".equals(ad.getSortType())){
//                ad.setSalaryType("일급");
//                ad.setSortType("salary");
//            } else if("월급높은순".equals(ad.getSortType())){
//                ad.setSalaryType("월급");
//                ad.setSortType("salary");
//            } else if("연봉높은순".equals(ad.getSortType())){
//                ad.setSalaryType("연봉");
//                ad.setSortType("salary");
//            } else if("건별높은순".equals(ad.getSortType())){
//                // 나중에 건 별로 공고 등록 추가할 때 사용할 코드
//                ad.setSalaryType("건별");
//                ad.setSortType("salary");
//            }
//            adResponse.setJobSiteList(adMapper.hurriedAdList(ad));
//            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
//                int totalPages = (int) Math.ceil((double) totalCount / size);
//                log.info("totalPages = " + totalPages);
//                adResponse.setTotalPages(totalPages);
//                adResponse.setCode("C000");
//                adResponse.setMessage("조회 성공");
//            } else {
//                adResponse.setCode("E004");
//                adResponse.setMessage("조회 실패");
//            }
//        } catch (Exception e){
//            adResponse.setCode("E001");
//            adResponse.setMessage("ERROR");
//            log.info(e.getMessage());
//        }
//
//        return adResponse;
//    }


    // aid 일치하는 주변 역 정보들 추출
    public AdResponse nearInfoList(AdNearInfo near) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
         adResponse.setNearInfoList(adMapper.nearInfoList(near));
         if(adResponse.getNearInfoList() != null && !adResponse.getNearInfoList().isEmpty()) {
             // 값이 있다면
             adResponse.setCode("C000");
             adResponse.setMessage("주변 역 정보 조회 성공");
         } else {
             adResponse.setCode("C003");
             adResponse.setMessage("주변 역 정보 조회 실패");
         }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("Error!!!");
        }

        return adResponse;
    }








// ------------------ 광고 테이블 끝 -----------------


//    // S3에 이미지 파일 업로드 ( db에 저장은 X) --> 파일 여러개 ver.
//    public S3UploadResponse S3Upload(MultipartFile[] multipartFiles) throws Exception {
//
//        S3UploadResponse s3UploadResponse = new S3UploadResponse();
//        List<String> uploadedUrls = new ArrayList<>();
//        try {
//            for (MultipartFile multipartFile : multipartFiles) {
//
//                    String imageUrl = s3Uploader.upload(multipartFile, "images");
//                    log.info("multipartFile = " + multipartFile);
//                    log.info("imageUrl = " + imageUrl);
////            int S3UploadFile = s3UploadMapper.S3UploadFile(imageUrl);
//
//                    // url 상대경로로 변환
//                    String formatImageUrl = formatImageUrl(imageUrl);
//                    log.info("formatImageUrl = " + formatImageUrl);
////                s3UploadResponse.setUrl(formatImageUrl);
//                    uploadedUrls.add(formatImageUrl);
//                    s3UploadResponse.setCode("C001");
//                    s3UploadResponse.setMessage("업로드 성공");
//
//            } // for 반복문
//            log.info("urlList = " + uploadedUrls);
//            s3UploadResponse.setUrlList(uploadedUrls);
//            s3UploadResponse.setCode("C001");
//            s3UploadResponse.setMessage("업로드 성공");
//        } catch (Exception e){
//            s3UploadResponse.setCode("E001");
//            s3UploadResponse.setMessage("업로드 실패");
//            log.info(e.getMessage());
//        }
//        return s3UploadResponse;
//    }

    // S3에 이미지 파일 업로드 ( db에 저장은 X) --> 파일 1개 ver.
    public S3UploadResponse S3Upload(MultipartFile multipartFile) throws Exception {

        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        try {
                String imageUrl = s3Uploader.upload(multipartFile, "images");
                log.info("multipartFile = " + multipartFile);
                log.info("imageUrl = " + imageUrl);
//            int S3UploadFile = s3UploadMapper.S3UploadFile(imageUrl);

                // url 상대경로로 변환
                String formatImageUrl = formatImageUrl(imageUrl);
                log.info("formatImageUrl = " + formatImageUrl);
                s3UploadResponse.setUrl(formatImageUrl);
                s3UploadResponse.setCode("C000");
                s3UploadResponse.setMessage("S3에 업로드 성공");
        } catch (Exception e){
            s3UploadResponse.setCode("E001");
            s3UploadResponse.setMessage("업로드 실패");
            log.info(e.getMessage());
        }
        return s3UploadResponse;
    }

//    // 광고 이미지 DB에 저장 . 실패시 S3에 업로드된 해당 파일 삭제
//    public ApiResponse addAdImg(fmAdImage adImage) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//        String url = adImage.getPath();
//        try {
//            String serialNumber = UUID.randomUUID().toString();
//            adImage.setAiId(serialNumber);
//            int addAdImg = adMapper.addAdImg(adImage);
//            log.info(adImage.toString());
//
//            if(addAdImg == 1){
////                int addUrl = commonMapper.addUrl(url);
////                log.info("addUrl : " + addUrl);
//                apiResponse.setCode("C001");
//                apiResponse.setMessage("이미지 업로드 최종 성공");
//
//            } else {
//                int deleteUrl = commonMapper.deleteUrl(url);
//                apiResponse.setCode("C005");
//                apiResponse.setMessage("이미지 업로드 실패, S3 해당 파일 삭제");
//                s3Uploader.deleteFile(adImage.getPath());
//            }
//        }catch (Exception e){
//            int deleteUrl = commonMapper.deleteUrl(url);
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("이미지 업로드 실패, S3 해당 파일 삭제");
//            s3Uploader.deleteFile(adImage.getPath());
//            log.info(e.getMessage());
//        }
//        return apiResponse;
//    }

//    // 광고 이미지 DB 삭제 및 S3에서도 삭제
//    public ApiResponse deleteAdImg(fmAdImage adImage) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//
//        try {
//            log.info(adImage.toString());
//            //db속 이미지 path 갖고오기
//            String path = adMapper.getPath(adImage);
//            log.info("path = " + path);
//            if(path != null) {
//                int deleteAdImg = adMapper.deleteAdImg(adImage);
//                if (deleteAdImg == 1) {
//                    // fa_file 테이블의 url row도 삭제
//                    int deleteUrl = commonMapper.deleteUrl(path);
//                    s3Uploader.deleteFile(path);
//                    apiResponse.setCode("C001");
//                    apiResponse.setMessage("이미지 삭제 및 DB 삭제 성공");
//                } else {
//                    apiResponse.setCode("C004");
//                    apiResponse.setMessage("이미지 삭제 및 DB 삭제 실패");
//                }
//            }
//        } catch(Exception e) {
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("ERROR!");
//            log.info(e.getMessage());
//        }
//        return apiResponse;
//    }

    // 광고 이미지 전체 조회
    public AdResponse fmAdImageList(AdImageRequest adImage) throws Exception {
        AdResponse adResponse = new AdResponse();

        int page = adImage.getPage(); // 현재 페이지
        int size = adImage.getSize(); // 한 페이지에 표시할 수
        int offset = (page - 1) * size; // 시작 위치
        int totalCount = adMapper.getFmAdImageListCount(adImage); //전체 수
        adImage.setOffset(offset);
        log.info(adMapper.fmAdImageList(adImage).toString());
        log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);
        adResponse.setFmAdImageList(adMapper.fmAdImageList(adImage));
        if(adResponse.getFmAdImageList() != null && !adResponse.getFmAdImageList().isEmpty()){
            int totalPages = (int) Math.ceil((double) totalCount / size);
            log.info("totalPages = " + totalPages);
            adResponse.setTotalPages(totalPages);
            adResponse.setCode("C000");
            adResponse.setMessage("고객사 조회 성공");
        } else {
            adResponse.setCode("E001");
            adResponse.setMessage("고객사 조회 실패");
        }
        return adResponse;
    }



    // 파일 url 상대경로
    public static String formatImageUrl(String url) {
        // 'com' 다음 인덱스 찾기
        int index = url.indexOf(".com");
        if (index != -1) {
            // 'com' 다음 인덱스 + 4 ('.com' 길이)부터 반환
            return url.substring(index + 4);
        }
        return ""; // 유효한 URL이 아니면 빈 문자열 반환
    }

    // formmail_file db에서 url 일치하는 데이터 삭제하기
    public ApiResponse deleteFile(formMail_file file) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String url = file.getAdImg();
            int deleteFile = adMapper.deleteFile(file);
            if(deleteFile == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("삭제 완료");
                s3Uploader.deleteFile(url);
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("삭제 실패");
            }
        } catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
        }
        return apiResponse;
    }

}
