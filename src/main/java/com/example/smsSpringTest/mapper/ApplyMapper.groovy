package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Apply
import com.example.smsSpringTest.model.Paging
import org.apache.ibatis.annotations.*

@Mapper
interface ApplyMapper {

    // 지원자 등록
    @Insert("""
        Insert INTO formmail_apply(
           apply_id
           , aid
           , cid
           , user_id
           , company
           , partner
           , apply_name
           , apply_birth
           , apply_gender
           , apply_address
           , apply_phone
           , interview_time
           , admin_memo
           , sido
           , sigungu
           , address_detail
        ) VALUES (
            #{apply.applyId}
            ,#{apply.aid}
            ,#{apply.cid}
            ,#{apply.userId}
            ,#{apply.company}
            ,#{apply.partner}
            ,#{apply.applyName}
            ,#{apply.applyBirth}
            ,#{apply.applyGender}
            ,#{apply.applyAddress}
            ,#{apply.applyPhone}
            ,#{apply.interviewTime}
            ,#{apply.adminMemo}
            ,#{apply.sido}
            ,#{apply.sigungu}
            ,#{apply.addressDetail}
        )
    """)
    int addApply(@Param("apply") Apply apply)

    // 지원자 수정
    @Update("""
    <script>
        UPDATE formmail_apply
       <set>
           <if test="apply.aid != null"> aid = #{apply.aid},   </if>
           <if test="apply.cid != null"> cid = #{apply.cid},   </if>
           <if test="apply.userId != null"> user_id = #{apply.userId},   </if>
           <if test="apply.company != null"> company = #{apply.company},   </if>
           <if test="apply.partner != null">  partner = #{apply.partner},   </if>
           <if test="apply.applyName != null"> apply_name = #{apply.applyName},   </if>
           <if test="apply.applyBirth != null"> apply_birth = #{apply.applyBirth},   </if>
           <if test="apply.applyGender != null"> apply_gender = #{apply.applyGender},   </if>
           <if test="apply.applyAddress != null"> apply_address = #{apply.applyAddress},   </if>
           <if test="apply.applyPhone  != null"> apply_phone = #{apply.applyPhone},   </if>
           <if test="apply.interviewTime  != null"> interview_time = #{apply.interviewTime},   </if>
           <if test="apply.adminMemo != null"> admin_memo  = #{apply.adminMemo},   </if>
           <if test="apply.addressDetail != null"> address_detail  = #{apply.addressDetail},   </if>     
       </set> 
        WHERE apply_id = #{apply.applyId}
    </script>    
    """)
    int updateApply(@Param("apply") Apply apply)

    // 지원자 전체 조회 (페이징 처리)
    @Select("""
        SELECT *
        FROM formmail_apply
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<Apply> applyList(@Param("paging") Paging paging)

    // 지원자 전체 수
    @Select("""
        SELECT count(*)
        FROM formmail_apply
    """)
    int applyListCount()

    // 지원자 한명 조회 (apply_id 일치하는)
    @Select("""
        SELECT *
        FROM formmail_apply
        WHERE apply_id = #{apply.applyId}
    """)
    List<Apply> findOneApply(@Param("apply") Apply apply)

}

