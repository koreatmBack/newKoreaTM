package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.ad.fmAd
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

import java.time.LocalDate

@Mapper
interface HolidayMapper {

    // 공휴일 등록 ( IGNORE -> 중복 제외, 중복 아닌 값들은 계속 저장 가능하게 )
    @Insert("""
        INSERT IGNORE INTO formmail_holiday(
            date_name
            , holiday_date
        ) VALUES (
            #{dateName}
            ,#{locdate}
        )
    """)
    int addHoliday(@Param("dateName") String dateName, @Param("locdate") LocalDate localDate)


    // 주말 등록
    @Insert("""
        INSERT IGNORE INTO formmail_holiday(
            date_name
            , holiday_date
        ) VALUES (
            '주말'
            ,#{locdate}
        )
    """)
    int addWeekend(@Param("locdate") LocalDate localDate)

    // 평일 일수 계산하는법( 시작일 제외 )
    @Select("""
    SELECT DATEDIFF(#{ad.endDate}, #{ad.startDate})
    - (SELECT COUNT(*) FROM formmail_holiday fh
       WHERE fh.holiday_date BETWEEN #{ad.startDate} AND #{ad.endDate}) 
    FROM formmail_ad fa
    WHERE fa.aid = #{serialNumber}
    """)
    int totalDay(@Param("ad") fmAd ad, @Param("serialNumber") String serialNumber)


    // 평일 일수 계산하는법 (시리얼 넘버 필요 x , 시작일 제외)
    @Select("""
    SELECT DATEDIFF(#{ad.endDate}, #{ad.startDate})
    - (SELECT COUNT(*) FROM formmail_holiday fh
       WHERE fh.holiday_date BETWEEN #{ad.startDate} AND #{ad.endDate}) AS total_days
    """)
    Integer onlyTotalDay(@Param("ad") fmAd ad)

}