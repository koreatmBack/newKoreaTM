package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.ad.AdImageRequest
import com.example.smsSpringTest.model.ad.AdRequest
import com.example.smsSpringTest.model.ad.fmAd
import com.example.smsSpringTest.model.ad.fmAdImage
import com.example.smsSpringTest.model.formMail_file
import org.apache.ibatis.annotations.*

@Mapper
interface AdMapper {

    // 광고 등록
    @Insert("""
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
        ) VALUES (
            #{ad.aid}
            , #{ad.cid}
            , #{ad.startDate}
            , #{ad.endDate}
            , #{ad.heaven}
            , #{ad.albamon}
            , #{ad.telejob}
            , #{ad.adTypeM}
            , #{ad.adTypeH}
        )
    """)
    int addAd(@Param("ad") formMail_file ad)

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
    List<fmAd> fmAdList(@Param("adRequest") AdRequest adRequest)

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
    int dupImgUrl(@Param("adImage") formMail_file adImage)
}