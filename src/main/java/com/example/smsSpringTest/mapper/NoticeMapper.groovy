package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.jobsite.Notice
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface NoticeMapper {

    // 공지사항, FAQ 등록
    @Insert("""
<script>
        INSERT INTO formmail_notice(
            title
            , content
            , type
        )VALUES(
            #{notice.title}
            , #{notice.content}
            , #{notice.type}
        )
</script>        
    """)
    int insertNotice(@Param("notice") Notice notice)

    // 공지사항 or FAQ 전체 조회
    @Select("""
        SELECT *
        FROM formmail_notice
        WHERE type = #{notice.type}
        LIMIT #{notice.size} OFFSET #{notice.offset}
    """)
    List<Notice> noticeList(@Param("notice") Notice notice)

    // 공지사항 or FAQ 전체 수
    @Select("""
        SELECT count(*)
        FROM formmail_notice
        WHERE type = #{notice.type}
    """)
    int countAll(@Param("notice") Notice notice)

//    // FAQ 전체 조회
//    @Select("""
//        SELECT *
//        FROM formmail_notice
//        WHERE type = 'B02'
//        LIMIT #{notice.size} OFFSET #{notice.offset}
//    """)
//    List<Notice> FAQList(@Param("notice") Notice notice)

    // 원하는 공지사항 or FAQ 조회
    @Select("""
        SELECT *
        FROM formmail_notice
        WHERE num = #{notice.num}
    """)
    Notice findOneNotice(@Param("notice") Notice notice)

//    // 원하는 FAQ 조회
//    @Select("""
//        SELECT *
//        FROM formmail_notice
//        WHERE type = 'B02'
//        AND num = #{notice.num}
//    """)
//    Notice findOneFAQ(@Param("notice") Notice notice)

    // 공지사항 or FAQ 수정하기
    @Update("""
<script>
        UPDATE formmail_notice
        <set>
         <if test="notice.title != null"> title = #{notice.title} </if>
         <if test="notice.content != null"> content = #{notice.content} </if>
         <if test="notice.type != null"> type = #{notice.type} </if>
        </set>
        WHERE num = #{notice.num}
</script>        
    """)
    int updateNotice(@Param("notice") Notice notice)

    // 공지사항 or FAQ 삭제하기
    @Delete("""
        DELETE FROM formmail_notice
        WHERE num = #{notice.num}
    """)
    int deleteNotice(@Param("notice") Notice notice)

    // 조회수 1 증가
    @Update("""
        UPDATE formmail_notice
        SET view_count = #{notice.viewCount}
        WHERE num = #{notice.num}
    """)
    int increaseViewCount(@Param("notice") Notice notice)
}