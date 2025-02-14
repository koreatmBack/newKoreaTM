package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Apply
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
           , apply_career
           , manager_memo
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
            ,#{apply.applyCareer}
            ,#{apply.managerMemo}
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
           <if test="apply.sido != null"> sido  = #{apply.sido},   </if>     
           <if test="apply.sigungu != null"> sigungu  = #{apply.sigungu},   </if>     
           <if test="apply.applyStatus != null"> apply_status  = #{apply.applyStatus},   </if>     
           <if test="apply.applyPath != null"> apply_path  = #{apply.applyPath},   </if>     
           <if test="apply.applyCareer != null"> apply_career  = #{apply.applyCareer},   </if>     
           <if test="apply.managerMemo != null"> manager_memo  = #{apply.managerMemo},   </if>     
       </set> 
        WHERE apply_id = #{apply.applyId}
    </script>    
    """)
    int updateApply(@Param("apply") Apply apply)

    // 지원자 전체 조회 (페이징 처리)
    @Select("""
<script>
        SELECT *
        FROM formmail_apply
        WHERE 1=1
        <if test="apply.managerId != null">AND manager_id = #{apply.managerId}</if>
        <choose>
            <when test="apply.searchType == '이름'">AND apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND apply_phone = #{apply.searchKeyword} </when>
        
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.interviewSort == '내림차순'"> interview_time DESC </when>
            <when test="apply.interviewSort == '오름차순'"> interview_time ASC </when>
            <otherwise> apply_date DESC </otherwise>
        </choose>
        LIMIT #{apply.size} OFFSET #{apply.offset}
</script>                
    """)
    List<Apply> applyList(@Param("apply") Apply apply)

    // 지원자 전체 수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_apply
        WHERE 1=1
        <if test="apply.managerId != null">AND manager_id = #{apply.managerId}</if>
        <choose>
            <when test="apply.searchType == '이름'">AND apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND apply_phone = #{apply.searchKeyword} </when>
            
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.interviewSort == '내림차순'"> interview_time DESC </when>
            <when test="apply.interviewSort == '오름차순'"> interview_time ASC </when>
            <otherwise> apply_date DESC </otherwise>
        </choose>
</script>          
    """)
    int applyListCount(@Param("apply") Apply apply)

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

    // 지원자 입력시, 지원자 이력 반환
    @Select("""
        SELECT *
        FROM formmail_apply
        WHERE apply_name = #{apply.applyName}
        AND apply_birth LIKE CONCAT (#{apply.applyBirth} , '%')
        AND apply_gender = #{apply.applyGender}
        AND apply_phone = #{apply.applyPhone}
        ORDER BY apply_date DESC 
    """)
    List<Apply> applyHistory(@Param("apply") Apply apply)

}

