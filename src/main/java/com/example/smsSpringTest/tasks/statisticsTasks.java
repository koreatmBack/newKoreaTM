package com.example.smsSpringTest.tasks;

import com.example.smsSpringTest.mapper.StatisticsMapper;
import com.example.smsSpringTest.model.Statistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * author : 신기훈
 * date : 2025-02-20
 * comment : 매일 00시 05분쯤 저장할 통계 데이터들
 */

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class statisticsTasks {

    private final StatisticsMapper statisticsMapper;

    @Scheduled(cron = "0 31 9 * * *") // 매일 실행  초,분,시간, * * *
    public void saveStatistics() throws Exception {

        LocalDate today1 = LocalDate.now(); // 오늘 날짜
        LocalDate yesterday1 = today1.minusDays(1); // 하루 전
        LocalDate twoDaysAgo1 = today1.minusDays(2); // 이틀 전

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today1.format(formatter); // 오늘 날짜
        String yesterday = yesterday1.format(formatter); // 하루 전
        String twoDaysAgo = twoDaysAgo1.format(formatter); // 이틀 전

        log.info("today = " + today);
        log.info("yesterday = " + yesterday);
        log.info("twoDaysAgo = " + twoDaysAgo);

        Statistics statistics = statisticsMapper.statistics(yesterday, today, twoDaysAgo);

        statistics.setDate(yesterday);

        log.info(statistics.toString());

        // 전체 통계 저장
        int saveStatistics = statisticsMapper.saveStatistics(statistics);

        // 매니저 통계 저장
        List<Statistics> managerStatistics = statisticsMapper.managerStatistics(yesterday, today, twoDaysAgo);
        for(Statistics stats : managerStatistics) {
            stats.setDate(yesterday);
            statisticsMapper.saveManagerStatistics(stats);
        }

    }
}
