package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.S3Upload
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface S3UploadMapper {

    // S3에 파일 업로드
    @Insert("""
        INSERT INTO formmail_ad(
            url
        ) VALUES (
            #{url}
        )
    """)
    int S3UploadFile(@Param("url") String url)

    // S3에 있는 파일 삭제
    @Delete("""
        DELETE FROM formmail_ad
        WHERE url = #{url}
    """)
    int S3DeleteFile(@Param("url") String url)

    // S3에 있는 파일 조회
    @Select("""
        SELECT *
        FROM formmail_ad
    """)
    List<S3Upload> s3UploadList()
}