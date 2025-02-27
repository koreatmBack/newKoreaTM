package com.example.smsSpringTest.mapper


import com.example.smsSpringTest.model.Company
import com.example.smsSpringTest.model.Paging
import org.apache.ibatis.annotations.*

@Mapper
interface CompanyMapper {

    @Insert("""
        INSERT INTO formmail_company(
            cid
            , company_name
            , gubun
            , channel
            , company_branch
            , manager1
            , manager2
            , c_phone1
            , c_phone2
            , manager_id
            , survey_type
            , partner
            , address
            , industry
            , sido
            , sigungu
            , survey_proceed
            , com_proceed
        ) VALUES (
            #{comp.cid}
            , #{comp.companyName}
            , #{comp.gubun}
            , #{comp.channel}
            , #{comp.companyBranch}
            , #{comp.manager1}
            , #{comp.manager2}
            , #{comp.cPhone1}
            , #{comp.cPhone2}
            , #{comp.managerId}
            , #{comp.surveyType}
            , #{comp.partner}
            , #{comp.address}
            , #{comp.industry}
            , #{comp.sido}
            , #{comp.sigungu}
            , #{comp.surveyProceed}
            , #{comp.comProceed}
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
        , fc.company_name
        , fc.gubun
        , fc.channel
        , fc.company_branch
        , fc.manager1
        , fc.c_phone1
        , fc.partner
        , fc.manager_id
        , fc.survey_type
        , fc.address
        , fc.industry
        , fa.r_name
        , fa.user_name
        , fa.position
        , fc.surveyProceed
        , fc.comProceed
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
        , fc.company_name
        , fc.gubun
        , fc.channel
        , fc.company_branch
        , fc.manager1
        , fc.c_phone1
        , fc.partner
        , fc.manager_id
        , fc.survey_type
        , fc.address
        , fc.industry
        , fa.r_name
        , fa.user_name
        , fa.position
        , fa.m_phone
        , fc.surveyProceed
        , fc.comProceed
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
            <if test="comp.companyName != null">
                company_name = #{comp.companyName},
            </if>
            <if test="comp.gubun != null">
                gubun = #{comp.gubun},
            </if>
            <if test="comp.channel != null">
                channel = #{comp.channel},
            </if>
            <if test="comp.companyBranch != null">
                company_branch = #{comp.companyBranch},
            </if>
            <if test="comp.manager1 != null">
                manager1 = #{comp.manager1},
            </if>
            <if test="comp.manager2 != null">
                manager2 = #{comp.manager2},
            </if>
            <if test="comp.cPhone1 != null">
                c_phone1 = #{comp.cPhone1},
            </if>
            <if test="comp.cPhone2 != null">
                c_phone2 = #{comp.cPhone2},
            </if>
            <if test="comp.managerId != null">
                manager_id = #{comp.managerId},
            </if>
            <if test="comp.surveyType != null">
                survey_type = #{comp.surveyType},
            </if>
            <if test="comp.partner != null">
                partner = #{comp.partner},
            </if>
            <if test="comp.address != null">
                address = #{comp.address},
            </if>
            <if test="comp.industry != null">
                industry = #{comp.industry},
            </if>
            <if test="comp.surveyProceed != null">
                survey_proceed = #{comp.surveyProceed},
            </if>
            <if test="comp.comProceed != null">
                com_proceed = #{com.comProceed},
            </if>
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