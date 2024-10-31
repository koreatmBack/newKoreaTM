package com.example.smsSpringTest.mapper.jobsite

import com.example.smsSpringTest.model.Paging
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
            , address_detail
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
            , #{user.addressDetail}
        )
    """)
    int jobSignUp(@Param("user") JobsiteUser user);

    // 회원가입 할때 폼메일 id와 일치하면 같은 id 사용 못하게 막기 위해
    @Select("""
        SELECT count(*)
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    int dupFormMailIdCheck(@Param("userId") String userId)

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인
    @Select("""
        SELECT count(*)
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    int checkId(@Param("userId") String userId)

    // 암호화된 비밀번호 반환 ( 로그인시 비밀번호 체크용 )
    @Select("""
        SELECT user_pwd
        FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    String dupPwd(@Param("user") JobsiteUser user);

    // 회원 한명(id, 이름, 연락처) 반환
    @Select("""
        SELECT user_id
        , user_name
        , phone
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    JobsiteUser findOneJobLoginUser(@Param("userId") String userId)

    // 이름 반환
    @Select("""
        SELECT user_name
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    String userName(@Param("userId") String userId)

    // 잡사이트 회원 정보 수정
    @Update("""
<script>
        UPDATE jobsite_user
      <set>
           <if test="user.userPwd != null"> user_pwd = #{user.userPwd},</if>
           <if test="user.userName !=null"> user_name = #{user.userName},</if>
           <if test="user.phone != null"> phone = #{user.phone},</if>
           <if test="user.address != null"> address = #{user.address} ,</if>
           <if test="user.sido != null"> sido = #{user.sido},</if>
           <if test="user.sigungu!= null"> sigungu = #{user.sigungu},</if>
           <if test="user.gender != null"> gender = #{user.gender} ,</if>
           <if test="user.birth != null"> birth = #{user.birth},</if>
           <if test="user.photo != null"> photo = #{user.photo} ,</if>
           <if test="user.marketing != null"> marketing = #{user.marketing},</if>  
           <if test="user.addressDetail != null"> address_detail = #{user.addressDetail},</if>
      </set>
        WHERE user_id = #{user.userId}  
</script>        
    """)
    int jobUserUpdate(@Param("user") JobsiteUser user)

    // 회원 한명 (비밀번호 제외) 조회
    @Select("""
        SELECT user_id
        , user_name
        , phone
        , address
        , sido
        , sigungu
        , gender
        , birth
        , photo
        , marketing
        , address_detail
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    JobsiteUser findOneJobUser(@Param("userId") String userId)

    // 회원 전체 수 조회
    @Select("""
        SELECT count(*)
        from jobsite_user
    """)
    int getUserListCount()

    // 전체 회원 리스트
    @Select("""
        SELECT user_id
        , user_name
        , phone
        , address
        , sido
        , sigungu
        , gender
        , birth
        , photo
        , marketing
        , address_detail
        FROM jobsite_user
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobsiteUser> jobsiteUserList(@Param("paging") Paging paging)


}