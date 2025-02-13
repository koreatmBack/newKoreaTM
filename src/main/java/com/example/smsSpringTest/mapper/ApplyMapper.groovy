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
           , manager_id
           , apply_name
           , apply_birth
           , apply_gender
           , apply_address
           , apply_phone
           , admin_memo
           , sido
           , sigungu
           , address_detail
           , apply_status
           , apply_path
        ) VALUES (
            #{apply.applyId}
            ,#{apply.aid}
            ,#{apply.cid}
            ,#{apply.managerId}
            ,#{apply.applyName}
            ,#{apply.applyBirth}
            ,#{apply.applyGender}
            ,#{apply.applyAddress}
            ,#{apply.applyPhone}
            ,#{apply.adminMemo}
            ,#{apply.sido}
            ,#{apply.sigungu}
            ,#{apply.addressDetail}
            ,#{apply.applyStatus}
            ,#{apply.applyPath}
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
           <if test="apply.managerId != null"> manager_id = #{apply.managerId},   </if>
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
           <if test="apply.applyStatus != null"> apply_status  = #{apply.applyStatus},   </if>     
           <if test="apply.applyPath != null"> apply_path  = #{apply.applyPath},   </if>     
       </set> 
        WHERE apply_id = #{apply.applyId}
    </script>    
    """)
    int updateApply(@Param("apply") Apply apply)

    // 지원자 전체 조회 (페이징 처리)
    @Select("""
        SELECT *
        FROM formmail_apply
        ORDER BY apply_date DESC
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

    // 지원자 등록할 때 블랙리스트면 등록 방지
    @Select("""
        SELECT count(*)
        FROM formmail_apply
        WHERE apply_name = #{apply.applyName}
        AND apply_birth LIKE CONCAT (#{apply.applyBirth} , '%')
        AND apply_gender = #{apply.applyGender}
        AND apply_phone = #{apply.applyPhone}
        AND apply_status = '블랙리스트'
    """)
    int blackListCheck(@Param("apply") Apply apply)

    // 중복 지원인지 체크 (전체 계열사 중 한 곳이라도 지원한 이력이 있는지)
    @Select("""
        SELECT count(*)
        FROM formmail_apply
        WHERE apply_name = #{apply.applyName}
        AND apply_birth LIKE CONCAT (#{apply.applyBirth} , '%')
        AND apply_gender = #{apply.applyGender}
        AND apply_phone = #{apply.applyPhone} 
    """)
    int dupApplyCheck(@Param("apply") Apply apply)

}

