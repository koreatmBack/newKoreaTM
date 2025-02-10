package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.PhoneNum
import com.example.smsSpringTest.entity.FormMailAdminEntity
import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.User
import com.example.smsSpringTest.model.findUser
import com.example.smsSpringTest.model.FormMailAdmin
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
            , created
            , updated
        FROM formmail_admin
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<FormMailAdmin> adminProfileList(@Param("paging") Paging paging)

    // 전체 회원 수
    @Select("""
        SELECT count(*)
        FROM formmail_admin
    """)
    int getUserListCount()

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
      </set>
        WHERE user_id = #{user.userId}
</script>
    """)
    int updateAdmin(@Param("user") FormMailAdmin admin)

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

    // -------- jwt 토큰 관련 mapper -------------------------

//    // RefreshToken 조회
//    @Select("""
//        SELECT user_id
//                , grant_type
//                , refresh_token
//                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
//                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
//          FROM formmail_admin_token
//         WHERE user_id = #{userId}
//           AND use_yn = 'Y'
//    """)
//    RefToken getUserRefreshTokenData(@Param("userId") String userId)
//
//    // RefreshToken 삭제
//    @Delete("""
//        DELETE FROM formmail_admin_token
//         WHERE user_id = #{userId}
//    """)
//    int deleteUserToken(@Param("userId") String userId)
//
//    // RefreshToken 토큰 등록
//    @Insert("""
//        INSERT INTO formmail_admin_token (
//                user_id
//                , grant_type
//                , refresh_token
//                , use_yn
//                , reg_date
//                , upt_date
//        ) VALUES (
//                #{refToken.userId}
//                , #{refToken.grantType}
//                , #{refToken.refreshToken}
//                , 'Y'
//                , sysdate()
//                , sysdate()
//        )
//    """)
//    int addUserToken(@Param("refToken") RefToken refToken)
//
//    // RefreshToken 업데이트
//    @Update("""
//        UPDATE formmail_admin_token
//           SET refresh_token = #{refToken.refreshToken}
//               , grant_type = #{refToken.grantType}
//               , upt_date = sysdate()
//         WHERE user_id = #{refToken.userId}
//           AND use_yn = 'Y'
//    """)
//    int updateUserToken(@Param("refToken") RefToken refToken)
//
//    // RefreshToken 존재여부 확인
//    @Select("""
//        SELECT count(*)
//          FROM formmail_admin_token
//         WHERE user_id = #{userId}
//           AND use_yn = 'Y'
//    """)
//    int getUserRefreshTokenCount(@Param("userId") String userId)
//
//    // 동일한 RefreshToken 확인
//    @Select("""
//        SELECT count(*)
//          FROM formmail_admin_token
//         WHERE refresh_token = #{refToken.refreshToken}
//           AND use_yn = 'Y'
//    """)
//    int getRefreshTokenCount(@Param("refToken") RefToken refToken)
//
//    // RefreshToken 으로 정보 조회
//    @Select("""
//        SELECT user_id
//                , grant_type
//                , refresh_token
//                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
//                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
//          FROM formmail_admin_token
//         WHERE refresh_token = #{refToken.refreshToken}
//           AND use_yn = 'Y'
//    """)
//    RefToken getRefreshTokenData(@Param("refToken") RefToken refToken)
//
//    // 토큰 있을때 회원 정보 조회
//    @Select("""
//        SELECT am.user_id
//                , am.r_name
//                , at.refresh_token
//        FROM formmail_admin am
//        INNER JOIN formmail_admin_token at on am.user_id = at.user_id
//        WHERE am.user_id = #{userId}
//    """)
//    UserProfile getFrontUserProfile(@Param("userId") String userId)


}