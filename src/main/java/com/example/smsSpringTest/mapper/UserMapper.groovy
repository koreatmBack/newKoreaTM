package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.UserProfile
import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.entity.PhoneNum
import com.example.smsSpringTest.model.User
import com.example.smsSpringTest.model.findUser
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {

    // 회원등록
    @Insert("""
        INSERT INTO formmail_admin (
            user_id
            , user_pwd
            , r_name
            , user_name
            , position
            , admin
            , team
            , m_phone
            , r_phone
        ) VALUES (
            #{user.userId}
            , #{user.userPwd}
            , #{user.rName}
            , #{user.userName}
            , #{user.position}
            , #{user.admin}
            , #{user.team}
            , #{user.mPhone}
            , #{user.rPhone}
        )
    """)
    int signUp(@Param("user") UserProfile user)

    // 아이디 중복처리
    @Select("""
        SELECT count(*) 
         FROM formmail_admin
         WHERE user_id = #{userId}
    """)
    int userDuplicatedChkId(@Param("userId") String userId)

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
        , admin
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
            , admin
            , team
            , m_phone
            , r_phone
        FROM formmail_admin
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<UserProfile> userProfileList(@Param("paging") Paging paging)

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
            , admin
            , team
            , m_phone
            , r_phone
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    UserProfile findOneUser(@Param("userId") String userId)

    // 회원 수정
    @Update("""
        UPDATE formmail_admin
        SET user_pwd = #{user.userPwd}
            , r_name = #{user.rName}
            , user_name = #{user.userName}
            , position = #{user.position}
            , admin = #{user.admin}
            , team = #{user.team}
            , m_phone = #{user.mPhone}
            , r_phone = #{user.rPhone}
        WHERE user_id = #{user.userId}
    """)
    int updateUser(@Param("user") UserProfile user)

    // id가 일치할때 그 회원의 모든 db값 반환
    @Select("""
        SELECT *
        FROM formmail_admin
        WHERE user_id = #{userId}
    """)
    UserProfile userProfile(@Param("userId") String userId)

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
        , admin
        , team
        , m_phone
        , r_phone
    FROM formmail_admin
    WHERE user_name LIKE CONCAT('%', #{name}, '%')
    OR r_name LIKE CONCAT('%', #{name}, '%')
    """)
    List<UserProfile> findUsers(@Param("name") String name)


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



}