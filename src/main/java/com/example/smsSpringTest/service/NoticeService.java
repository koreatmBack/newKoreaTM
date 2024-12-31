package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.NoticeMapper;
import com.example.smsSpringTest.model.jobsite.Notice;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author : 신기훈
 * date : 2024-12-16
 * comment : 공지사항, FAQ Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoticeService {

    private final NoticeMapper noticeMapper;

    // 공지사항 or FAQ 등록하기
    public ApiResponse insertNotice(Notice notice) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int insertNotice = noticeMapper.insertNotice(notice);
            if(insertNotice == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("등록 성공");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("등록 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error !!! ");
        }

        return apiResponse;
    }

    // 공지사항 or FAQ 전체 조회
    public NoticeResponse findAllNotice(Notice notice) throws Exception {
        NoticeResponse noticeResponse = new NoticeResponse();

        try {
            int page = notice.getPage(); // 현재 페이지
            int size = notice.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = noticeMapper.countAll(notice); //전체 수
            notice.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);

            noticeResponse.setNoticeList(noticeMapper.noticeList(notice));
            if(noticeResponse.getNoticeList() != null && !noticeResponse.getNoticeList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                noticeResponse.setTotalPages(totalPages);
                noticeResponse.setCode("C000");
                noticeResponse.setMessage("전체 조회 성공");
            } else {
                noticeResponse.setCode("C003");
                noticeResponse.setMessage("전체 조회 실패");
            }
        } catch (Exception e) {
                noticeResponse.setCode("E001");
                noticeResponse.setMessage("Error!!!");
                log.info(e.getMessage());
        }
        return noticeResponse;
    }


    // 원하는 공지사항 or FAQ 조회
    public NoticeResponse findOneNotice(Notice notice) throws Exception {
        NoticeResponse noticeResponse = new NoticeResponse();

        try {
//            Notice findNotice = noticeMapper.findOneNotice(notice);
//            log.info("최초 find = " + findNotice);
//            String formattedContent = findNotice.getContent().replace("\\n", "\n");
//            log.info("format = " + formattedContent);
//            findNotice.setContent(formattedContent);
//            log.info("나중 find = " + findNotice.getContent());

            // 공지사항이면 조회수 1 증가
            if(noticeMapper.findOneNotice(notice).getType().equals("B01")){
                int viewCount = noticeMapper.findOneNotice(notice).getViewCount();
                viewCount ++;
                notice.setViewCount(viewCount);
                noticeMapper.increaseViewCount(notice);
            }

            noticeResponse.setFindOneNotice(noticeMapper.findOneNotice(notice));
            if(noticeResponse.getFindOneNotice() != null){
                noticeResponse.setCode("C000");
                noticeResponse.setMessage("조회 성공");
            } else {
                noticeResponse.setCode("C003");
                noticeResponse.setMessage("조회 실패");
            }
        } catch (Exception e) {
            noticeResponse.setCode("E001");
            noticeResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }

        return noticeResponse;
    }

    // 공지사항 or FAQ 수정하기
    public ApiResponse updateNotice(Notice notice) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int updateNotice = noticeMapper.updateNotice(notice);
            if(updateNotice == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("수정 성공");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("수정 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error !!! ");
        }

        return apiResponse;
    }

    // 공지사항 or FAQ 삭제하기
    public ApiResponse deleteNotice(Notice notice) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int deleteNotice = noticeMapper.deleteNotice(notice);
            if(deleteNotice == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("삭제 성공");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error !!! ");
        }
        return apiResponse;
    }

    // FAQ 카테고리로 , 해당 FAQ 목록 반환
    // 공지사항 or FAQ 전체 조회
    public NoticeResponse faqCategoryList(Notice notice) throws Exception {
        NoticeResponse noticeResponse = new NoticeResponse();

        try {
            int page = notice.getPage(); // 현재 페이지
            int size = notice.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = noticeMapper.faqCategoryCount(notice); //전체 수
            notice.setOffset(offset);

            log.info("page = " + page + " size = " + size + " offset = " + offset + " totalCount = " + totalCount);

            noticeResponse.setNoticeList(noticeMapper.faqCategoryList(notice));
            if(noticeResponse.getNoticeList() != null && !noticeResponse.getNoticeList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("totalPages = " + totalPages);
                noticeResponse.setTotalPages(totalPages);
                noticeResponse.setCode("C000");
                noticeResponse.setMessage("전체 조회 성공");
            } else {
                noticeResponse.setCode("C003");
                noticeResponse.setMessage("전체 조회 실패");
            }
        } catch (Exception e) {
            noticeResponse.setCode("E001");
            noticeResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return noticeResponse;
    }
}
