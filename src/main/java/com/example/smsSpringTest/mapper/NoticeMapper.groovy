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
            , faq_category
        )VALUES(
            #{notice.title}
            , #{notice.content}
            , #{notice.type}
            , #{notice.faqCategory}
        )
</script>        
    """)
    int insertNotice(@Param("notice") Notice notice)

    // 공지사항 or FAQ 전체 조회
    @Select("""
<script>
        SELECT *
        FROM formmail_notice
        WHERE type = #{notice.type}
        <if test="notice.keyword != null">
           AND title LIKE CONCAT('%', #{notice.keyword}, '%')
        </if>
        ORDER BY updated DESC
        LIMIT #{notice.size} OFFSET #{notice.offset}
</script>    
    """)
    List<Notice> noticeList(@Param("notice") Notice notice)

//    // 공지사항 or FAQ 검색하기
//    @Select("""
//        SELECT *
//        FROM formmail_notice
//        WHERE type = #{notice.type}
//        AND title LIKE CONCAT('%', #{ad.keyword}, '%')
//        LIMIT #{notice.size} OFFSET #{notice.offset}
//    """)
//    List<Notice> searchNoticeList(@Param("notice") Notice notice)

    // 공지사항 or FAQ 전체 수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_notice
        WHERE type = #{notice.type}
        <if test="notice.keyword != null">
           AND title LIKE CONCAT('%', #{notice.keyword}, '%')
        </if>
        LIMIT #{notice.size} OFFSET #{notice.offset}
</script>   
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
         <if test="notice.faqCategory != null"> faq_category = #{notice.faqCategory} </if>
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

    // FAQ 카테고리로 , 해당 FAQ 목록 반환
    @Select("""
<script>
        SELECT *
        FROM formmail_notice
        WHERE 1=1
        AND type = 'B02'
        <if test="notice.faqCategory != null">
        AND faq_category = #{notice.faqCategory}
        </if>    
        <if test="notice.keyword != null">
           AND title LIKE CONCAT('%', #{notice.keyword}, '%')
        </if>
        ORDER BY updated DESC
        LIMIT #{notice.size} OFFSET #{notice.offset}
</script>        
    """)
    List<Notice> faqCategoryList(@Param("notice") Notice notice)

    // FAQ 카테고리로 , 해당 FAQ 목록 반환 수
    @Select("""
<script>
        SELECT count(*)
        FROM formmail_notice
        WHERE 1=1
        AND type = 'B02'
        <if test="notice.faqCategory != null">
        AND faq_category = #{notice.faqCategory}
        </if>   
        <if test="notice.keyword != null">
           AND title LIKE CONCAT('%', #{notice.keyword}, '%')
        </if>
</script>        
    """)
    int faqCategoryCount(@Param("notice") Notice notice)

}