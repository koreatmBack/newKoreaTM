package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.cafecon.BizApi
import com.example.smsSpringTest.model.cafecon.CafeUser
import com.example.smsSpringTest.model.cafecon.CafeCoupon
import com.example.smsSpringTest.model.cafecon.PointLog
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘(신규 몰) Mapper
 */
@Mapper
interface CafeConMapper {

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

    // tr_id 값 생성
    @Select("""
        SELECT CONCAT('cafe_', DATE_FORMAT(CURDATE(), '%Y%m%d'), '_', LPAD(CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) + 1, 8, '0')) AS tr_id
          FROM cafecon_coupon
         WHERE SUBSTRING_INDEX(SUBSTRING_INDEX(tr_id, '_', 2), '_', -1) = DATE_FORMAT(CURDATE(), '%Y%m%d')
         ORDER BY CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) DESC
         LIMIT 1
    """)
    String getTrId();

    // 로컬 전용 tr_id 값 생성
    @Select("""
        SELECT CONCAT('lafe_', DATE_FORMAT(CURDATE(), '%Y%m%d'), '_', LPAD(CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) + 1, 8, '0')) AS tr_id
          FROM cafecon_coupon
         WHERE SUBSTRING_INDEX(SUBSTRING_INDEX(tr_id, '_', 2), '_', -1) = DATE_FORMAT(CURDATE(), '%Y%m%d')
         ORDER BY CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) DESC
         LIMIT 1
    """)
    String localGetTrId();

    // 사용자가 구매한 쿠폰 저장
    @Insert("""
        INSERT INTO cafecon_coupon (
                user_id
                , tr_id
                , phone
                , order_no
                , goods_code
                , goods_name
                , goods_img_b
                , memo
                , success_yn
                , use_yn
                , code
                , message
                , limit_date
                , reg_date
                , upt_date
        ) VALUES (
                #{cafeCoupon.userId}
                , #{cafeCoupon.trId}
                , #{cafeCoupon.phone}
                , #{cafeCoupon.orderNo}
                , #{cafeCoupon.goodsCode}
                , #{cafeCoupon.goodsName}
                , #{cafeCoupon.goodsImgB}
                , #{cafeCoupon.memo}
                , 'Y'
                , 'Y'
                , #{cafeCoupon.code}
                , #{cafeCoupon.message}
                , #{cafeCoupon.limitDate}
                , sysdate()
                , sysdate()
        )
    """)
    int addCompUserCoupon(@Param("cafeCoupon") CafeCoupon cafeCoupon)

    // 회원 포인트 업데이트
    @Update("""
        UPDATE user_profile
           SET point = #{cafeUser.point}
                , upt_date = sysdate()
         WHERE user_id = #{cafeUser.userId}
    """)
    int userPointManage(@Param("cafeUser") CafeUser cafeUser)

    // 사용자가 구매 실패한 쿠폰 저장
    @Insert("""
        INSERT INTO cafecon_coupon (
                user_id
                , order_no
                , tr_id
                , phone
                , goods_code
                , success_yn
                , use_yn
                , code
                , message
                , reg_date
                , upt_date
        ) VALUES (
                #{cafeCoupon.userId}
                , #{cafeCoupon.orderNo}
                , #{cafeCoupon.trId}
                , #{cafeCoupon.phone}
                , #{cafeCoupon.goodsCode}
                , 'N'
                , 'Y'
                , #{cafeCoupon.code}
                , #{cafeCoupon.message}
                , sysdate()
                , sysdate()
        )
    """)
    int addFailUserCoupon(@Param("cafeCoupon") CafeCoupon cafeCoupon)

    // 회원 포인트 지급/차감 로그 생성
    @Insert("""
        INSERT INTO cafecon_point_log (
                user_id
                , gubun
                , goods_name
                , discount_price
                , point
                , curr_point
                , log_type
                , use_status
                , reg_date
                , upt_date
        ) VALUES (
                #{pointlog.userId}
                , #{pointlog.gubun}
                , #{pointlog.goodsName}
                , #{pointlog.discountPrice}
                , #{pointlog.point}
                , #{pointlog.currPoint}
                , #{pointlog.logType}
                , 'Y'
                , sysdate()
                , sysdate()
        )
    """)
    int addCompUserPointLog(@Param("pointlog") PointLog pointLog)

    // 쿠폰 trId 있는지 확인
    @Select("""
        SELECT count(*)
          FROM cafecon_coupon
         WHERE tr_id = #{cafeCoupon.trId}
    """)
    int tridCountChk(@Param("cafeCoupon") CafeCoupon cafeCoupon)

    // 구매 쿠폰 삭제
    @Update("""
        UPDATE cafecon_coupon
           SET success_yn = 'N'
                , code = #{cafeCoupon.code}
                , message = #{cafeCoupon.message}
         WHERE tr_id = #{cafeCoupon.trId}
    """)
    int deleteUserCoupon(@Param("cafeCoupon") CafeCoupon cafeCoupon)

    // 2025-01-14 ~

    // 본인이 구매한 상품인지 확인
    @Select("""
        SELECT COUNT(*)
          FROM cafecon_coupon
         WHERE user_id = #{bizApi.userId}
           AND tr_id = #{bizApi.trId}
    """)
    int isUserCouponCheck(@Param("bizApi") BizApi bizApi)

    // trId 로 상품 정보 상세 조회
    @Select("""
        SELECT user_id
                , tr_id
                , phone
                , order_no
                , goods_code
                , goods_name
                , goods_img_b
                , memo
                , success_yn
                , use_yn
                , code
                , message
                , limit_date
                , reg_date
                , upt_date
          FROM cafecon_coupon
         WHERE tr_id = #{trId}
    """)
    CafeCoupon getTrIdCouponData(@Param("trId") String trId)

    // 상품 가격 조회
    @Select("""
        SELECT goods_code
                , discount_price
                , real_price
                , sale_price
          FROM goods_item
         WHERE goods_code = #{goodsCode}
    """)
    CafeCoupon getGoodsPriceData(@Param("goodsCode") String goodsCode)

    // 상품 가격 조회
    @Select("""
        SELECT real_price
          FROM goods_item
         WHERE goods_code = #{bizApi.goodsCode}
    """)
    int getItemPrice(@Param("bizApi") BizApi bizApi)
}