package com.example.smsSpringTest.mapper


import com.example.smsSpringTest.model.formmail_vo.Company
import com.example.smsSpringTest.model.Paging
import org.apache.ibatis.annotations.*

@Mapper
interface CompanyMapper {

    @Insert("""
        INSERT INTO formmail_company(
            cid
            ,com_gubun
            ,manager_id
            ,com_center
            ,com_gubun
            ,com_name
            ,com_channel
            ,com_spot
            ,com_phone
            ,com_address
            ,head_name
            ,head_phone
            ,leader_name
            ,leader_phone
            ,manager
            ,manager1
            ,manager2
            ,manager3
            ,gen_name
            ,gen_phone
            ,affiliation
            ,head_count
            ,location
            ,work_start
            ,work_end
            ,work_time
            ,lunch_time
            ,rest_time
            ,work_type
            ,park
            ,cafe
            ,com_photo
            ,business
            ,use_db
            ,merch
            ,min_age
            ,max_age
            ,reward
            ,int_type
            ,int_time
            ,int_count
            ,edu_cost
            ,edu_inst
            ,first_pay
            ,guar_pay
            ,guar_period
            ,ave_pay
            ,top_pay
            ,bonus
            ,promo
            ,bond
            ,bond_pay
            ,welfare
            ,welf_etc
            ,prevEmp
            ,empSch
            ,strength
            ,propose
            ,suggest
            ,profile
            ,empAdmin
            ,visitMeet
        ) VALUES (
            #{comp.cid}
            , #{comp.comNameAlias}
            , #{comp.managerId}
            , #{comp.comCenter}
            , #{comp.comGubun}
            , #{comp.comName}
            , #{comp.comChannel}
            , #{comp.comSpot}
            , #{comp.comPhone}
            , #{comp.comAddress}
            , #{comp.headName}
            , #{comp.headPhone}
            , #{comp.leaderName}
            , #{comp.leaderPhone}
            , #{comp.manager}
            , #{comp.manager1}
            , #{comp.manager2}
            , #{comp.manager3}
            , #{comp.genName}
            , #{comp.genPhone}
            , #{comp.affiliation}
            , #{comp.headCount}
            , #{comp.location}
            , #{comp.workStart}
            , #{comp.workEnd}
            , #{comp.workTime}
            , #{comp.lunchTime}
            , #{comp.restTime}
            , #{comp.workType}
            , #{comp.park}
            , #{comp.cafe}
            , #{comp.comPhoto}
            , #{comp.business}
            , #{comp.useDb}
            , #{comp.merch}
            , #{comp.minAge}
            , #{comp.maxAge}
            , #{comp.reward}
            , #{comp.intType}
            , #{comp.intTime}
            , #{comp.intCount}
            , #{comp.eduCost}
            , #{comp.eduInst}
            , #{comp.firstPay}
            , #{comp.guarPay}
            , #{comp.guarPeriod}
            , #{comp.avePay}
            , #{comp.topPay}
            , #{comp.bonus}
            , #{comp.promo}
            , #{comp.bond}
            , #{comp.bondPay}
            , #{comp.welfare}
            , #{comp.welfEtc}
            , #{comp.prevEmp}
            , #{comp.empSch}
            , #{comp.strength}
            , #{comp.propose}
            , #{comp.suggest}
            , #{comp.profile}
            , #{comp.empAdmin}
            , #{comp.visitMeet}
        )
    """)
    int addComp(@Param("comp") Company comp)

//    // 고객사 아이디 중복처리
//    @Select("""
//        SELECT count(*)
//         FROM formmail_company
//         WHERE cid = #{cid}
//    """)
//    int compDuplicatedChkId(@Param("cid") String cid)


    // 전체 고객사 조회
    @Select("""
        SELECT 
        fc.cid
        , fc.com_name
        , fc.com_gubun
        , fc.com_channel
        , fc.com_spot
        , fc.manager
        , fc.manager_id
        , fc.com_address
        , fa.r_name
        , fa.user_name
        , fa.position
        , fc.surveyProceed
        FROM formmail_company fc
        JOIN formmail_admin fa ON fc.manager_id = fa.user_id 
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<Company> companyList(@Param("paging") Paging paging)

    // 고객사 수 조회
    @Select("""
        SELECT COUNT(*)
        FROM formmail_company
    """)
    int getCompanyListCount()

    // cid 일치하는 고객사 정보 반환하기
    @Select("""
        SELECT
        fc.cid
        , fc.com_name
        , fc.com_gubun
        , fc.com_channel
        , fc.com_spot
        , fc.manager
        , fc.manager_id
        , fc.com_address
        , fa.r_name
        , fa.user_name
        , fa.position
        , fa.m_phone
        , fc.surveyProceed
        FROM formmail_company fc
        JOIN formmail_admin fa ON fc.manager_id = fa.user_id
        WHERE fc.cid = #{cid}
        LIMIT 1;
    """)
    List<Company> findCompany(@Param("cid") String cid)

//    // cid 일치하는 고객사 정보 반환하기
//    @Select("""
//        SELECT *
//        FROM formmail_company
//        WHERE cid = #{comp.cid}
//    """)
//    formMail_company findCompany(@Param("cid") String cid)


    // 고객사 업데이트
    @Update("""
    <script>
        UPDATE formmail_company
        <set>
            <if test="comp.managerId != null"> manager_id = #{comp.managerId}, </if>
            <if test="comp.comCenter != null"> com_center = #{comp.comCenter}, </if>
            <if test="comp.comGubun != null"> com_gubun = #{comp.comGubun}, </if>
            <if test="comp.comName != null"> com_name = #{comp.comName}, </if>            
            <if test="comp.comChannel != null"> com_channel = #{comp.comChannel}, </if>
            <if test="comp.comSpot != null"> com_spot = #{comp.comSpot}, </if>
            <if test="comp.comPhone != null"> com_phone = #{comp.comPhone}, </if>
            <if test="comp.comAddress != null"> com_address = #{comp.comAddress}, </if>
            <if test="comp.headName != null"> head_name = #{comp.headName}, </if>
            <if test="comp.headPhone != null"> head_phone = #{comp.headPhone}, </if>
            <if test="comp.leaderName != null"> leader_name = #{comp.leaderName}, </if>
            <if test="comp.leaderPhone != null"> leader_phone = #{comp.leaderPhone}, </if>
            <if test="comp.manager != null"> manager = #{comp.manager}, </if>
            <if test="comp.manager1 != null"> manager1 = #{comp.manager1}, </if>            
            <if test="comp.manager2 != null"> manager2 = #{comp.manager2}, </if>            
            <if test="comp.manager3 != null"> manager3 = #{comp.manager3}, </if>
            <if test="comp.genName != null"> gen_name = #{com.genName}, </if>
            <if test="comp.genPhone != null"> gen_phone = #{com.genPhone}, </if>            
            <if test="comp.affiliation != null"> affiliation = #{com.affiliation}, </if>            
            <if test="comp.headCount != null"> head_count = #{com.headCount}, </if>            
            <if test="comp.location != null"> location = #{com.location}, </if>            
            <if test="comp.workStart != null"> work_start  = #{com.workStart}, </if>            
            <if test="comp.workEnd != null"> work_end = #{com.workEnd}, </if>            
            <if test="comp.workTime != null"> work_time = #{com.workTime}, </if>            
            <if test="comp.lunchTime != null"> lunch_time = #{com.lunchTime}, </if>            
            <if test="comp.restTime != null"> rest_time = #{com.restTime}, </if>            
            <if test="comp.workType != null"> work_type = #{com.workType}, </if>            
            <if test="comp.park != null"> park = #{com.park}, </if>            
            <if test="comp.cafe != null"> cafe = #{com.cafe}, </if>            
            <if test="comp.comPhoto != null"> com_photo = #{com.comPhoto}, </if>            
            <if test="comp.business != null"> business = #{com.business}, </if>            
            <if test="comp.useDb != null"> user_db = #{com.useDb}, </if>            
            <if test="comp.merch != null"> merch = #{com.merch}, </if>            
            <if test="comp.minAge != null"> min_age = #{com.minAge}, </if>            
            <if test="comp.maxAge != null"> max_age = #{com.maxAge}, </if>            
            <if test="comp.reward != null"> reward = #{com.reward}, </if>            
            <if test="comp.intType != null"> int_type = #{com.intType}, </if>            
            <if test="comp.intTime != null"> int_time = #{com.intTime}, </if>            
            <if test="comp.intCount != null"> int_count = #{com.intCount}, </if>            
            <if test="comp.eduCost != null"> edu_cost = #{com.eduCost}, </if>            
            <if test="comp.eduInst != null"> edu_inst = #{com.eduInst}, </if>            
            <if test="comp.firstPay != null"> first_pay = #{com.firstPay}, </if>            
            <if test="comp.guarPay != null"> guar_pay = #{com.guarPay}, </if>            
            <if test="comp.guarPeriod != null"> guar_period = #{com.guarPeriod}, </if>            
            <if test="comp.avePay != null"> ave_pay = #{com.avePay}, </if>            
            <if test="comp.topPay != null"> top_pay = #{com.topPay}, </if>            
            <if test="comp.bonus != null"> bonus = #{com.bonus}, </if>            
            <if test="comp.promo != null"> promo = #{com.promo}, </if>            
            <if test="comp.bond != null"> bond = #{com.bond}, </if>            
            <if test="comp.bondPay != null"> bond_pay = #{com.bondPay}, </if>            
            <if test="comp.welfare != null"> welfare = #{com.welfare}, </if>            
            <if test="comp.welfEtc != null"> welf_etc = #{com.welfEtc}, </if>            
            <if test="comp.prevEmp != null"> prev_emp = #{com.prevEmp}, </if>            
            <if test="comp.empSch != null"> emp_sch = #{com.empSch}, </if>            
            <if test="comp.strength != null"> strength = #{com.strength}, </if>            
            <if test="comp.propose != null"> propose = #{com.propose}, </if>            
            <if test="comp.suggest != null"> suggest = #{com.suggest}, </if>            
            <if test="comp.profile != null"> profile = #{com.profile}, </if>            
            <if test="comp.empAdmin != null"> emp_admin = #{com.empAdmin}, </if>            
            <if test="comp.visitMeet != null"> visit_meet = #{com.visitMeet}, </if>            
            <if test="comp.surveyProceed != null"> survey_proceed = #{com.surveyProceed}, </if>            
            upt_date = sysdate()
        </set>
        WHERE cid = #{comp.cid} 
    </script>
    """)
    int updateCompany(@Param("comp") Company comp)


    // 고객사 삭제
    @Delete("""
        DELETE FROM formmail_company
        WHERE cid = #{comp.cid}
        AND company_name = #{comp.companyName}
        AND company_branch = #{comp.companyBranch}
    """)
    int deleteCompany(@Param("comp") Company comp)

    // 삭제한 사람의 로그
    @Insert("""
        INSERT INTO formmail_delete_log(
            user_id
            , company_name
            , company_branch
            , type
        ) VALUES (
            #{comp.managerId}
            , #{comp.companyName}
            , #{comp.companyBranch}
            , 'company'
        )
    """)
    int deleteLog(@Param("comp") Company comp)

    // 고객사 하나 찾기
    @Select("""
        SELECT *
        FROM formmail_company
        WHERE cid = #{cid}
    """)
    Company findOneComp(@Param("cid") String cid)

}