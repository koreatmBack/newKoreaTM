package com.example.smsSpringTest.mapper.jobsite

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.jobsite.BookMark
import com.example.smsSpringTest.model.jobsite.Cert
import com.example.smsSpringTest.model.jobsite.JobsiteUser
import com.example.smsSpringTest.model.jobsite.Social
import org.apache.ibatis.annotations.*

@Mapper
interface JobUserMapper {

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 본인인증 (문자) ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 문자 본인인증 코드 저장할때 이미 인증 받았으면 해당 row에 인증 번호 덮어 씌우기
    @Update("""
        UPDATE jobsite_sms_code
        SET sms_code = #{cert.smsCode}
        WHERE phone = #{cert.phone} 
    """)
    int dupSmsCode(@Param("cert") Cert cert);

    // 문자 본인인증 일치하는지 찾기
    @Select("""
        SELECT count(*)
        FROM jobsite_sms_code
        WHERE user_name = #{cert.userName}
        AND phone = #{cert.phone}
        AND sms_code = #{cert.smsCode}
    """)
    int certUser(@Param("cert") Cert cert);

    // 문자 본인인증 성공시 테이블에서 해당 row 삭제
    @Delete("""
        DELETE FROM jobsite_sms_code
        WHERE phone = #{cert.phone}
    """)
    int deleteSmsCode(@Param("cert") Cert cert);

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 본인인증 (이메일) ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 이메일 인증번호 저장
    @Insert("""
        INSERT INTO jobsite_email_code(
            email
            , email_code
        ) VALUES (
            #{cert.email}
            , #{cert.emailCode}
        )
    """)
    int insertEmailCode(@Param("cert") Cert cert)

    // 이메일 본인인증 코드 저장할 때 이미 인증 받았으면 해당 row에 인증 번호 덮어 씌우기
    // 이메일 인증번호 재요청시
    @Update("""
        UPDATE jobsite_email_code
        SET email_code = #{cert.emailCode}
        WHERE email = #{cert.email}
    """)
    int dupEmailCode(@Param("cert") Cert cert)

    // 이메일 본인인증 일치하는지 찾기
    @Select("""
        SELECT count(*)
        FROM jobsite_email_code
        WHERE email = #{cert.email}
        AND email_code = #{cert.emailCode}
    """)
    int certUserEmail(@Param("cert") Cert cert)

    // 문자 본인인증 성공시 테이블에서 해당 row 삭제
    @Delete("""
        DELETE FROM jobsite_email_code
        WHERE email = #{cert.email}
    """)
    int deleteEmailCode(@Param("cert") Cert cert)


    // 회원 가입
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
            , email
            , agree_over15
            , agree_terms
            , agree_privacy
            , agree_sms_marketing
            , agree_email_marketing
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
            , #{user.email}
            , #{user.agreeOver15}
            , #{user.agreeTerms}
            , #{user.agreePrivacy}
            , #{user.agreeSmsMarketing}
            , #{user.agreeEmailMarketing}
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

    // email 중복 체크하기( 별도의 api )
    @Select("""
        SELECT count(*)
        FROM jobsite_user
        WHERE email = #{email}
    """)
    int checkEmail(@Param("email") String email)

    // 암호화된 비밀번호 반환 ( 로그인시 비밀번호 체크용 )
    @Select("""
        SELECT user_pwd
        FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    String dupPwd(@Param("user") JobsiteUser user);

    // 회원 탈퇴하기
    @Delete("""
        DELETE FROM jobsite_user
        WHERE user_id = #{user.userId}
    """)
    int resignUser(@Param("user") JobsiteUser user);

    // 회원 한명(id, 이름, 연락처) 반환
    @Select("""
        SELECT user_id
        , user_name
        , phone
        , email
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    JobsiteUser findOneJobLoginUser(@Param("userId") String userId)

    // 아이디 찾기 눌렀을때 가입된 아이디인지 확인하기 (userName, phone or email 필수)
    @Select("""
<script>
        SELECT count(*)
        FROM jobsite_user
        WHERE user_name = #{user.userName}
        <if test="user.phone != null"> AND phone = #{user.phone} </if>
        <if test="user.email != null"> AND email = #{user.email} </if>
</script>        
    """)
    int findJobUserIdBeforeCert(@Param("user") JobsiteUser user)

    // 이름,연락처 or 이름 ,이메일 로 id 찾기용
    @Select("""
<script>
        SELECT user_id
        , created_at
        FROM jobsite_user
        WHERE user_name = #{user.userName}
        <if test="user.phone != null"> AND phone = #{user.phone} </if>
        <if test="user.email != null"> AND email = #{user.email} </if>
</script>        
    """)
    JobsiteUser findJobUserId(@Param("user") JobsiteUser user)

//    // 이메일로 id 찾기용
//    @Select("""
//        SELECT user_id
//        , created_at
//        FROM jobsite_user
//        WHERE email = #{email}
//    """)
//    JobsiteUser findJobUserIdFromEmail(@Param("email") String email)

//    // 이메일로 비밀번호 찾기 (email)
//    @Select("""
//        SELECT count(*)
//        FROM jobsite_user
//        WHERE email = #{user.email}
//    """)
//    int findJobUserPwdFromEmail(@Param("user") JobsiteUser user)

    // 이메일,문자로 비밀번호 찾기용
    // userId, userName, phone or email 일치하는지
    @Select("""
<script>
        SELECT count(*)
        FROM jobsite_user
        WHERE user_id = #{user.userId}
        AND user_name = #{user.userName}
        <if test="user.phone != null"> AND phone = #{user.phone} </if>
        <if test="user.email != null"> AND email = #{user.email} </if>
</script>        
    """)
    int findJobUserPwd(@Param("user") JobsiteUser user);

    // 연락처 or 이메일로 비밀번호 업데이트
    @Update("""
<script>
        UPDATE jobsite_user
        SET user_pwd = #{user.userPwd}
        WHERE user_name = #{user.userName}
        <if test="user.phone != null"> AND phone = #{user.phone}</if>
        <if test="user.email != null"> AND email = #{user.email} </if>
</script>        
    """)
    int updateNewPwd(@Param("user") JobsiteUser user)

    // 회원 정보 수정 -> 비밀번호 변경하기 (userId, 기존 pwd, 새로운 pwd)
    @Update("""
        UPDATE jobsite_user
        SET user_pwd = #{user.userPwd}
        WHERE user_id = #{user.userId}
    """)
    int changePwd(@Param("user") JobsiteUser user)

    // 이름 반환
    @Select("""
        SELECT user_name
        FROM jobsite_user
        WHERE user_id = #{userId}
    """)
    String userName(@Param("userId") String userId)

    // 잡사이트 회원 정보 수정 , 비밀번호는 여기서 변경 불가
    @Update("""
<script>
        UPDATE jobsite_user
      <set>
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
           <if test="user.email != null"> email = #{user.email},</if>
           <if test="user.agreeOver15 != null"> agree_over15 = #{user.agreeOver15},</if>
           <if test="user.agreeTerms != null"> agree_terms = #{user.agreeTerms},</if>
           <if test="user.agreePrivacy != null"> agree_privacy = #{user.agreePrivacy},</if>
           <if test="user.agreeSmsMarketing != null"> agree_sms_marketing = #{user.agreeSmsMarketing},</if>
           <if test="user.agreeEmailMarketing != null"> agree_email_marketing = #{user.agreeEmailMarketing},</if>
           <if test="user.agreeDate != null"> agree_date = #{user.agreeDate},</if>
           
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
        , email
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
        , email
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
    String socialUserId(@Param("id") String id)

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

    // 비밀번호 변경시 소셜 회원가입한 유저인지 체크
    @Select("""
        SELECT count(*)
        FROM jobsite_user_social
        WHERE user_id = #{userId}
    """)
    int checkSocialUser(@Param("userId") String userId)


    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 스크랩, 좋아요 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 스크랩 or 좋아요 추가
    @Insert("""
        INSERT INTO jobsite_bookmark(
            user_id
            , aid
            , type
        ) VALUES (
            #{mark.userId}
            , #{mark.aid}
            , #{mark.type}
        )
    """)
    int addBookmark(@Param("mark") BookMark mark)

    // userId , type 일치할 때 스크랩 or 좋아요 전체 조회 (페이징 처리)
    @Select("""
        SELECT *
        FROM jobsite_bookmark
        WHERE user_id = #{mark.userId} 
        AND type = #{mark.type}
        LIMIT #{mark.size} OFFSET #{mark.offset}
    """)
    List<BookMark> bookMarkList(@Param("mark") BookMark mark)

    // userId , type 일치할 때 스크랩 or 좋아요 전체 조회 수
    @Select("""
        SELECT count(*)
        FROM jobsite_bookmark
        WHERE user_id = #{mark.userId} 
        AND type = #{mark.type}
    """)
    int bookMarkListCount(@Param("mark") BookMark mark)

    // userId, type, aid 일치하는게 있는지, 있으면 저장 x
    @Select("""
        SELECT count(*)
        FROM jobsite_bookmark
        WHERE user_id = #{mark.userId}
        AND type = #{mark.type}
        ANd aid = #{mark.aid}
    """)
    int dupBookmarkCheck(@Param("mark") BookMark mark)

    // userId, type, aid 일치할 때 하나 삭제하기
    @Delete("""
        DELETE FROM jobsite_bookmark
        WHERE user_id = #{mark.userId}
        AND type = #{mark.type}
        ANd aid = #{mark.aid}
    """)
    int deleteOne(@Param("mark") BookMark mark)

    // userId, type, aid 일치할 때 전체 삭제하기
    @Delete("""
        DELETE FROM jobsite_bookmark
        WHERE user_id = #{mark.userId}
        AND type = #{mark.type}
    """)
    int deleteAll(@Param("mark") BookMark mark)
}