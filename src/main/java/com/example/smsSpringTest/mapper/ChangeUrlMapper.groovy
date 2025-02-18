package com.example.smsSpringTest.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ChangeUrlMapper {

    @Insert("""
        INSERT INTO formmail_change_url(
            original_url,
            short_url
        ) VALUES (
            #{origin}
            , #{short}
        )
    """)
    int changeUrl(@Param("origin") String originUrl , @Param("short") String shortUrl)

    @Select("""
        SELECT original_url
        FROM formmail_change_url
        WHERE short_url = #{short}
    """)
    String originalUrl(@Param("short") String shortUrl)

    @Select("""
        SELECT count(*)
        FROM formmail_change_url
        WHERE original_url = #{origin}
    """)
    int dupCheckOrigin(@Param("origin") String originUrl)

}