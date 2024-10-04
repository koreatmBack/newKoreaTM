package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Apply
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

@Mapper
interface ApplyMapper {

    // 지원자 등록
    @Insert("""
        Insert INTO formmail_apply(
           apply_id
           , aid
           , cid
           , user_id
           , apply_name
           , gender
           , birth
           , a_phone
           , address
           , applied_time
           , interview_time
           , admin_memo
           , interview_memo
           , last_applied_time
        ) VALUES (
            #{apply.applyId}
            ,#{apply.aid}
            ,#{apply.cid}
            ,#{apply.userId}
            ,#{apply.applyName}
            ,#{apply.gender}
            ,#{apply.birth}
            ,#{apply.aPhone}
            ,#{apply.address}
            ,#{apply.appliedTime}
            ,#{apply.interviewTime}
            ,#{apply.adminMemo}
            ,#{apply.interviewMemo}
            ,#{apply.lastAppliedTime}
        )
    """)
    int addApply(@Param("apply") Apply apply)

    // 지원자 수정
    @Update("""
    <script>
        UPDATE formmail_apply
       <set>
       
           <if test="apply.aid != null"> aid = #{apply.aid},   </if>
           <if test="apply.cid != null">  cid = #{apply.cid},   </if>
           <if test="apply.userId != null"> user_id = #{apply.userId},   </if>
           <if test="apply.gender != null"> gender = #{apply.gender},   </if>
           <if test="apply.address != null"> address = #{apply.address},   </if>
           <if test="apply.appliedTime != null"> applied_time = #{apply.appliedTime},   </if>
           <if test="apply.interviewTime  != null"> interview_time = #{apply.interviewTime},   </if>
           <if test="apply.adminMemo != null"> admin_memo  = #{apply.adminMemo},   </if>
           <if test="apply.interviewMemo  != null"> interview_memo  = #{apply.interviewMemo},   </if>
           <if test="apply.lastAppliedTime  != null"> last_applied_time  = #{apply.lastAppliedTime},   </if>
                     
       </set> 
        WHERE apply_id = #{apply.applyId}
    </script>    
    """)
    int updateApply(@Param("apply") Apply apply)



}