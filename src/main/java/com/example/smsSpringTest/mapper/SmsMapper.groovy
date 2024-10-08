package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.SmsForm
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface SmsMapper {

    // db에 문자 관련 등록
    @Insert("""
        INSERT INTO formmail_sms(
            s_phone
            , r_phone
            , sms_type
            , msg
        ) VALUES (
            #{sms.sPhone}
            , #{sms.rPhone}
            , #{sms.smsType}
            , #{sms.msg}
        )
    """)
    int addMsg(@Param("sms") SmsForm sms)
}