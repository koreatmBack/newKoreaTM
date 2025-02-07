package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.SmsForm
import com.example.smsSpringTest.model.jobsite.Cert
import org.apache.ibatis.annotations.Delete
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

    // 본인인증용 db 저장
    @Insert("""
        INSERT INTO cert_sms_code(
            user_name
            ,phone
            ,sms_code
            ,user_id
        ) VALUES (
            #{cert.userName}
            , #{cert.phone}
            , #{cert.smsCode}
            , #{cert.userId}
        )
    """)
    int addCertSMS(@Param("cert") Cert cert)

    // 본인인증용 db 모두 삭제 ( 자정에 실행할 예정 )
    @Delete("""
        DELETE FROM cert_sms_code
    """)
    int deleteAllSMSCode()
}