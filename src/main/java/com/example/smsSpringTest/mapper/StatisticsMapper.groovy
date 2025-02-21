package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Statistics
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/*

    통계용 mapper

 */

@Mapper
interface StatisticsMapper {

    // 00시05분쯤 저장시킬 데이터들
    @Insert("""
        INSERT INTO formmail_statistics(
            date
            , total_apply
            , total_available
            , today_interview_expect
            , yesterday_interview_attend
            , tomorrow_interview_expect
        ) VALUES (
            #{stat.date}
            , #{stat.totalApply}
            , #{stat.totalAvailable}
            , #{stat.todayInterviewExpect}
            , #{stat.yesterdayInterviewAttend}
            , #{stat.tomorrowInterviewExpect}
        )
    """)
    int saveStatistics(@Param("stat") Statistics stat)

    // 통계에 필요한 데이터 찾기 (저장용)
    @Select("""
    SELECT 
        SUM(CASE WHEN DATE(apply_date) = #{yesterday} THEN 1 ELSE 0 END) AS total_apply,
        SUM(CASE WHEN DATE(apply_date) = #{yesterday} AND apply_status IN 
        ('신규DB','당일면접','익일면접','면접예정','면접참석', '면접불참', '면접포기','위촉자','합격자','교육중', '부재', '당부', '보류', '취소')
        THEN 1 ELSE 0 END) AS total_available,
        SUM(CASE WHEN DATE(interview_time) = #{yesterday} AND apply_status = '당일면접' THEN 1 ELSE 0 END) AS today_interview_expect,
        SUM(CASE WHEN DATE(interview_time) = #{twoDaysAgo} AND apply_status = '면접참석' THEN 1 ELSE 0 END) AS yesterday_interview_attend,
        SUM(CASE WHEN DATE(interview_time) = #{today} AND apply_status = '익일면접' THEN 1 ELSE 0 END) AS tomorrow_interview_expect
    FROM formmail_apply
    """)
    Statistics statistics(@Param("yesterday") String yesterday, @Param("today") String today,
    @Param("twoDaysAgo") String twoDaysAgo)


    // 지원자 리스트에서 실시간으로 볼 통계
    @Select("""
        SELECT
            SUM(CASE WHEN DATE(apply_date) = CURDATE() THEN 1 ELSE 0 END) AS total_apply,
            SUM(CASE WHEN DATE(apply_date) = CURDATE() AND apply_status IN
                    ('신규DB','당일면접','익일면접','면접예정','면접참석', '면접불참', '면접포기','위촉자','합격자','교육중', '부재', '당부', '보류', '취소')
            THEN 1 ELSE 0 END) AS total_available,
            SUM(CASE WHEN DATE(interview_time) = CURDATE() AND apply_status = '당일면접' THEN 1 ELSE 0 END) AS today_interview_expect,
            SUM(CASE WHEN DATE(interview_time) = DATE_ADD(CURDATE(), INTERVAL -1 DAY) AND apply_status = '면접참석'
            THEN 1 ELSE 0 END) AS yesterday_interview_attend,
            SUM(CASE WHEN DATE(interview_time) = DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND apply_status = '익일면접'
            THEN 1 ELSE 0 END) AS tomorrow_interview_expect,
            CURDATE() AS date
        FROM formmail_apply    
    """)
    Statistics dailyStatistics()

    // 증감 값 구하기 위해 DB에서 하루 전 데이터 가져오기
    @Select("""
        SELECT *
        FROM formmail_statistics
        WHERE date = DATE_ADD(CURDATE(), INTERVAL -1 DAY)
    """)
    Statistics yesterdayStatistics()

}