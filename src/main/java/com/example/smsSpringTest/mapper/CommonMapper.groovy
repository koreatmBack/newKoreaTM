package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.formMail_file
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface CommonMapper {

    // url 저장 테이블에 입력한 상대경로 저장하기
    @Insert("""
    <script>
       INSERT INTO formmail_file(
        cid,
         <if test="adImage.adImg != null">ad_img,</if>
         <if test="adImage.logoImg != null">logo_img,</if>
         concept
       ) VALUES (
        #{adImage.cid},
         <if test="adImage.adImg != null">#{adImage.adImg},</if>
         <if test="adImage.logoImg != null">#{adImage.logoImg},</if>
         #{adImage.concept}
       )
       </script>
    """)
    int addUrl(@Param("adImage") formMail_file adImage)

//    // db에 저장된 url 삭제
//    @Delete("""
//        DELETE FROM formmail_file
//        WHERE url = #{url}
//    """)
//    int deleteUrl(@Param("url") String url)
}