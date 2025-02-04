package com.example.smsSpringTest.mapper.cafecon

import com.example.smsSpringTest.model.cafecon.BizApi
import com.example.smsSpringTest.model.cafecon.CafeCoupon
import com.example.smsSpringTest.model.cafecon.PointLog
import com.example.smsSpringTest.model.common.RefToken
import org.apache.ibatis.annotations.*
/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘(신규 몰) Common Mapper
 */
@Mapper
interface CafeconCommonMapper {

    // 카페콘 회원 RefreshToken 전체 조회
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

    // 카페콘 계정 RefreshToken 전체 조회
    @Select("""
        SELECT user_id
                , grant_type
                , refresh_token
                , reg_date
                , upt_date
          FROM formmail_user_token
    """)
    List<RefToken> getCafeconUserRefreshTokenAll()

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

    // 01-20 기프티콘 전송

    // tr_id 값 생성
    @Select("""
        SELECT CONCAT('cafe_', DATE_FORMAT(CURDATE(), '%Y%m%d'), '_', LPAD(CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) + 1, 8, '0')) AS tr_id
          FROM cafecon_coupon
         WHERE SUBSTRING_INDEX(SUBSTRING_INDEX(tr_id, '_', 2), '_', -1) = DATE_FORMAT(CURDATE(), '%Y%m%d')
         ORDER BY CAST(SUBSTRING_INDEX(tr_id, '_', -1) AS UNSIGNED) DESC
         LIMIT 1
    """)
    String getTrId();

    // point_log 테이블의 order_no이 오늘 날짜로 있는지 체킹하기
    @Select("""
        SELECT order_no
        FROM cafecon_point_log
        WHERE order_no LIKE CONCAT(#{currDate}, '%')
        ORDER BY order_no DESC
        LIMIT 1
    """)
    String findOrderNoPointLog(@Param("currDate") String date)

    // cafecon_coupon 테이블의 order_no이 오늘 날짜로 있는지 체킹하기
    @Select("""
        SELECT order_no
        FROM cafecon_coupon
        WHERE order_no LIKE CONCAT(#{currDate}, '%')
        ORDER BY order_no DESC
        LIMIT 1
    """)
    String findOrderNoCoupon(@Param("currDate") String date)

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
                , real_price
                , discount_price
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
                , #{cafeCoupon.realPrice}
                , #{cafeCoupon.discountPrice}
                , #{cafeCoupon.code}
                , #{cafeCoupon.message}
                , #{cafeCoupon.limitDate}
                , sysdate()
                , sysdate()
        )
    """)
    int addCompUserCoupon(@Param("cafeCoupon") CafeCoupon cafeCoupon)

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
                , real_price
                , discount_price
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
                , #{cafeCoupon.realPrice}
                , #{cafeCoupon.discountPrice}
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
                , order_no
                , use_status
                , tr_id
                , resend_cnt
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
                , #{pointlog.orderNo}
                , 'Y'
                , #{pointlog.trId}
                , #{pointlog.resendCnt}
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
                , real_price
                , discount_price
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

    // orderNo, userId로 tr_id 조회하기
    @Select("""
        SELECT tr_id
        FROM cafecon_point_log
        WHERE user_id = #{userId}
        AND order_no = #{orderNo}
    """)
    String findTrId(@Param("userId") String userId, @Param("orderNo") String orderNo)

    // 취소 내역 있는지 조회
    @Select("""
        SELECT count(*)
        FROM cafecon_point_log
        WHERE tr_id = #{pl.trId}
        AND user_id = #{pl.userId}
        AND log_type = 'CE'
    """)
    int findTrIdCheck(@Param("pl") PointLog pl)

    // point_log 테이블에서 tr_id 일치하는 값 찾아서
    // 취소시 update 하기
    @Update("""
        UPDATE cafecon_point_log
        SET curr_point = #{pl.currPoint}
           ,log_type = 'CE'
           ,gubun = 'P'
           ,cancel_date = sysdate()
           ,upt_date = sysdate()
        WHERE tr_id = #{pl.trId}
    """)
    int cancelCoupon(@Param("pl") PointLog pl)

    // 재전송 횟수 수정하기
    @Update("""
        UPDATE cafecon_point_log
        SET resend_cnt = #{pl.resendCnt}
            ,upt_date = sysdate()
        WHERE tr_id = #{pl.trId}
    """)
    int updateResendCnt(@Param("pl") PointLog pl)

    // 재전송할때 재전송 횟수 가져오기
    @Select("""
        SELECT resend_cnt
        FROM cafecon_point_log
        WHERE tr_id = #{trId}
    """)
    int resendCnt(@Param("trId") String trId)

//    // 관리자 -> 지급관리 탭
//    @Select("""
//        SELECT cd.user_id
//              ,cu.company_name
//              ,cu.manager_name
//              ,cu.phone
//              ,cd.charge_point
//              ,cd.depositor_name
//              ,cd.status
//              ,cd.reg_date
//        FROM cafecon_deposit cd
//        JOIN cafecon_user cu ON cu.user_id = cd.user_id
//    """)
//    List<Deposit>

}