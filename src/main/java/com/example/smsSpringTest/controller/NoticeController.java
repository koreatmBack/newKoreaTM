package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.jobsite.Notice;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.NoticeResponse;
import com.example.smsSpringTest.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-12-16
 * comment : 공지사항,FAQ controller
 */
@RestController
@RequestMapping({"/api/v1/notice", "/v1/notice"})
@Slf4j
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항, FAQ 등록
    @PostMapping("/insert")
    public ApiResponse insertNotice(@RequestBody Notice notice) throws Exception {
        return noticeService.insertNotice(notice);
    }

    // 공지사항 or FAQ 전체 조회
    // 타입 필요(B01 = 공지사항, B02 = FAQ)
    @PostMapping("/find/allList")
    public NoticeResponse findAllNotice(@RequestBody Notice notice) throws Exception {
        return noticeService.findAllNotice(notice);
    }

    // 원하는 공지사항 or FAQ 조회하기
    @PostMapping("/find/one")
    public NoticeResponse findOneNotice(@RequestBody Notice notice) throws Exception {
        return noticeService.findOneNotice(notice);
    }

    // 공지사항 or FAQ 수정하기
    @PutMapping("/update/one")
    public ApiResponse updateNotice(@RequestBody Notice notice) throws Exception {
        return noticeService.updateNotice(notice);
    }

    // 공지사항 or FAQ 삭제하기
    @DeleteMapping("/delete/one")
    public ApiResponse deleteNotice(@RequestBody Notice notice) throws Exception {
        return noticeService.deleteNotice(notice);
    }

    // FAQ 카테고리로 , 해당 FAQ 목록 반환
    @PostMapping("/find/faq/category")
    public NoticeResponse faqCategoryList(@RequestBody Notice notice) throws Exception {
        return noticeService.faqCategoryList(notice);
    }

}
