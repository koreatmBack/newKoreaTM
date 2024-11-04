package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.ad.*
import com.example.smsSpringTest.model.findCompanyAndUser
import com.example.smsSpringTest.model.formMail_file
import org.apache.ibatis.annotations.*

@Mapper
interface AdMapper {

    // 광고 등록
    @Insert("""
<script>
        INSERT INTO formmail_ad(
            aid
            , cid
            , start_date
            , end_date
            , heaven
            , albamon
            , telejob
            , ad_type_m
            , ad_type_h
            , ad_img
            , logo_img
            , concept
            , user_name
            , company
            , address
            , title
            , work_start
            , work_end
            , rest_time
            , min_pay
            , max_pay
            , work_day
            , welfare
            , welfare2
            , experience
            , ad_num
            , work_time
            , grade
            , sido
            , sigungu
            , hashtag
        ) VALUES (
            #{ad.aid},
        <if test="ad.cid != null and ad.cid != ''">
            #{ad.cid},
        </if>        
        <if test="ad.cid == null or ad.cid == ''">
            NULL,
        </if>
             #{ad.startDate}
            , #{ad.endDate}
            , #{ad.heaven}
            , #{ad.albamon}
            , #{ad.telejob}
            , #{ad.adTypeM}
            , #{ad.adTypeH}
            , #{ad.adImg}
            , #{ad.logoImg}
            , #{ad.concept}
            , #{ad.userName}
            , #{ad.company}
            , #{ad.address}
            , #{ad.title}
            , #{ad.workStart}
            , #{ad.workEnd}
            , #{ad.restTime}
            , #{ad.minPay}
            , #{ad.maxPay}
            , #{ad.workDay}
            , #{ad.welfare}
            , #{ad.welfare2}
            , #{ad.experience}
            , #{ad.adNum}
            , #{ad.workTime}
            , #{ad.grade}
            , #{ad.sido}
            , #{ad.sigungu}
            , #{ad.hashtag}
        )
</script>        
    """)
    int addAd(@Param("ad") fmAd ad)

    // 광고 총 일수 등록
    @Update("""
        UPDATE formmail_ad
        SET total_day = #{totalDay}
        WHERE aid = #{serialNumber}
    """)
    int addTotalDay(@Param("totalDay") int totalDay, @Param("serialNumber") String serialNumber)

    // 광고 연장일 추가
    @Update("""
        UPDATE formmail_ad
        SET extension_day = #{extensionDay}
        WHERE aid = #{ad.aid}
    """)
    int addExtensionDay(@Param("extensionDay") int extensionDay, @Param("ad") fmAd ad)

    // 해당 고객사 광고 조회
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE cid = #{adRequest.cid}
        LIMIT #{adRequest.size} OFFSET #{adRequest.offset}
    """)
    List<AdRequest> findFmAdList(@Param("adRequest") AdRequest adRequest)

    // 해당되는 고객사 광고 수
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE cid = #{adRequest.cid}
    """)
    int getFmAdListCount(@Param("adRequest") AdRequest adRequest)

    // 광고 수정 ( 광고 고유 id 일치해야 가능 )
    @Update("""
    <script>
        UPDATE formmail_ad
     <set>
        <if test="ad.cid != null">
            cid = #{ad.cid},
        </if>
        <if test="ad.startDate != null">
            start_date = #{ad.startDate},
        </if>    
        <if test="ad.endDate != null">
            end_date = #{ad.endDate},
        </if>        
        <if test="ad.extensionDay != null">
            extension_day = #{ad.extensionDay},
        </if>
        <if test="ad.heaven != null">
            heaven = #{ad.heaven},
        </if> 
        <if test="ad.albamon != null">
            albamon = #{ad.albamon},
        </if>           
        <if test="ad.telejob != null">
            telejob = #{ad.telejob},
        </if>  
        <if test="ad.adTypeM != null">
            ad_type_m = #{ad.adTypeM},
        </if>    
        <if test="ad.adTypeH != null">
            ad_type_h = #{ad.adTypeH},
        </if>   
        <if test="ad.adImg != null">
            ad_img = #{ad.adImg},
        </if>   
        <if test="ad.logoImg != null">
            logo_img = #{ad.logoImg},
        </if>   
        <if test="ad.concept != null">
            concept = #{ad.concept},
        </if>   
        <if test="ad.userName != null">
            user_name = #{ad.userName},
        </if>   
        <if test="ad.company != null">
            company = #{ad.company},
        </if>   
        <if test="ad.address != null">
            address = #{ad.address},
        </if>   
        <if test="ad.title != null">
            title = #{ad.title},
        </if>   
        <if test="ad.workStart != null">
            work_start = #{ad.workStart},
        </if>   
        <if test="ad.workEnd != null">
            work_end = #{ad.workEnd},
        </if>   
        <if test="ad.restTime != null">
            rest_time = #{ad.restTime},
        </if>           
        <if test="ad.minPay != null">
            min_pay = #{ad.minPay},
        </if>           
        <if test="ad.maxPay != null">
            max_pay = #{ad.maxPay},
        </if>           
        <if test="ad.workDay != null">
            work_day = #{ad.workDay},
        </if>           
        <if test="ad.welfare != null">
            welfare = #{ad.welfare},
        </if>           
        <if test="ad.welfare2 != null">
            welfare2 = #{ad.welfare2},
        </if>          
        <if test="ad.experience != null">
            experience = #{ad.experience},
        </if>           
        <if test="ad.adNum != null">
            ad_num = #{ad.adNum},
        </if>           
        <if test="ad.workTime != null">
            work_time = #{ad.workTime},
        </if>
        <if test="ad.hashtag != null">
            hashtag = #{ad.hashtag},
        </if>         
        <if test="ad.grade != null">
            grade = #{ad.grade},
        </if>     
     </set>
        WHERE aid = #{ad.aid}
    </script>
    """)
    int updateAd(@Param("ad") fmAd ad)

    // 광고 삭제 ( 광고 고유 id 일치해야 가능 )
    @Delete("""
        DELETE from formmail_ad
        WHERE aid = #{ad.aid}
    """)
    int deleteAd(@Param("ad") fmAd ad)

    // 폼메일용 광고 목록 전체 조회 (페이징 처리)
    @Select("""
        SELECT *
        FROM formmail_ad
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<fmAd> allAdList(@Param("paging") Paging paging)

    // 광고 목록 전체 조회 수 (페이징 토탈 값)
    @Select("""
        SELECT count(*)
        FROM formmail_ad
    """)
    int allAdListCount()

    // 폼메일용 adNum 일치하는 광고 목록 전체 조회 (페이징 처리)
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE ad_num = #{ad.adNum}
    """)
    List<fmAd> searchAdNumList(@Param("ad") fmAd ad)


    // 폼메일용 aid 일치하는 광고 상세 조회
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE aid = #{ad.aid}
    """)
    List<fmAd> findOneAd(@Param("ad") fmAd ad)

    // 폼메일용 title이 포함된 광고 조회
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE title LIKE CONCAT('%', #{ad.title}, '%')
    """)
    List<fmAd> searchTitleAd(@Param("ad") fmAd ad)

    // 폼메일용 hashtag( 배열 형식 ) 중 완전 일치하는 리스트 반환
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE FIND_IN_SET(#{ad.hashtag} , REPLACE(hashtag, ' ','')) > 0 ;
    """)
    List<fmAd> searchHashtagAd(@Param("ad") fmAd ad)

    // 폼메일용 sido (필수) , sigungu (필수아님) 일치하는 광고 찾기
    // 만약 sido, sigungu 같이 입력시 -> 2개 항목 다 완전 일치하는 것
    @Select("""
<script>
        SELECT *
        FROM formmail_ad
        WHERE sido = #{ad.sido}
        <if test="ad.sigungu != null">
        AND sigungu = #{ad.sigungu}
        </if>
</script>   
    """)
    List<fmAd> searchAddressAd(@Param("ad") fmAd ad)
    // ---------------------------------------------

    // 잡사이트용 광고 목록 전체 조회 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobSite> allJobsiteList(@Param("paging") Paging paging)

    // 잡사이트용 광고 목록 전체 조회 수 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE end_date >= CURDATE()
    """)
    int allJobsiteListCount()


    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        AND title LIKE CONCAT('%', #{ad.title}, '%')
        AND end_date >= CURDATE()
    """)
    List<JobSite> searchTitleJobsite(@Param("ad") fmAd ad)

    // 잡사이트용 aid 일치하는 광고 상세 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        AND aid = #{ad.aid}
    """)
    List<JobSite> findOneJobsite(@Param("ad") fmAd ad)

    // 잡 사이트용 등록순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        ORDER BY created_at DESC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobSite> orderByCreated(@Param("paging") Paging paging)

    // 잡 사이트용 급여 높은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        ORDER BY CAST(max_pay AS UNSIGNED) DESC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobSite> orderByMaxPay(@Param("paging") Paging paging)

    // 잡 사이트용 근무일수 적은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        ORDER BY (LENGTH(work_day) - LENGTH(REPLACE(work_day, ',', '')) + 1) ASC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobSite> orderByWorkDay(@Param("paging") Paging paging)

    // 잡 사이트용 근무시간 짧은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
        ORDER BY work_time ASC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<JobSite> orderByWorkTime(@Param("paging") Paging paging)


    //-------------------------------------

    // aid로 cid 찾기
    @Select("""
        SELECT cid
        FROM formmail_ad
        WHERE aid = #{ad.aid}
    """)
    String findCid(@Param("ad") fmAd ad)

    // aid 입력 후 찾은 cid로 고객사 정보와 유저 정보 찾기
    @Select("""
        SELECT
        fc.company_name
        , fc.company_branch
        , fa.r_name
        , fa.user_name
        , fa.position
        , fa.user_id
        , fc.cid
        FROM formmail_company fc
        JOIN formmail_admin fa ON fc.mid = fa.user_id
        WHERE fc.cid = #{cid}
        LIMIT 1;
    """)
    List<findCompanyAndUser> findCompanyAndUser(@Param("cid") String cid)


// ------------------ 광고 테이블 끝 -----------------

    // 광고 이미지 등록
    @Insert("""
        INSERT INTO formmail_ad_image(
            ai_id
            , aid
            , path
            , concept
        ) VALUES (
            #{adImage.aiId}
            , #{adImage.aid}
            , #{adImage.path}
            , #{adImage.concept}
        )
    """)
    int addAdImg(@Param("adImage") fmAdImage adImage)


    // 광고 이미지 DB 삭제 ( 광고 이미지 고유 id 일치해야 가능 )
    @Delete("""
        DELETE FROM formmail_ad_image
        WHERE ai_id = #{adImage.aiId}
    """)
    int deleteAdImg(@Param("adImage") fmAdImage adImage)

    // 광고 이미지 고유 id로 해당 이미지 path 반환
    @Select("""
        SELECT path
        FROM formmail_ad_image
        WHERE ai_id = #{adImage.aiId}
    """)
    String getPath(@Param("adImage") fmAdImage adImage)

    // 광고 이미지 전체 조회
    @Select("""
        SELECT *
        FROM formmail_ad_image
        WHERE aid = #{adImage.aid}
        LIMIT #{adImage.size} OFFSET #{adImage.offset}
    """)
    List<fmAdImage> fmAdImageList(@Param("adImage") AdImageRequest adImage)

    // 광고 이미지 전체 수
    @Select("""
        SELECT count(*)
        FROM formmail_ad_image
        WHERE aid = #{adImage.aid}
    """)
    int getFmAdImageListCount(@Param("adImage") AdImageRequest adImage)

    // formmail_file db에서 url 일치하는 데이터 삭제하기
    @Delete("""
        DELETE FROM formmail_file
        WHERE ad_img = #{adImage.adImg} 
    """)
    int deleteFile(@Param("adImage") formMail_file adImage)

    // 파일 등록할때, adImg가 있거나 logoImg가 이미 있으면 올리지 않게.
    @Select("""
        SELECT count(*)
        FROM formmail_file
        WHERE ad_img = #{adImage.adImg}
        OR logo_img = #{adImage.logoImg}
    """)
    int dupImgUrl(@Param("adImage") fmAd adImage)


    // 잡사이트용 광고 목록 전체 조회 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
    """)
    List<JobSite> jobSiteListTest()


}