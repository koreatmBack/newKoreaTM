package com.example.smsSpringTest.mapper.jobsite


import com.example.smsSpringTest.model.jobsite.CertSMS
import com.example.smsSpringTest.model.jobsite.JobsiteUser
import org.apache.ibatis.annotations.*

@Mapper
interface JobUserMapper {

    // 본인인증 코드 저장할때 이미 인증 받았으면 해당 row에 인증 번호 덮어 씌우기
    @Update("""
        UPDATE jobsite_sms_code
        SET sms_code = #{cert.smsCode}
        WHERE phone = #{cert.phone} 
    """)
    int dupSmsCode(@Param("cert") CertSMS cert);

    // 본인인증 일치하는지 찾기
    @Select("""
        SELECT count(*)
        FROM jobsite_sms_code
        WHERE user_name = #{cert.userName}
        AND phone = #{cert.phone}
        AND sms_code = #{cert.smsCode}
    """)
    int certUser(@Param("cert") CertSMS cert);

    // 본인인증 성공시 테이블에서 해당 row 삭제
    @Delete("""
        DELETE FROM jobsite_sms_code
        WHERE phone = #{cert.phone}
    """)
    int deleteSmsCode(@Param("cert") CertSMS cert);

    @Insert("""
        INSERT INTO jobsite_user(
            user_id
            , user_pwd
            , user_name
            , phone
            , address
            , sido
            , sigungu
            , gender
            , birth
            , photo
            , marketing
        ) VALUES (
            #{user.userId}
            , #{user.userPwd}
            , #{user.userName}
            , #{user.phone}
            , #{user.address}
            , #{user.sido}
            , #{user.sigungu}
            , #{user.gender}
            , #{user.birth}
            , #{user.photo}
            , #{user.marketing}
        )
    """)
    int jobSignUp(@Param("user") JobsiteUser user);

    // 암호화된 비밀번호 반환 ( 로그인시 비밀번호 체크용 )
    @Select("""
        SELECT user_pwd
        FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    String dupPwd(@Param("user") JobsiteUser user);

    // 회원 한명(pw 제외) 반환
    @Select("""
        SELECT user_id
        , user_name
        , phone
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    JobsiteUser findOneUser(@Param("userId") String userId)

    // 이름 반환
    @Select("""
        SELECT user_name
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    String userName(@Param("userId") String userId)
}