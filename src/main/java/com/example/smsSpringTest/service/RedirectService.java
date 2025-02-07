package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * author : 신기훈
 * date : 2025-02-07
 * comment : 광고 로그 통계 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RedirectService {

    private final CommonMapper commonMapper;

    public void countRedirect(String page, String url) throws Exception {
        try {

            // 현재 시간 가져오기
            LocalTime now = LocalTime.now();

            // 시간대별 카운트 컬럼 선택
            String timeType = getTimeType(now);

            // 20250207 형식
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            int firstInsertCheck = commonMapper.firstInsertChk(date,url,page);
            if(firstInsertCheck != 0) {
                // 이미 값 있음
                // 그럼 업데이트 해야겠지
                int updatePageView = commonMapper.updatePageView(page, date, timeType, url);

            } else {
                // 값 없어
                // 그러면 추가 해야겠지
                int firstInsert = commonMapper.firstInsert(page, date, timeType, url);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private String getTimeType(LocalTime time) {
        if (time.isBefore(LocalTime.of(6, 0))) {
            return "dawn_view";   // 새벽 (00:00 - 05:59)
        } else if (time.isBefore(LocalTime.of(12, 0))) {
            return "morning_view"; // 오전 (06:00 - 11:59)
        } else if (time.isBefore(LocalTime.of(18, 0))) {
            return "afternoon_view"; // 오후 (12:00 - 17:59)
        } else {
            return "night_view";   // 밤 (18:00 - 23:59)
        }
    }

}
