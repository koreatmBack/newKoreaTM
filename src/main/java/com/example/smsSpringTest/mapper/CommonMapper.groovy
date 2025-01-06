package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.UserProfile
import com.example.smsSpringTest.model.ad.fmAd
import com.example.smsSpringTest.model.common.RefToken
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface CommonMapper {

    // url 저장 테이블에 입력한 상대경로 저장하기
    // 기업회원용
    @Insert("""
    <script>
       INSERT INTO formmail_file(
        company_user_id,
         <if test="adImage.adImg != null">ad_img,</if>
         <if test="adImage.logoImg != null">logo_img,</if>
         concept
       ) VALUES (
        #{adImage.companyUserId},
         <if test="adImage.adImg != null">#{adImage.adImg},</if>
         <if test="adImage.logoImg != null">#{adImage.logoImg},</if>
         #{adImage.concept}
       )
       </script>
    """)
    int addUrl(@Param("adImage") fmAd adImage)

//    // db에 저장된 url 삭제
//    @Delete("""
//        DELETE FROM formmail_file
//        WHERE url = #{url}
//    """)
//    int deleteUrl(@Param("url") String url)


    // -------- jwt 토큰 관련 mapper -------------------------

//    // RefreshToken 조회
//    @Select("""
//        SELECT user_id
//                , grant_type
//                , refresh_token
//                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
//                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
//          FROM formmail_user_token
//         WHERE user_id = #{userId}
//           AND use_yn = 'Y'
//    """)
//    RefToken getUserRefreshTokenData(@Param("userId") String userId)
//
//    // RefreshToken 삭제
//    @Delete("""
//        DELETE FROM formmail_user_token
//         WHERE user_id = #{userId}
//    """)
//    int deleteUserToken(@Param("userId") String userId)
//
//    // RefreshToken 토큰 등록
//    @Insert("""
//        INSERT INTO formmail_user_token (
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
//        UPDATE formmail_user_token
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
//          FROM formmail_user_token
//         WHERE user_id = #{userId}
//           AND use_yn = 'Y'
//    """)
//    int getUserRefreshTokenCount(@Param("userId") String userId)
//
//    // 동일한 RefreshToken 확인
//    @Select("""
//        SELECT count(*)
//          FROM formmail_user_token
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
//          FROM formmail_user_token
//         WHERE refresh_token = #{refToken.refreshToken}
//           AND use_yn = 'Y'
//    """)
//    RefToken getRefreshTokenData(@Param("refToken") RefToken refToken)

    // -------- jwt 토큰 관련 admin 버전 mapper -------------------------

    // 폼메일 계정 RefreshToken 전체 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , reg_date
                , upt_date
          FROM formmail_user_token
    """)
    List<RefToken> getFormMailUserRefreshTokenAll()

        // RefreshToken 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
          FROM formmail_admin_token
         WHERE user_id = #{userId}
           AND use_yn = 'Y'
    """)
    RefToken getUserRefreshTokenData(@Param("userId") String userId)

    // RefreshToken 삭제
    @Delete("""
        DELETE FROM formmail_admin_token
         WHERE user_id = #{userId}
    """)
    int deleteUserToken(@Param("userId") String userId)

    // RefreshToken 토큰 등록
    @Insert("""
        INSERT INTO formmail_admin_token (
                user_id
                , grant_type
                , refresh_token
                , use_yn
                , reg_date
                , upt_date
        ) VALUES (
                #{refToken.userId}
                , #{refToken.grantType}
                , #{refToken.refreshToken}
                , 'Y'
                , sysdate()
                , sysdate()
        )
    """)
    int addUserToken(@Param("refToken") RefToken refToken)

    // RefreshToken 업데이트
    @Update("""
        UPDATE formmail_admin_token
           SET refresh_token = #{refToken.refreshToken}
               , grant_type = #{refToken.grantType}
               , upt_date = sysdate()
         WHERE user_id = #{refToken.userId}
           AND use_yn = 'Y'
    """)
    int updateUserToken(@Param("refToken") RefToken refToken)

    // RefreshToken 존재여부 확인
    @Select("""
        SELECT count(*)
          FROM formmail_admin_token
         WHERE user_id = #{userId}
           AND use_yn = 'Y'
    """)
    int getUserRefreshTokenCount(@Param("userId") String userId)

    // 동일한 RefreshToken 확인
    @Select("""
        SELECT count(*)
          FROM formmail_admin_token
         WHERE refresh_token = #{refToken.refreshToken}
           AND use_yn = 'Y'
    """)
    int getRefreshTokenCount(@Param("refToken") RefToken refToken)

    // RefreshToken 으로 정보 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
          FROM formmail_admin_token
         WHERE refresh_token = #{refToken.refreshToken}
           AND use_yn = 'Y'
    """)
    RefToken getRefreshTokenData(@Param("refToken") RefToken refToken)

    // 토큰 있을때 회원 정보 조회
    @Select("""
        SELECT am.user_id
                , am.r_name
                , at.refresh_token
        FROM formmail_admin am
        INNER JOIN formmail_admin_token at on am.user_id = at.user_id
        WHERE am.user_id = #{userId}
    """)
    UserProfile getFrontUserProfile(@Param("userId") String userId)

    // 이메일 인증번호 테이블 전체 비우기 (자정에 실행시킬 예정)
    @Delete("""
        DELETE FROM jobsite_email_code
    """)
    int deleteAllEmailCode()
}