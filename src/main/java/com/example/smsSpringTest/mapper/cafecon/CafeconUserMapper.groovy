package com.example.smsSpringTest.mapper.cafecon

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.cafecon.CafeUser
import com.example.smsSpringTest.model.cafecon.Coupon
import org.apache.ibatis.annotations.*
/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘(신규 몰) Mapper
 */
@Mapper
interface CafeconUserMapper {

    // 회원 화면표시 정보 조회
    @Select("""
        SELECT user_id
                , user_name
                , point
          FROM user_profile
         WHERE user_id = #{cafeUser.userId}
           AND use_status = 'Y'
    """)
    CafeUser getFrontUserProfile(@Param("cafeUser") CafeUser cafeUser)

//    // RefreshToken 유무 조회
//    @Select("""
//        SELECT count(*)
//          FROM user_token
//         WHERE user_id = #{userId}
//           AND use_yn = 'Y'
//    """)
//    int userRefreshTokenChk(@Param("userId") String userId)
//
//    // 로그인 시 토큰 없으면 추가
//    @Insert("""
//        INSERT INTO user_token (
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
//    // 로그인 시 토큰 수정
//    @Update("""
//        UPDATE user_token
//           SET refresh_token = #{refToken.refreshToken}
//               , grant_type = #{refToken.grantType}
//               , upt_date = sysdate()
//         WHERE user_id = #{refToken.userId}
//           AND use_yn = 'Y'
//    """)
//    int updateUserToken(@Param("refToken") RefToken refToken)
//
//    // 기업 회원 포인트 조회
//    @Select("""
//        SELECT point
//          FROM user_profile
//         WHERE user_id = #{bizApi.userId}
//    """)
//    int getCompUserPoint(@Param("bizApi") BizApi bizApi)
//
//    // 기업 회원 전화번호 조회
//    @Select("""
//        SELECT phone
//          FROM user_profile
//         WHERE user_id = #{bizApi.userId}
//    """)
//    String getCompUserPhone(@Param("bizApi") BizApi bizApi)



//    // 회원 포인트 업데이트
//    @Update("""
//        UPDATE user_profile
//           SET point = #{cafeUser.point}
//                , upt_date = sysdate()
//         WHERE user_id = #{cafeUser.userId}
//    """)
//    int userPointManage(@Param("cafeUser") CafeUser cafeUser)

    // 2025-01-14 ~



    // 2025-01-17 ~

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ 카페콘 회원 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 카페콘 회원 가입
    @Insert("""
        INSERT INTO cafecon_user(
            user_id
            , user_pwd
            , company_name
            , manager_name
            , phone
            , business_no
            , business_name
            , business_email
            , business_license
            , agree_terms
            , agree_privacy
            , agree_marketing
        ) VALUES (
            #{user.userId}
            , #{user.userPwd}
            , #{user.companyName}
            , #{user.managerName}
            , #{user.phone}
            , #{user.businessNo}
            , #{user.businessName}
            , #{user.businessEmail}
            , #{user.businessLicense}
            , #{user.agreeTerms}
            , #{user.agreePrivacy}
            , #{user.agreeMarketing}
        )
    """)
    int cafeconSignUp(@Param("user") CafeUser user)

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인
    @Select("""
        SELECT count(*)
        FROM cafecon_user
        WHERE user_id = #{userId}
    """)
    int checkId(@Param("userId") String userId)

    // 암호화된 비밀번호 반환 ( 로그인시 비밀번호 체크용 )
    @Select("""
        SELECT user_pwd
        FROM cafecon_user
        WHERE user_id = #{user.userId}
    """)
    String dupPwd(@Param("user") CafeUser user);

    // 회원 한명(id, 이름, 연락처) 반환
    @Select("""
        SELECT user_id
        , manager_name
        , phone
        , point
        , company_name
        , role
        FROM cafecon_user
        WHERE user_id = #{userId}
    """)
    CafeUser findOneCafeconLoginUser(@Param("userId") String userId)

    // 회원 정보 수정 -> 비밀번호 변경하기 (userId, 기존 pwd, 새로운 pwd)
    @Update("""
        UPDATE cafecon_user
        SET user_pwd = #{user.userPwd}
        WHERE user_id = #{user.userId}
    """)
    int changePwd(@Param("user") CafeUser user)

    // 회원 정보 수정
    @Update("""
<script>
        UPDATE cafecon_user
        <set>
            <if test="user.companyName != null"> company_name = #{user.companyName}, </if>
            <if test="user.managerName != null"> manager_name = #{user.managerName}, </if>
            <if test="user.phone != null"> phone = #{user.phone}, </if>
            <if test="user.businessNo != null"> business_no = #{user.businessNo}, </if>
            <if test="user.businessName != null"> business_name = #{user.businessName}, </if>
            <if test="user.businessEmail != null"> business_email = #{user.businessEmail}, </if>
            <if test="user.businessLicense != null"> business_license = #{user.businessLicense}, </if>
        </set>
        WHERE user_id = #{user.userId}
</script>
    """)
    int updateCafeconUser(@Param("user") CafeUser user)

    // 회원 '포인트' 수정
    @Update("""
        UPDATE cafecon_user
        SET point = #{user.point}
        WHERE user_id = #{user.userId}
    """)
    int updatePoint(@Param("user") CafeUser user)

    // 회원 포인트 조회
    @Select("""
        SELECT point
          FROM cafecon_user
         WHERE user_id = #{userId}
    """)
    int getUserPoint(@Param("userId") String userId)

    // 카페콘 회원 한명 (비밀번호 제외) 조회
    @Select("""
        SELECT user_id
        , company_name
        , manager_name
        , phone
        , point
        , role
        , business_no
        , business_name
        , business_email
        , business_license
        , agree_terms
        , agree_privacy
        , agree_marketing
        FROM cafecon_user
        WHERE user_id = #{userId}
    """)
    CafeUser findOneCafUser(@Param("userId") String userId)

    // 회원 전체 수 조회
    @Select("""
        SELECT count(*)
        from cafecon_user
    """)
    int getUserListCount()

    // 전체 회원 리스트
    @Select("""
        SELECT user_id
        , manager_name
        , company_name
        , point
        , role
        , phone
        , business_no
        , business_name
        , business_email
        , business_license
        , agree_terms
        , agree_privacy
        , agree_marketing
        FROM cafecon_user
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<CafeUser> cafeconUserList(@Param("paging") Paging paging)

    // 회원 권한 체킹하기
    @Select("""
        SELECT role
        FROM cafecon_user
        WHERE user_id = #{userId}
    """)
    String findRole(@Param("userId") String userId)

    // 회원 권한 수정하기
    @Update("""
        UPDATE cafecon_user
        SET role = #{user.role}
        WHERE user_id = #{user.userId}
    """)
    int updateRole(@Param("user") CafeUser user)

    // 회원의 쿠폰(기프티콘) 구매 내역 조회
    @Select("""
        SELECT user_id
              ,tr_id
              ,phone
              ,goods_code
              ,goods_name
              ,goods_img_b
              ,reg_date
              ,limit_date
        FROM cafecon_coupon
        WHERE user_id = #{userId}
        ORDER BY reg_date DESC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<Coupon> userCouponList(@Param("paging") Paging paging, @Param("userId") String userId)

    // 회원의 쿠폰(기프티콘) 구매 내역 개수
    @Select("""
        SELECT count(*)
        FROM cafecon_coupon
        WHERE user_id = #{userId}
        ORDER BY reg_date DESC
    """)
    int countUserCouponList(@Param("userId") String userId)

}