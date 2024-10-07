package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.entity.formMail_company
import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.findCompany
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

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
            , mid
            , survey_type
            , partner
            , address
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
            , #{comp.mid.userId}
            , #{comp.surveyType}
            , #{comp.partner}
            , #{comp.address}
        )
    """)
    int addComp(@Param("comp") formMail_company comp)

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
        , fc.mid
        , fc.survey_type
        , fc.address
        , fa.r_name
        , fa.user_name
        , fa.position
        FROM formmail_company fc
        JOIN formmail_admin fa ON fc.mid = fa.user_id 
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<findCompany> companyList(@Param("paging") Paging paging)

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
        , fc.mid
        , fc.survey_type
        , fc.address
        , fa.r_name
        , fa.user_name
        , fa.position
        FROM formmail_company fc
        JOIN formmail_admin fa ON fc.mid = fa.user_id
        WHERE fc.cid = #{cid}
        LIMIT 1;
    """)
    List<findCompany> findCompany(@Param("cid") String cid)

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
            <if test="comp.mid != null and comp.mid.userId != null">
                mid = #{comp.mid.userId},
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
        </set>
        WHERE cid = #{comp.cid} 
    </script>
    """)
    int updateCompany(@Param("comp") formMail_company comp)


    // 고객사 삭제
    @Delete("""
        DELETE FROM formmail_company
        WHERE cid = #{comp.cid}
        AND company_name = #{comp.companyName}
        AND company_branch = #{comp.companyBranch}
    """)
    int deleteCompany(@Param("comp") formMail_company comp)

    // 삭제한 사람의 로그
    @Insert("""
        INSERT INTO formmail_delete_log(
            user_id
            , company_name
            , company_branch
            , type
        ) VALUES (
            #{comp.mid.userId}
            , #{comp.companyName}
            , #{comp.companyBranch}
            , 'company'
        )
    """)
    int deleteLog(@Param("comp") formMail_company comp)

}