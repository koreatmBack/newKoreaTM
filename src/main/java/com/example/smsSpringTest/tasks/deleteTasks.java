package com.example.smsSpringTest.tasks;

import com.example.smsSpringTest.mapper.SmsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : 매일 자정에 삭제할 과제들
 */

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class deleteTasks {

    private final SmsMapper smsMapper;

    // 본인인증 테이블 비우기
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteAllSMSCode() throws Exception {
        smsMapper.deleteAllSMSCode();
    }




}
