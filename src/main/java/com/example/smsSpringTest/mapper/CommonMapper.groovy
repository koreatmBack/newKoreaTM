package com.example.smsSpringTest.mapper


import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface CommonMapper {

    // url 저장 테이블에 입력한 상대경로 저장하기
    @Insert("""
       INSERT INTO formmail_file(
        url       
       ) VALUES (
        #{url}
       )
    """)
    int addUrl(@Param("url") String url)

    // db에 저장된 url 삭제
    @Delete("""
        DELETE FROM formmail_file
        WHERE url = #{url}
    """)
    int deleteUrl(@Param("url") String url)
}