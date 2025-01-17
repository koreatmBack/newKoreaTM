package com.example.smsSpringTest.mapper.cafecon

import com.example.smsSpringTest.model.common.RefToken
import org.apache.ibatis.annotations.*
/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘(신규 몰) Common Mapper
 */
@Mapper
interface CafeconCommonMapper {

    // 잡사이트 회원 RefreshToken 전체 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , reg_date
                , upt_date
        FROM cafecon_user_token
    """)
    List<RefToken> getJobUserRefreshTokenAll()

    // RefreshToken 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , DATE_FORMAT(reg_date, '%Y-%m-%d') as reg_date
                , DATE_FORMAT(upt_date, '%Y-%m-%d') as upt_date
          FROM cafecon_user_token
         WHERE user_id = #{userId}
           AND use_yn = 'Y'
    """)
    RefToken getUserRefreshTokenData(@Param("userId") String userId)

    // RefreshToken 삭제
    @Delete("""
        DELETE FROM cafecon_user_token
         WHERE user_id = #{userId}
    """)
    int deleteUserToken(@Param("userId") String userId)

    // RefreshToken 토큰 등록
    @Insert("""
        INSERT INTO cafecon_user_token (
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
        UPDATE cafecon_user_token
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
          FROM cafecon_user_token
         WHERE user_id = #{userId}
           AND use_yn = 'Y'
    """)
    int getUserRefreshTokenCount(@Param("userId") String userId)

    // 동일한 RefreshToken 확인
    @Select("""
        SELECT count(*)
          FROM cafecon_user_token
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
          FROM cafecon_user_token
         WHERE refresh_token = #{refToken.refreshToken}
           AND use_yn = 'Y'
    """)
    RefToken getRefreshTokenData(@Param("refToken") RefToken refToken)

//    // 토큰 있을때 회원 정보 조회
//    @Select("""
//        SELECT am.user_id
//                , am.r_name
//                , at.refresh_token
//        FROM cafecon_user cu
//        INNER JOIN cafecon_user_token jt on ju.user_id = jt.user_id
//        WHERE jm.user_id = #{userId}
//    """)
//    JobsiteUser getFrontUserProfile(@Param("userId") String userId)

}