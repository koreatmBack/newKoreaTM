package com.example.smsSpringTest.tasks;

import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.SmsMapper;
import com.example.smsSpringTest.mapper.jobsite.JobCommonMapper;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final JobCommonMapper jobCommonMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommonMapper commonMapper;

    // 잡사이트 회원 만료 리프레시 토큰 삭제
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void jobsiteRefreshTokenExpirationCheck() throws Exception {
        List<RefToken> expiredTokens = jobCommonMapper.getJobUserRefreshTokenAll();

        for (RefToken token : expiredTokens) {
            Long remainingMilliseconds = jwtTokenProvider.getExpiration(token.getRefreshToken());

            if(remainingMilliseconds == null || remainingMilliseconds <= 0) {
                jobCommonMapper.deleteUserToken(token.getUserId());
            }
        }
    }

    // 폼메일 계정 만료 리프레시 토큰 삭제
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void formMailRefreshTokenExpirationCheck() throws Exception {
        List<RefToken> expiredTokens = commonMapper.getFormMailUserRefreshTokenAll();

        for (RefToken token : expiredTokens) {
            Long remainingMilliseconds = jwtTokenProvider.getExpiration(token.getRefreshToken());

            if(remainingMilliseconds == null || remainingMilliseconds <= 0) {
                commonMapper.deleteUserToken(token.getUserId());
            }
        }
    }

    // 본인인증 테이블 비우기
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteAllSMSCode() throws Exception {
        smsMapper.deleteAllSMSCode();
    }




}
