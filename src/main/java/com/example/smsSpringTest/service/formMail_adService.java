package com.example.smsSpringTest.service;

import com.example.smsSpringTest.config.S3Uploader;
import com.example.smsSpringTest.mapper.AdMapper;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.HolidayMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.ad.AdImageRequest;
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
//    private final RedisTemplate<String, Object> redisTemplate;

    // 광고 등록
    @Transactional
    public ApiResponse addAd(fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            log.info("ad = " + ad);
            String serialNumber = UUID.randomUUID().toString();
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
                apiResponse.setCode("C001");
                apiResponse.setMessage("광고 등록 성공");
            } else {
                apiResponse.setCode("C004");
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

        } catch (Exception e) {

        }
        Integer onlyTotalDay = holidayMapper.onlyTotalDay(ad);

        return onlyTotalDay;
    }

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
                    adResponse.setCode("C001");
                    adResponse.setMessage("광고 목록 조회 성공");

//                    // Redis에 데이터 저장
//                    redisTemplate.opsForValue().set(cacheKey, adResponse, cacheTime, TimeUnit.MILLISECONDS);
//                    log.info("광고 목록을 Redis에 캐싱했습니다.");
                } else {
                    adResponse.setCode("C005");
                    adResponse.setMessage("광고 조회 실패");
                }
//            }
        } catch(Exception e){
            adResponse.setCode("E000");
            adResponse.setMessage("광고 조회 실패");
            log.error("광고 목록 조회 중 오류 발생: ", e);
        }
        return adResponse;
    }

    // 광고 수정
    public ApiResponse updateAd(fmAd ad) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int updateAd = adMapper.updateAd(ad);
            if(updateAd == 1){
                apiResponse.setCode("C001");
                apiResponse.setMessage("광고 업데이트 성공");

//                String pattern = "fmAdList_*"; // 패턴 정의
//                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기
//
//                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
//                log.info("Redis에서 광고 목록 캐시를 삭제했습니다.");
            } else {
                apiResponse.setCode("C004");
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
                apiResponse.setCode("C001");
                apiResponse.setMessage("광고 삭제 성공");

//                String pattern = "fmAdList_*"; // 패턴 정의
//                Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기
//
//                redisTemplate.delete(keys); // 전체 고객사 목록 캐시를 삭제
//                log.info("Redis에서 광고 목록 캐시를 삭제했습니다.");
            } else {
                apiResponse.setCode("C004");
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
                adResponse.setCode("C001");
                adResponse.setMessage("광고 목록 전체 조회 성공");
            } else {
                adResponse.setCode("C005");
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
                adResponse.setCode("C001");
                adResponse.setMessage("광고 목록 조회 성공");
            } else {
                adResponse.setCode("C005");
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
                adResponse.setCode("C001");
                adResponse.setMessage("aid 일치하는 폼메일용 광고 조회 성공");
            } else {
                adResponse.setCode("C004");
                adResponse.setMessage("aid 일치하는 폼메일용 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
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
                adResponse.setCode("C001");
                adResponse.setMessage("광고 목록 전체 조회 성공");
            } else {
                adResponse.setCode("C005");
                adResponse.setMessage("광고 목록 전체 조회 실패");
            }
        } catch (Exception e){
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }

    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    public AdResponse searchTitleList(fmAd ad) throws Exception {
        AdResponse adResponse = new AdResponse();

        try {
            adResponse.setJobSiteList(adMapper.searchTitleList(ad));
            if(adResponse.getJobSiteList() != null && !adResponse.getJobSiteList().isEmpty()){
                adResponse.setCode("C001");
                adResponse.setMessage("제목 일치하는 잡사이트 목록 조회 성공");
            } else {
                adResponse.setCode("C004");
                adResponse.setMessage("제목 일치하는 잡사이트 목록 조회 실패");
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
                adResponse.setCode("C001");
                adResponse.setMessage("aid 일치하는 잡사이트 광고 조회 성공");
            } else {
                adResponse.setCode("C004");
                adResponse.setMessage("aid 일치하는 잡사이트 광고 조회 실패");
            }
        } catch (Exception e) {
            adResponse.setCode("E001");
            adResponse.setMessage("ERROR");
        }
        return adResponse;
    }
//
//    public AdResponse searchTitleList(fmAd ad) throws Exception {
//        AdResponse adResponse = new AdResponse();
//
//        try {
//
//        } catch (Exception e) {
//
//        }
//
//        return adResponse;
//    }




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
                s3UploadResponse.setCode("C001");
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
            adResponse.setCode("C001");
            adResponse.setMessage("고객사 조회 성공");
        } else {
            adResponse.setCode("E000");
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
                apiResponse.setCode("C001");
                apiResponse.setMessage("삭제 완료");
                s3Uploader.deleteFile(url);
            } else {
                apiResponse.setCode("C004");
                apiResponse.setMessage("삭제 실패");
            }
        } catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error");
        }
        return apiResponse;
    }

}
