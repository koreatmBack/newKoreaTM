package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.FormMailAdminEntity
import com.example.smsSpringTest.entity.PhoneNum
import com.example.smsSpringTest.model.FormMailAdmin
import com.example.smsSpringTest.model.SmsForm
import com.example.smsSpringTest.model.User
import com.example.smsSpringTest.model.findUser
import org.apache.ibatis.annotations.*

/*

    ADMIN 용 mapper

 */

@Mapper
interface AdminMapper {

    // 회원등록
    @Insert("""
        INSERT INTO formmail_admin (
            user_id
            , user_pwd
            , r_name
            , user_name
            , position
            , role
            , team
            , m_phone
            , r_phone
            , email
            , form_no
            ,rank
        ) VALUES (
            #{admin.userId}
            , #{admin.userPwd}
            , #{admin.rName}
            , #{admin.userName}
            , #{admin.position}
            , #{admin.role}
            , #{admin.team}
            , #{admin.mPhone}
            , #{admin.rPhone}
            , #{admin.email}
            , #{admin.formNo}
            , #{admin.rank}
        )
    """)
    int signUp(@Param("admin") FormMailAdmin admin)

    // 아이디 중복처리
    @Select("""
        SELECT count(*) 
         FROM formmail_admin
         WHERE user_id = #{userId}
    """)
    int userDuplicatedChkId(@Param("userId") String userId)

    // 잡사이트 아이디와 같으면 가입 x
    @Select("""
        SELECT count(*) 
         FROM jobsite_user
         WHERE user_id = #{userId}
    """)
    int dupJobsiteIdCheck(@Param("userId") String userId)

    // 로그인시 비밀번호 반환
    @Select("""
        SELECT user_pwd
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    String userPassword(@Param("userId") String userId)


    // 회원 정보 반환
    @Select("""
        SELECT r_name
        , m_phone
        , role
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    User user(@Param("userId") String userId)

    // 이름 반환
    @Select("""
        SELECT r_name
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    String userName(@Param("userId") String userId)

    // 회원 목록(pw 제외) 반환
    @Select("""
<script>
        SELECT user_id
            , r_name
            , user_name
            , position
            , role
            , team
            , m_phone
            , r_phone
            , email
            , form_no
            , rank
            , created_at
            , updated_at
            , use_status
        FROM formmail_admin
        WHERE 1=1
        <if test="admin.team != null"> AND team = #{admin.team} </if>
        ORDER BY created_at DESC
        LIMIT #{admin.size} OFFSET #{admin.offset}
</script>        
    """)
    List<FormMailAdmin> adminProfileList(@Param("admin") FormMailAdmin admin)

    // SUBADMIN 일때 team 일치한 회원 목록 리턴
    @Select("""
        SELECT user_id
            , r_name
            , user_name
            , position
            , role
            , team
            , m_phone
            , r_phone
            , email
            , form_no
            , rank
            , created_at
            , updated_at
            , use_status
            FROM formmail_admin
            WHERE team = #{admin.team}
            ORDER BY created_at DESC
            LIMIT #{admin.size} OFFSET #{admin.offset}
    """)
    List<FormMailAdmin> teamList(@Param("admin") FormMailAdmin admin)

    // SUBADMIN 일때 team 일치한 회원 목록 리턴
    @Select("""
        SELECT count(*)
        FROM formmail_admin
        WHERE team = #{team}
    """)
    int teamListCount(@Param("team") String team)

    // 전체 회원 수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_admin
        WHERE 1=1
        <if test="admin.team != null"> AND team = #{admin.team} </if>
</script>        
    """)
    int getUserListCount(@Param("admin") FormMailAdmin admin)

    // 회원 한명(pw 제외) 반환
    @Select("""
        SELECT user_id
            , r_name
            , user_name
            , position
            , role
            , team
            , m_phone
            , r_phone
            , email
            , form_no
            , rank
            , use_status
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    FormMailAdmin findOneAdmin(@Param("userId") String userId)

    // 회원 수정
    @Update("""
<script>
        UPDATE formmail_admin
      <set>
        <if test="admin.userPwd != null"> user_pwd = #{admin.userPwd},</if>
        <if test="admin.rName != null"> r_name = #{admin.rName},</if>
        <if test="admin.userName != null"> user_name = #{admin.userName},</if>
        <if test="admin.position != null"> position = #{admin.position},</if>
        <if test="admin.team != null"> team = #{admin.team},</if>
        <if test="admin.mPhone != null"> m_phone = #{admin.mPhone},</if>
        <if test="admin.rPhone != null"> r_phone = #{admin.rPhone},</if>
        <if test="admin.email != null"> email = #{admin.email},</if>
        <if test="admin.role != null"> role = #{admin.role},</if>
        <if test="admin.formNo != null"> form_no = #{admin.formNo},</if>
        <if test="admin.rank != null"> rank = #{admin.rank},</if>
        <if test="admin.useStatus != null"> use_status = #{admin.useStatus},</if>
      </set>
        WHERE user_id = #{admin.userId}
</script>
    """)
    int updateAdmin(@Param("admin") FormMailAdmin admin)

    // id가 일치할때 그 회원의 모든 db값 반환
    @Select("""
        SELECT *
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    FormMailAdminEntity userProfile(@Param("userId") String userId)

//    // 이름이 일치할때 그 회원의 모든 db값 반환
//    @Select("""
//        SELECT user_id
//            , r_name
//            , user_name
//            , position
//            , admin
//            , team
//            , m_phone
//            , r_phone
//        FROM formmail_admin
//        WHERE user_name = #{user.userName}
//    """)
//    List<UserProfile> findUsers(@Param("user") UserProfile user)


    // 이름이 일치할때 그 회원의 모든 db값 반환
    @Select("""
    SELECT user_id
        , r_name
        , user_name
        , position
        , role
        , team
        , m_phone
        , r_phone
        , email
        , form_no
        , use_status
    FROM formmail_admin
    WHERE user_name LIKE CONCAT('%', #{name}, '%')
    OR r_name LIKE CONCAT('%', #{name}, '%')
    """)
    List<FormMailAdmin> findAdmins(@Param("name") String name)


    // 업무용 연락처 등록
    @Insert("""
        INSERT INTO formmail_phone (
            phone_number
        )
        VALUES (
            #{mPhone}
        )
    """)
    int addPhoneNum(@Param("mPhone") String mPhone)

    // 업무용 연락처 등록시, 중복 체크
    @Select("""
        SELECT COUNT(*)
        FROM formmail_phone
        WHERE phone_number = #{mPhone}
    """)
    int validPhoneChk(@Param("mPhone") String mPhone)

    // 업무용 연락처 전체 조회
    @Select("""
        SELECT phone_number
        , DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS createDate
        FROM formmail_phone
    """)
    List<PhoneNum> allPhoneNumList()

    // 업무용 연락처 번호 삭제
    @Delete("""
        DELETE FROM formmail_phone
        WHERE phone_number = #{mPhone}
    """)
    int delPhoneNum(@Param("mPhone") String mPhone)

    // 입력받은 업무용 연락처로 회원 db에서 번호 일치하는 회원 이름, 포지션 찾기
    @Select("""
        SELECT fa.r_name
        , fa.user_name
        , fa.position
        FROM formmail_admin fa
        JOIN formmail_phone fp ON fa.m_phone = fp.phone_number
        WHERE fp.phone_number = #{phoneNum}
    """)
    List<findUser> findUserList(@Param("phoneNum") String phoneNum)

    // 폼메일 회원 삭제
    @Delete("""
        DELETE FROM formmail_admin
        WHERE user_id = #{admin.userId}
    """)
    int deleteOne(@Param("admin") FormMailAdmin admin)

    // "채용"팀이며 사용중(use_status = true)인 회원 목록
    @Select("""
        SELECT user_id
              ,form_no
              ,r_name
              ,user_name
              ,rank
        FROM formmail_admin
        WHERE team = '채용'
        AND use_status = 1
    """)
    List<FormMailAdmin> recruitTeamList()

    // 문자 내역 조회
    @Select("""
<script>
        SELECT *
        FROM formmail_sms
        WHERE 1=1
        <if test="sms.managerId != null">AND manager_id = #{sms.managerId}</if>
        <if test="sms.smsType != null">AND sms_type = #{sms.smsType}</if>
        <if test="sms.sPhone != null and sms.sPhone.trim() != '' and sms.sPhone.matches('^[0-9\\\\-]+\$')">
            AND (REPLACE(s_phone, '-', '') = REPLACE(#{sms.sPhone}, '-', '')
            OR RIGHT(REPLACE(s_phone, '-', ''), 4) = #{sms.sPhone})
        </if>
        <if test="sms.rPhone != null">
            AND (REPLACE(r_phone, '-', '') = REPLACE(#{sms.rPhone}, '-', '')
            OR RIGHT(REPLACE(r_phone, '-', ''), 4) = #{sms.rPhone})
        </if>
        ORDER BY send_date DESC
        LIMIT #{sms.size} OFFSET #{sms.offset}
</script>        
    """)
    List<SmsForm> smsList(@Param("sms") SmsForm sms)

    // 문자 내역 조회 개수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_sms
        WHERE 1=1
        <if test="sms.managerId != null">AND manager_id = #{sms.managerId}</if>
        <if test="sms.smsType != null">AND sms_type = #{sms.smsType}</if>
        <if test="sms.sPhone != null">
            AND (REPLACE(s_phone, '-', '') = REPLACE(#{sms.sPhone}, '-', '')
            OR RIGHT(REPLACE(s_phone, '-', ''), 4) = #{sms.sPhone})
        </if>
        <if test="sms.rPhone != null">
            AND (REPLACE(r_phone, '-', '') = REPLACE(#{sms.rPhone}, '-', '')
            OR RIGHT(REPLACE(r_phone, '-', ''), 4) = #{sms.rPhone})
        </if>
</script>        
    """)
    int smsListCount(@Param("sms") SmsForm sms)

}