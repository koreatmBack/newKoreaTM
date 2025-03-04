package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.formmail_vo.Insurance
import org.apache.ibatis.annotations.*

@Mapper
interface InsuranceMapper {

    // 보험 타입, 보험명 등록하기
    @Insert("""
        INSERT INTO formmail_insurance(
            type
            , name
        ) VALUES (
            #{type}
            ,#{name}
        )
    """)
    int addInsurance(@Param("type") String type, @Param("name") String name)

    // 중복 찾기
    @Select("""
        SELECT count(*)
        FROM formmail_insurance
        WHERE type = #{type}
        AND name = #{name}
    """)
    int dupInsCheck(@Param("type") String type, @Param("name") String name)

    // 하나 삭제
    @Delete("""
        DELETE FROM formmail_insurance
        WHERE name = #{ins.name}
    """)
    int deleteInsurance(@Param("ins") Insurance ins)

    // name 검색 기능. 포함된 것
    @Select("""
        SELECT type, name
        FROM formmail_insurance
        WHERE name LIKE CONCAT('%',#{ins.searchKeyword},'%')
    """)
    List<Insurance> searchName(@Param("ins") Insurance ins)

}