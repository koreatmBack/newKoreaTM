package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Apply
import com.example.smsSpringTest.model.InterviewMemo
import org.apache.ibatis.annotations.*

@Mapper
interface ApplyMapper {

    // 시리얼 넘버 생성 전, 이미 있는 시리얼 넘버인지 체킹
    @Select("""
        SELECT count(*)
        FROM formmail_apply
        WHERE aid = #{serialNumber}
    """)
    int dupAidCheck(@Param("serialNumber") String serialNumber)

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
           , sido
           , sigungu
           , address_detail
           , apply_status
           , apply_path
           , apply_career
           , manager_memo
           , manager_name
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
            ,#{apply.sido}
            ,#{apply.sigungu}
            ,#{apply.addressDetail}
            ,#{apply.applyStatus}
            ,#{apply.applyPath}
            ,#{apply.applyCareer}
            ,#{apply.managerMemo}
            ,#{apply.managerName}
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
           <if test="apply.addressDetail != null"> address_detail  = #{apply.addressDetail},   </if>     
           <if test="apply.sido != null"> sido  = #{apply.sido},   </if>     
           <if test="apply.sigungu != null"> sigungu  = #{apply.sigungu},   </if>     
           <if test="apply.applyStatus != null"> apply_status  = #{apply.applyStatus},   </if>     
           <if test="apply.applyPath != null"> apply_path  = #{apply.applyPath},   </if>     
           <if test="apply.applyCareer != null"> apply_career  = #{apply.applyCareer},   </if>     
           <if test="apply.managerMemo != null"> manager_memo  = #{apply.managerMemo},   </if>     
           <if test="apply.managerName != null"> manager_name = #{apply.managerName},   </if>     
           <if test="apply.surveyStatus != null"> survey_status  = #{apply.surveyStatus},   </if>     
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
        <if test="apply.applyStatus != null">AND apply_status = #{apply.applyStatus}</if>
        <if test="apply.applyCareer != null">AND apply_career = #{apply.applyCareer}</if>
        <if test="apply.applyPath != null">AND apply_path = #{apply.applyPath}</if>
        <if test="apply.aid != null">AND aid = #{apply.aid}</if>
        <if test="apply.surveyStatus != null">AND survey_status = #{apply.surveyStatus}</if>                
        <choose>
            <when test="apply.searchType == '이름'">AND apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND REPLACE(apply_phone, '-', '') = REPLACE(#{apply.searchKeyword}, '-', '') </when>
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.surveyStatusSort == '내림차순'"> FIELD(survey_status, '미발송', '발송', '완료'), interview_time DESC </when>
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
        <if test="apply.applyStatus != null">AND apply_status = #{apply.applyStatus}</if>
        <if test="apply.applyCareer != null">AND apply_career = #{apply.applyCareer}</if>
        <if test="apply.applyPath != null">AND apply_path = #{apply.applyPath}</if>        
        <if test="apply.aid != null">AND aid = #{apply.aid}</if>
        <if test="apply.surveyStatus != null">AND survey_status = #{apply.surveyStatus}</if>        
        <choose>
            <when test="apply.searchType == '이름'">AND apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND REPLACE(apply_phone, '-', '') = REPLACE(#{apply.searchKeyword}, '-', '') </when>
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.surveyStatusSort == '내림차순'"> FIELD(survey_status, '미발송', '발송', '완료'), apply_date DESC </when>
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

    // 지원자 채용 현황 변경 버튼 일괄 클릭 -> 변경
    @Update("""
<script>
        UPDATE formmail_apply
        SET apply_status = #{applyStatus}
        WHERE apply_id IN 
        <foreach item="apply" collection="applyIds" open="(" separator="," close=")">
            #{apply.applyId}
        </foreach>
</script>
    """)
    int updateApplyStatus(@Param("applyStatus") String applyStatus , @Param("applyIds") List<Apply> applyIds)

    // 면접일 갱신 버튼 클릭 -> 일괄 변경
    @Update("""
<script>
        UPDATE formmail_apply
        SET apply_status = '익일면접'
        WHERE DATE(STR_TO_DATE(interview_time, '%Y-%m-%d %H:%i')) = DATE_ADD(CURDATE(), INTERVAL 1 DAY)
        AND apply_status = '면접예정'
</script>
    """)
    int updateAllInterview()

    // 면접일 갱신하기 전, 오늘 이전에 당일면접 데이터가 남아있는지 체크
    @Select("""
        SELECT count(*)
        FROM formmail_apply
        WHERE DATE(STR_TO_DATE(interview_time, '%Y-%m-%d %H:%i')) < CURDATE()
        AND apply_status = '당일면접'
    """)
    int checkTodayInterview();

    // 지원자 삭제 (일괄 삭제까지 가능)
    @Delete("""
<script>
        DELETE FROM formmail_apply
        WHERE apply_id IN 
        <foreach item="apply" collection="applyIds" open="(" separator="," close=")">
            #{apply.applyId}
        </foreach>
</script>        
    """)
    int deleteApply(@Param("applyIds") List<Apply> applyIds)

    // 면접 시간 설정에 따라 채용현황 자동 변환 (당일면접, 익일면접, 면접예정)
    // 면접 시간 선택 후 설정 버튼 클릭시 사용할 API임
    @Update("""
        UPDATE formmail_apply
        SET interview_time = #{apply.interviewTime}
           ,apply_status = #{apply.applyStatus}
        WHERE apply_id = #{apply.applyId}   
    """)
    int editInterviewTime(@Param("apply") Apply apply)

    // 면접 질의서 url 저장
    @Update("""
        UPDATE formmail_apply
        SET survey = #{apply.survey}
        WHERE apply_id = #{apply.applyId}
    """)
    int addSurvey(@Param("apply") Apply apply)

    // 면접 질의서 있는지 체킹
    @Select("""
        SELECT survey
        FROM formmail_apply
        WHERE apply_id = #{apply.applyId}
    """)
    String survey(@Param("apply") Apply apply)

    // ----- managerId가 일치할 때 formNo, rName, userName, rank 도 보내기 위함 --------
    // 지원자 전체 조회 (페이징 처리)
    @Select("""
<script>
        SELECT fa.*
              ,fd.form_no
              ,fd.r_name
              ,fd.user_name
              ,fd.rank
        FROM formmail_apply fa
        LEFT JOIN formmail_admin fd ON fd.user_id = fa.manager_id 
        WHERE 1=1
        <if test="apply.managerId != null">AND fa.manager_id = #{apply.managerId}</if>
        <if test="apply.applyStatus != null">AND fa.apply_status = #{apply.applyStatus}</if>
        <if test="apply.applyCareer != null">AND fa.apply_career = #{apply.applyCareer}</if>
        <if test="apply.applyPath != null">AND fa.apply_path = #{apply.applyPath}</if>
        <if test="apply.aid != null">AND fa.aid = #{apply.aid}</if>
        <if test="apply.surveyStatus != null">AND fa.survey_status = #{apply.surveyStatus}</if>                
        <choose>
            <when test="apply.searchType == '이름'">AND fa.apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND REPLACE(fa.apply_phone, '-', '') = REPLACE(#{apply.searchKeyword}, '-', '') </when>
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.surveyStatusSort == '내림차순'"> FIELD(fa.survey_status, '미발송', '발송', '완료'), fa.interview_time DESC </when>
            <when test="apply.interviewSort == '내림차순'"> fa.interview_time DESC </when>
            <when test="apply.interviewSort == '오름차순'"> fa.interview_time ASC </when>
            <otherwise> fa.apply_date DESC </otherwise>
        </choose>
        LIMIT #{apply.size} OFFSET #{apply.offset}
</script>                
    """)
    List<Apply> applyListSameManagerId(@Param("apply") Apply apply)

    // 지원자 전체 수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_apply fa
        LEFT JOIN formmail_admin fd ON fd.user_id = fa.manager_id 
        WHERE 1=1
        <if test="apply.managerId != null">AND fa.manager_id = #{apply.managerId}</if>
        <if test="apply.applyStatus != null">AND fa.apply_status = #{apply.applyStatus}</if>
        <if test="apply.applyCareer != null">AND fa.apply_career = #{apply.applyCareer}</if>
        <if test="apply.applyPath != null">AND fa.apply_path = #{apply.applyPath}</if>        
        <if test="apply.aid != null">AND fa.aid = #{apply.aid}</if>
        <if test="apply.surveyStatus != null">AND fa.survey_status = #{apply.surveyStatus}</if>        
        <choose>
            <when test="apply.searchType == '이름'">AND fa.apply_name = #{apply.searchKeyword} </when>
            <when test="apply.searchType == '연락처'">AND REPLACE(fa.apply_phone, '-', '') = REPLACE(#{apply.searchKeyword}, '-', '') </when>
        </choose>
        ORDER BY 
        <choose>
            <when test="apply.surveyStatusSort == '내림차순'"> FIELD(fa.survey_status, '미발송', '발송', '완료'), fa.apply_date DESC </when>
            <when test="apply.interviewSort == '내림차순'"> fa.interview_time DESC </when>
            <when test="apply.interviewSort == '오름차순'"> fa.interview_time ASC </when>
            <otherwise> fa.apply_date DESC </otherwise>
        </choose>
        
</script>          
    """)
    int applyListSameManagerIdCount(@Param("apply") Apply apply)

    // ------------------------ ---------------------------------

    // 인터뷰 메모 등록하기
    @Insert("""
        INSERT INTO interview_memo (
             mid
            ,apply_id
            ,content
            ,manager_id
            ,manager_name
        ) VALUES (
            #{im.mid}
            ,#{im.applyId}
            ,#{im.content}
            ,#{im.userId}
            ,#{im.rName}
        )
    """)
    int addInterviewMemo(@Param("im") InterviewMemo im)

    // 인터뷰 메모 mid 중복 체크
    @Select("""
        SELECT count(*)
        FROM interview_memo
        WHERE mid = #{mid}
    """)
    int dupMidCheck(@Param("mid") String mid)

    // 지원자 테이블 인터뷰 메모 컬럼에도 추가
    @Update("""
        Update formmail_apply 
        SET interview_memo = #{im.content}
        WHERE apply_id = #{im.applyId}
    """)
    int addAndUptInterviewMemo(@Param("im") InterviewMemo im)

    // 면접 메모 조회하기
    @Select("""
        SELECT *
        FROM interview_memo
        ORDER BY reg_date DESC
        LIMIT #{im.size} OFFSET #{im.offset} 
    """)
    List<InterviewMemo> interviewMemoList(@Param("im") InterviewMemo im)

    // 면접 메모 총 개수
    @Select("""
        SELECT count(*)
        FROM interview_memo
    """)
    int interviewMemoListCount()

}

