package com.example.smsSpringTest.mapper.cafecon

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.cafecon.CafeUser
import com.example.smsSpringTest.model.cafecon.Coupon
import com.example.smsSpringTest.model.cafecon.LogResult
import com.example.smsSpringTest.model.cafecon.PointLog
import com.example.smsSpringTest.model.cafecon.TotalResult
import org.apache.ibatis.annotations.*
/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 카페콘(신규 몰) Mapper
 */
@Mapper
interface CafeconUserMapper {

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
            , business_license_name
            , business_status
            , business_sector
            , business_address
            , owner_name
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
            , #{user.businessLicenseName}
            , #{user.businessStatus}
            , #{user.businessSector}
            , #{user.businessAddress}
            , #{user.ownerName}
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
    String dupPwd(@Param("user") CafeUser user)

    // 로그인시 use_status N인지 체크 = N일때는 탈퇴 회원이기 때문
    @Select("""
        SELECT count(*)
        FROM cafecon_user
        WHERE user_id = #{userId}
        AND use_status = 'N'
    """)
    int delUserCheck(@Param("userId") String userId)

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
        AND use_status = 'Y'
    """)
    CafeUser findOneCafeconLoginUser(@Param("userId") String userId)

    // 회원 탈퇴시 DB 삭제가 아닌, use_status 'N'으로 변경
    @Update("""
        UPDATE cafecon_user
        SET use_status = 'N'
            , role = "DELETE"
            , del_date = sysdate()
        WHERE user_id = #{userId}
    """)
    int deleteUser(@Param("userId") String userId)

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
            <if test="user.businessAddress != null"> business_address = #{user.businessAddress}, </if>
            <if test="user.businessNo != null"> business_no = #{user.businessNo}, </if>
            <if test="user.businessName != null"> business_name = #{user.businessName}, </if>
            <if test="user.businessEmail != null"> business_email = #{user.businessEmail}, </if>
            <if test="user.businessLicense != null"> business_license = #{user.businessLicense}, </if>
            <if test="user.businessLicenseName != null"> business_license_name = #{user.businessLicenseName}, </if>
            <if test="user.businessStatus != null"> business_status = #{user.businessStatus}, </if>
            <if test="user.businessSector != null"> business_sector = #{user.businessSector}, </if>
            <if test="user.ownerName != null"> owner_name = #{user.ownerName}, </if>
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
         AND use_status = 'Y'
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
        , business_license_name
        , business_status
        , business_sector
        , business_address
        , owner_name
        , agree_terms
        , agree_privacy
        , agree_marketing
        FROM cafecon_user
        WHERE user_id = #{userId}
        AND use_status = 'Y'
    """)
    CafeUser findOneCafUser(@Param("userId") String userId)

    // 회원 전체 수 조회
    @Select("""
<script>
        SELECT count(*)
        from cafecon_user
        WHERE use_status = 'Y'
        <if test="user.searchType == 'userId'">AND user_id = #{user.searchKeyword} </if>
        <if test="user.searchType == 'companyName'">AND company_name = #{user.searchKeyword} </if>
        <if test="user.searchType == 'managerName'">AND manager_name = #{user.searchKeyword} </if>
        <if test="user.searchType == 'phone'">AND phone = #{user.searchKeyword} </if>
        <if test="user.searchType == 'businessNo'">AND business_no = #{user.searchKeyword} </if>
        <if test="user.searchType == 'businessName'">AND business_name LIKE CONCAT('%', #{user.searchKeyword}, '%')  </if>
        <if test="user.searchType == 'businessEmail'">AND business_email LIKE CONCAT('%', #{user.searchKeyword}, '%') </if>
</script>    
    """)
    int getUserListCount(@Param("user") CafeUser user)

    // 전체 회원 리스트
    @Select("""
<script>
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
        , business_license_name
        , business_status
        , business_sector
        , business_address
        , owner_name
        , agree_terms
        , agree_privacy
        , agree_marketing
        , created_at
        FROM cafecon_user
        WHERE use_status = 'Y'
        <if test="user.searchType == 'userId'">AND user_id = #{user.searchKeyword} </if>
        <if test="user.searchType == 'companyName'">AND company_name = #{user.searchKeyword} </if>
        <if test="user.searchType == 'managerName'">AND manager_name = #{user.searchKeyword} </if>
        <if test="user.searchType == 'phone'">AND phone = #{user.searchKeyword} </if>
        <if test="user.searchType == 'businessNo'">AND business_no = #{user.searchKeyword} </if>
        <if test="user.searchType == 'businessName'">AND business_name LIKE CONCAT('%', #{user.searchKeyword}, '%')  </if>
        <if test="user.searchType == 'businessEmail'">AND business_email LIKE CONCAT('%', #{user.searchKeyword}, '%')  </if>
        ORDER BY created_at DESC
        LIMIT #{user.size} OFFSET #{user.offset}
</script>        
    """)
    List<CafeUser> cafeconUserList(@Param("user") CafeUser user)

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
        SELECT cc.user_id
              ,cc.tr_id
              ,cc.phone
              ,cc.goods_code
              ,cc.goods_name
              ,cc.goods_img_b
              ,cc.reg_date
              ,cc.limit_date
              ,IF(cp.cancel_date = '0000-00-00 00:00:00', NULL, cp.cancel_date) AS cancel_date
              ,cp.resend_cnt
        FROM cafecon_coupon cc
        LEFT JOIN cafecon_point_log cp ON cp.tr_id = cc.tr_id
        WHERE cc.user_id = #{userId}
        AND cc.success_yn = 'Y'
        ORDER BY cc.reg_date DESC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<Coupon> userCouponList(@Param("paging") Paging paging, @Param("userId") String userId)

    // 회원의 쿠폰(기프티콘) 구매 내역 개수
    @Select("""
        SELECT count(*)
        FROM cafecon_coupon cc
        LEFT JOIN cafecon_point_log cp ON cp.tr_id = cc.tr_id
        WHERE cc.user_id = #{userId}
        AND cc.success_yn = 'Y'
    """)
    int countUserCouponList(@Param("userId") String userId)

    // 회원의 쿠폰 구매, 취소, 선물 내역 조회
    @Select("""
<script>
        SELECT cp.reg_date
              ,cp.order_no
              ,cp.log_type
              ,cc.memo
              ,cc.phone
              ,cc.goods_name
              ,cp.point
              ,cc.tr_id
              ,IF(cp.cancel_date = '0000-00-00 00:00:00', NULL, cp.cancel_date) AS cancel_date
              ,cp.resend_cnt
        FROM cafecon_point_log cp
        LEFT JOIN cafecon_coupon cc ON cc.order_no = cp.order_no
        WHERE cp.user_id = #{user.userId}
        AND cp.log_type IN ('CP', 'CE' ,'GI')
        <if test="user.searchType == 'memo'"> AND cc.memo LIKE CONCAT('%', #{user.searchKeyword}, '%') </if>
        <if test="user.searchType == 'orderNo'"> AND cp.order_no = #{user.searchKeyword}</if>
        <if test="user.searchType == 'phone'"> AND cc.phone = #{user.searchKeyword}</if>
        <if test="user.searchType == 'goodsName'"> AND cc.goods_name LIKE CONCAT('%', #{user.searchKeyword}, '%') </if>      
        <if test="user.startDate != null"> AND cp.reg_date BETWEEN #{user.startDate} AND DATE_ADD(#{user.endDate}, INTERVAL 1 DAY) - INTERVAL 1 SECOND </if>
        ORDER BY cp.reg_date DESC
        LIMIT #{user.size} OFFSET #{user.offset}
</script>        
    """)
    List<PointLog> userPointLogList(@Param("user") CafeUser user)

    // 회원의 포인트 지급 및 차감 내역 총 개수
    @Select("""
        SELECT COUNT(*)
        FROM cafecon_point_log cp
        LEFT JOIN cafecon_coupon cc ON cc.order_no = cp.order_no
        WHERE cp.user_id = #{user.userId}
        AND cp.log_type IN ('CP', 'CE' ,'GI')
    """)
    int countUserPointLogList(@Param("user") CafeUser user)

    // 회원의 충전 내역 조회 관리자 지급, 차감만
    @Select("""
        SELECT reg_date
              ,order_no
              ,point
              ,log_type
        FROM cafecon_point_log
        WHERE user_id = #{user.userId}
        AND log_type IN ('AP', 'AD')
        ORDER BY reg_date DESC
        LIMIT #{user.size} OFFSET #{user.offset}
    """)
    List<PointLog> userChargeList(@Param("user") CafeUser user)

    // 회원의 충전 내역 수
    @Select("""
        SELECT count(*)
        FROM cafecon_point_log
        WHERE user_id = #{user.userId}
        AND log_type IN ('AP', 'AD')
    """)
    int countUserChargeList(@Param("user") CafeUser user)

    // 아이디 찾기시 가입된 id인지 체킹
    @Select("""
        SELECT user_id
              ,use_status
              ,created_at
        FROM cafecon_user
        WHERE manager_name = #{user.managerName}
        AND phone = #{user.phone}
    """)
    CafeUser findCafUserIdBeforeCert(@Param("user") CafeUser user)

    // 이미 가입된 연락처인지 체킹하는 API
    @Select("""
        SELECT count(*)
        FROM cafecon_user
        WHERE phone = #{phone}
    """)
    int dupCheckPhone(@Param("phone") String phone)

    // 비밀번호 찾기시 가입된 id인지 체킹
    @Select("""
        SELECT user_id
              ,use_status
              ,created_at
        FROM cafecon_user
        WHERE manager_name = #{user.managerName}
        AND phone = #{user.phone}
        AND user_id = #{user.userId}
    """)
    CafeUser findCafUserPwdBeforeCert(@Param("user") CafeUser user)

    // 쿠폰 구매 이력 조회
    @Select("""
        SELECT point
        FROM cafecon_point_log
        WHERE user_id = #{user.userId}
        AND log_type = 'CP'
        AND reg_date BETWEEN #{user.startDate} AND DATE_ADD(#{user.endDate}, INTERVAL 1 DAY) - INTERVAL 1 SECOND 
    """)
    List<PointLog> cpPointLogList(@Param("user") CafeUser user)

    // 시작일 ~ 종료일 사이에서 log_type 별로 일마다 포인트 합산 후 type별로 리턴
    @Select("""
        SELECT point
              ,log_type
        FROM cafecon_point_log
        WHERE reg_date BETWEEN #{user.startDate} AND DATE_ADD(#{user.endDate}, INTERVAL 1 DAY) - INTERVAL 1 SECOND
    """)
    List<PointLog> allPointLog(@Param("user") CafeUser user)

    // 관리자전용 날짜별 포인트 지급액, 사용액 조회
    @Select("""
         <script>
         SELECT DATE_FORMAT(log.reg_date, '%Y-%m-%d') AS reg_date
                , SUM(CASE WHEN log.gubun = 'P' THEN log.point ELSE 0 END) AS plusPnt
                , SUM(CASE WHEN log.gubun = 'D' OR log.gubun = 'B' THEN log.point ELSE 0 END) AS miunsPnt
                , SUM(CASE WHEN log.log_type = 'AP' THEN log.point ELSE 0 END) as apPnt
                , SUM(CASE WHEN log.log_type = 'AD' THEN log.point ELSE 0 END) as adPnt
                , SUM(CASE WHEN log.log_type = 'GI' THEN log.point ELSE 0 END) as giPnt
                , SUM(CASE WHEN log.log_type = 'CP' THEN log.point ELSE 0 END) as cpPnt
                , SUM(CASE WHEN log.log_type = 'CE' THEN log.point ELSE 0 END) as cePnt
           FROM cafecon_point_log log
          WHERE 1=1
          <if test="pointLog.startDate != null and pointLog.startDate != '' ">
            AND log.reg_date BETWEEN date(#{pointLog.startDate}) AND DATE_ADD(date(#{pointLog.endDate}), INTERVAL 1 DAY) - INTERVAL 1 SECOND
          </if>
          GROUP BY DATE_FORMAT(log.reg_date, '%Y-%m-%d')
          ORDER BY reg_date DESC
          </script>
    """)
    List<LogResult> getAdminDateTotalPoint(@Param("pointLog") PointLog pointLog)

    // 관리자 전용 날짜별 포인트 지급액, 사용액 합계 조회
    @Select("""
        <script>
        SELECT SUM(plusPnt) AS totalPlusPnt
            , SUM(miunsPnt) AS totalMiunsPnt
            , SUM(apPnt) AS totalApPnt
            , SUM(adPnt) AS totalAdPnt
            , SUM(cpPnt) AS totalCpPnt
            , SUM(cePnt) AS totalCePnt
            , SUM(giPnt) AS totalGiPnt
          FROM (
            SELECT DATE_FORMAT(log.reg_date, '%Y-%m-%d') AS reg_date
                , SUM(CASE WHEN log.gubun = 'P' THEN log.point ELSE 0 END) AS plusPnt
                , SUM(CASE WHEN log.gubun = 'D' OR log.gubun = 'B' THEN log.point ELSE 0 END) AS miunsPnt
                , SUM(CASE WHEN log.log_type = 'AP' THEN log.point ELSE 0 END) as apPnt
                , SUM(CASE WHEN log.log_type = 'AD' THEN log.point ELSE 0 END) as adPnt
                , SUM(CASE WHEN log.log_type = 'GI' THEN log.point ELSE 0 END) as giPnt
                , SUM(CASE WHEN log.log_type = 'CP' THEN log.point ELSE 0 END) as cpPnt
                , SUM(CASE WHEN log.log_type = 'CE' THEN log.point ELSE 0 END) as cePnt
           FROM cafecon_point_log log
           WHERE 1=1
             <if test="pointLog.startDate != null and pointLog.startDate != '' ">
                AND log.reg_date BETWEEN date(#{pointLog.startDate}) AND DATE_ADD(date(#{pointLog.endDate}), INTERVAL 1 DAY) - INTERVAL 1 SECOND
             </if>
             GROUP BY DATE_FORMAT(log.reg_date, '%Y-%m-%d')
             ORDER BY reg_date DESC
          ) AS daily_summary
        </script>
    """)
    TotalResult getTotalPoint(@Param("pointLog") PointLog pointLog)

}