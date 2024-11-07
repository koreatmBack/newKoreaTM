package com.example.smsSpringTest.mapper.jobsite

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.jobsite.CertSMS
import com.example.smsSpringTest.model.jobsite.JobsiteUser
import com.example.smsSpringTest.model.jobsite.Social
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

    // id찾기용
    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인
    @Select("""
        SELECT user_id
        FROM jobsite_user
        WHERE phone = #{phone}
    """)
    String findJobUserId(@Param("phone") String phone)

    // 비밀번호 찾기용
    // userId, userName, phone 일치하는지
    @Select("""
        SELECT count(*)
        FROM jobsite_user
        WHERE user_id = #{user.userId}
        AND user_name = #{user.userName}
        AND phone = #{user.phone}
    """)
    int findJobUserPwd(@Param("user") JobsiteUser user);

    // 비밀번호 업데이트
    @Update("""
        UPDATE jobsite_user
        SET user_pwd = #{user.userPwd}
        WHERE phone = #{user.phone}
    """)
    int updateNewPwd(@Param("user") JobsiteUser user);

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
           <if test="user.favorite != null"> favorite = #{user.favorite},</if>
           <if test="user.clipping != null"> clipping = #{user.clipping},</if>
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
        , favorite
        , clipping
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    JobsiteUser findOneJobUser(@Param("userId") String userId)

    // userId가 일치할때, 즐겨찾기 지우기 (컬럼 null로 변경)
    @Update("""
        UPDATE jobsite_user
        SET favorite = null
        WHERE user_id = #{user.userId}
    """)
    int deleteFavorite(@Param("user") JobsiteUser user)

    // userId가 일치할때, 스크랩 지우기 (컬럼 null로 변경)
    @Update("""
        UPDATE jobsite_user
        SET clipping = null
        WHERE user_id = #{user.userId}
    """)
    int deleteClipping(@Param("user") JobsiteUser user)

    // userId가 일치할때, 즐겨찾기 목록 반환
    @Select("""
        SELECT favorite
        FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    String findFavorite(@Param("user") JobsiteUser user)

    // userId가 일치할때, 스크랩 목록 반환
    @Select("""
        SELECT clipping
        FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    String findClipping(@Param("user") JobsiteUser user)


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

    // 소셜 연동된 userId 찾기
    @Select("""
        SELECT count(*)
        FROM jobsite_user_social
        WHERE user_id = #{userId}
        AND use_status != 'N'
    """)
    int dupSocialUserIdCheck(@Param("userId") String userId)

    // 카카오 로그인 회원 중복 체크
    @Select("""
        SELECT count(*)
        FROM jobsite_user_social
        WHERE social_id = #{id}
        AND use_status != 'N'
    """)
    int dupSocialIdCheck(@Param("id") String id)

    // 카카오 로그인 회원 아이디 조회
    @Select("""
        SELECT user_id
          FROM jobsite_user_social
         WHERE social_id = #{id}
    """)
    String kakaoUserId(@Param("id") String id)

    // 프로모션을 통한 소셜 회원가입
    @Insert("""
        INSERT INTO jobsite_user_social (
                user_id
                , social_type
                , social_id
                , use_status
                , reg_date
                , upt_date
        ) VALUES (
                #{social.userId}
                , #{social.socialType}
                , #{social.socialId}
                , 'Y'
                , sysdate()
                , sysdate()
        )
    """)
    int addSocialData(@Param("social") Social social)
}