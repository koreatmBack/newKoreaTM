package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.Member
import com.example.smsSpringTest.model.common.JwtUser
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : jwt 회원 Mapper
 */
@Mapper
interface MemberMapper {

    // 회원가입
    @Insert("""
        INSERT INTO formmail_user_profile (
                user_id
                , user_pwd
                , user_name
                , phone
                , birth
                , email
                , main_addr
                , gender
                , role
                , point
                , agree_yn
                , use_status
                , promo_yn
                , point_expiry_date
                , agree_date
                , reg_date
                , upt_date
        ) VALUES (
                #{user.userId}
                , #{user.userPwd}
                , #{user.userName}
                , #{user.phone}
                , #{user.birth}
                , #{user.email}
                , #{user.mainAddr}
                , #{user.gender}
                , #{user.role}
                , #{user.point}
                , #{user.agreeYn}
                , 'Y'
                , #{user.promoYn}
                , #{user.pointExpiryDate}
                , sysdate()
                , sysdate()
                , sysdate()
        )
    """)
    int signUp(@Param("user") JwtUser user)


    // 회원 화면 정보 조회
    @Select("""
        SELECT up.user_id
                , up.user_name
                , up.point
                , ut.refresh_token
          FROM formmail_user_profile up
         INNER JOIN formmail_user_token ut on up.user_id = ut.user_id
         WHERE up.user_id = #{userId}
           AND up.use_status = 'Y'
    """)
    Member getFrontUserProfile(@Param("userId") String userId)
}