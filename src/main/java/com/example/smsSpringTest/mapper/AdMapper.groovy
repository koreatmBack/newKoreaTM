package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Paging
import com.example.smsSpringTest.model.Regions
import com.example.smsSpringTest.model.ad.*
import com.example.smsSpringTest.model.formMail_file
import org.apache.ibatis.annotations.*

@Mapper
interface AdMapper {

    // 광고 등록
    @Insert("""
<script>
        INSERT INTO formmail_ad(
            aid
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
            , grade
            , sido
            , sigungu
            , dong_eub_myun
            , job_type
            , employment_type
            , recruit_count
            , work_period
            , work_days
            , gender
            , age
            , education
            , pre_conditions
            , etc_conditions
            , apply_method
            , near_university
            , x
            , y
            , salary
            , salary_type
            , manager_name
            , manager_email
            , manager_phone
            , manager_sub_phone
            , probation
            , period_discussion
            , work_date
            , work_date_detail
            , work_time
            , work_time_detail
            , age_type
            , min_age
            , max_age
            , end_limit
            , apply_url
            , zip_code
            , address_detail
            , photo_list
            , ad_link
            , sido2
            , sigungu2
            , dong_eub_myun2
            , sido3
            , sigungu3
            , dong_eub_myun3
            , focus
            , detail_content
            , phone_show
            , sub_phone_show
            , detail_images
            , company_user_id
        ) VALUES (
            #{ad.aid}
            , #{ad.startDate}
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
            , #{ad.grade}
            , #{ad.sido}
            , #{ad.sigungu}
            , #{ad.dongEubMyun}
            , #{ad.jobType}
            , #{ad.employmentType}
            , #{ad.recruitCount}
            , #{ad.workPeriod}
            , #{ad.workDays}
            , #{ad.gender}
            , #{ad.age}
            , #{ad.education}
            , #{ad.preConditions}
            , #{ad.etcConditions}
            , #{ad.applyMethod}
            , #{ad.nearUniversity}
            , #{ad.x}
            , #{ad.y}
            , #{ad.salary}
            , #{ad.salaryType}
            , #{ad.managerName}
            , #{ad.managerEmail}
            , #{ad.managerPhone}
            , #{ad.managerSubPhone}
            , #{ad.probation}
            , #{ad.periodDiscussion}
            , #{ad.workDate}
            , #{ad.workDateDetail}
            , #{ad.workTime}
            , #{ad.workTimeDetail}
            , #{ad.ageType}
            , #{ad.minAge}
            , #{ad.maxAge}
            , #{ad.endLimit}
            , #{ad.applyUrl}
            , #{ad.zipCode}
            , #{ad.addressDetail}
            , #{ad.photoList}
            , #{ad.adLink}
            , #{ad.sido2}
            , #{ad.sigungu2}
            , #{ad.dongEubMyun2}
            , #{ad.sido3}
            , #{ad.sigungu3}
            , #{ad.dongEubMyun3}
            , #{ad.focus}
            , #{ad.detailContent}
            , #{ad.phoneShow}
            , #{ad.subPhoneShow}
            , #{ad.detailImages}
            , #{ad.companyUserId}
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

//    // 해당 고객사 광고 조회
//    @Select("""
//        SELECT *
//        FROM formmail_ad
//        WHERE cid = #{adRequest.cid}
//        LIMIT #{adRequest.size} OFFSET #{adRequest.offset}
//    """)
//    List<AdRequest> findFmAdList(@Param("adRequest") AdRequest adRequest)

//    // 해당되는 고객사 광고 수
//    @Select("""
//        SELECT count(*)
//        FROM formmail_ad
//        WHERE cid = #{adRequest.cid}
//    """)
//    int getFmAdListCount(@Param("adRequest") AdRequest adRequest)

    // 광고 수정 ( 광고 고유 id 일치해야 가능 )
    @Update("""
    <script>
        UPDATE formmail_ad
     <set>
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
        <if test="ad.grade != null">
            grade = #{ad.grade},
        </if>
        <if test="ad.sido != null">
            sido = #{ad.sido},
        </if>
        <if test="ad.sigungu != null">
            sigungu = #{ad.sigungu},
        </if>        
        <if test="ad.dongEubMyun != null">
            dong_eub_myun = #{ad.dongEubMyun},
        </if>        
        <if test="ad.jobType != null">
            job_type = #{ad.jobType},
        </if>        
        <if test="ad.employmentType != null">
            employment_type = #{ad.employmentType},
        </if>        
        <if test="ad.recruitCount != null">
            recruit_count = #{ad.recruitCount},
        </if>        
        <if test="ad.workPeriod != null">
            work_period = #{ad.workPeriod},
        </if>        
        <if test="ad.workDays != null">
            work_days = #{ad.workDays},
        </if>        
        <if test="ad.gender != null">
            gender = #{ad.gender},
        </if>        
        <if test="ad.age != null">
            age = #{ad.age},
        </if>        
        <if test="ad.education != null">
            education = #{ad.education},
        </if>        
        <if test="ad.preConditions != null">
            pre_conditions = #{ad.preConditions},
        </if>        
        <if test="ad.etcConditions != null">
            etc_conditions = #{ad.etcConditions},
        </if>        
        <if test="ad.applyMethod != null"> apply_method = #{ad.applyMethod}, </if>   
        <if test="ad.nearUniversity != null"> near_university = #{ad.nearUniversity},</if>
        <if test="ad.x != null"> x = #{ad.x}, </if>           
        <if test="ad.y != null"> y = #{ad.y}, </if>
        <if test="ad.managerName != null"> manager_name = #{ad.managerName}, </if>
        <if test="ad.managerEmail != null"> manager_email = #{ad.managerEmail}, </if>
        <if test="ad.managerPhone != null"> manager_phone = #{ad.managerPhone}, </if>
        <if test="ad.managerSubPhone != null"> manager_sub_phone = #{ad.managerSubPhone}, </if>
        <if test="ad.probation != null"> probation = #{ad.probation}, </if>
        <if test="ad.periodDiscussion != null"> period_discussion = #{ad.periodDiscussion}, </if>
        <if test="ad.workDate != null"> work_date = #{ad.workDate}, </if>
        <if test="ad.workDateDetail != null"> work_date_detail = #{ad.workDateDetail}, </if>
        <if test="ad.workTime != null"> work_time = #{ad.workTime}, </if>
        <if test="ad.workTimeDetail != null"> work_time_detail = #{ad.workTimeDetail}, </if>
        <if test="ad.ageType != null"> age_type = #{ad.ageType}, </if>
        <if test="ad.minAge != null"> min_age = #{ad.minAge}, </if>
        <if test="ad.maxAge != null"> max_age = #{ad.maxAge}, </if>
        <if test="ad.endLimit != null"> end_limit = #{ad.endLimit}, </if>
        <if test="ad.applyUrl != null"> apply_url = #{ad.applyUrl}, </if>
        <if test="ad.zipCode != null"> zip_code = #{ad.zipCode}, </if>
        <if test="ad.addressDetail != null"> address_detail = #{ad.addressDetail}, </if>
        <if test="ad.photoList != null"> photo_list = #{ad.photoList}, </if>
        <if test="ad.adLink != null"> ad_link = #{ad.adLink}, </if>
        <if test="ad.sido2 != null"> sido2 = #{ad.sido2}, </if>
        <if test="ad.sigungu2 != null"> sigungu2 = #{ad.sigungu2}, </if>
        <if test="ad.dongEubMyun2 != null"> dong_eub_myun2 = #{ad.dongEubMyun2}, </if>
        <if test="ad.sido3 != null"> sido3 = #{ad.sido3}, </if>
        <if test="ad.sigungu3 != null"> sigungu3 = #{ad.sigungu3}, </if>
        <if test="ad.dongEubMyun3 != null"> dong_eub_myun3 = #{ad.dongEubMyun3}, </if>
        <if test="ad.focus != null"> focus = #{ad.focus}, </if>
        <if test="ad.phoneShow != null"> phone_show = #{ad.phoneShow}, </if>
        <if test="ad.subPhoneShow != null"> sub_phone_show = #{ad.subPhoneShow}, </if>
        <if test="ad.detailContent != null"> detail_content = #{ad.detailContent}, </if>
        <if test="ad.detailImages != null"> detail_images = #{ad.detailImages}, </if>
        <if test="ad.companyUserId != null"> company_user_id = #{ad.companyUserId}, </if>
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
        ORDER BY num DESC
        LIMIT #{paging.size} OFFSET #{paging.offset}
    """)
    List<fmAd> allAdList(@Param("paging") Paging paging)

    // 광고 목록 전체 조회 수 (페이징 토탈 값)
    @Select("""
        SELECT count(*)
        FROM formmail_ad
    """)
    int allAdListCount()

    // 폼메일용 adNum 일치하는 광고 목록 전체 조회
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE ad_num = #{ad.adNum}
    """)
    List<fmAd> searchAdNumList(@Param("ad") fmAd ad)

    // 폼메일용 num 일치하는 광고 조회, 고유 번호임
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE num = #{ad.num}
    """)
    List<fmAd> searchOneAd(@Param('ad') fmAd ad)


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

//    // 폼메일용 hashtag( 배열 형식 ) 중 완전 일치하는 리스트 반환
//    @Select("""
//        SELECT *
//        FROM formmail_ad
//        WHERE FIND_IN_SET(#{ad.hashtag} , REPLACE(hashtag, ' ','')) > 0 ;
//    """)
//    List<fmAd> searchHashtagAd(@Param("ad") fmAd ad)

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



    // 폼메일 관리자용 공고관리 -> 전체, 진행중, 대기중, 종료
    // 검색 기능 클릭시 -> 공고제목, 근무지명, 담당자명, 공고번호, 연락처 포함
    @Select("""
    <script>
        SELECT *
        FROM formmail_ad
        WHERE 1=1
        <if test="ad.status != '전체'">
            <choose>
                <when test="ad.status == '진행중'">
                  AND <![CDATA[start_date <= CURDATE() AND 
                         (end_date IS NULL OR end_date >= CURDATE())]]>
                </when>
                <when test="ad.status == '대기중'">
                    AND <![CDATA[CURDATE() < start_date]]>
                </when>
                <when test="ad.status == '종료'">
                    AND <![CDATA[CURDATE() > end_date]]>
                </when>
            </choose>
        </if>
        
        <if test="ad.searchType != null and ad.keyword != null">
            <choose>
                <when test="ad.searchType == '공고제목'">
                    AND title LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '근무회사명'">
                    AND company LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '담당자명'">
                    AND manager_name LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '공고번호'">
                    AND num LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '연락처'">
                    AND manager_phone LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
            </choose>
        </if>
        ORDER BY num DESC
        LIMIT #{ad.size}
        OFFSET #{ad.offset}
    </script>
    """)
    List<fmAd> statusList(@Param("ad") AdRequest ad)

    // 위 조건 개수
    @Select("""
    <script>
        SELECT count(*)
        FROM formmail_ad
        WHERE 1=1
        <if test="ad.status != '전체'">
            <choose>
                <when test="ad.status == '진행중'">
                  AND <![CDATA[start_date <= CURDATE() AND 
                         (end_date IS NULL OR end_date >= CURDATE())]]>
                </when>
                <when test="ad.status == '대기중'">
                    AND <![CDATA[CURDATE() < start_date]]>
                </when>
                <when test="ad.status == '종료'">
                    AND <![CDATA[CURDATE() > end_date]]>
                </when>
            </choose>
        </if>
        
        <if test="ad.searchType != null and ad.keyword != null">
            <choose>
                <when test="ad.searchType == '공고제목'">
                    AND title LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '근무회사명'">
                    AND company LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '담당자명'">
                    AND manager_name LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '공고번호'">
                    AND ad_num LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
                <when test="ad.searchType == '연락처'">
                    AND manager_phone LIKE CONCAT('%', #{ad.keyword}, '%')
                </when>
            </choose>
        </if>
    </script>
    """)
    int statusListCount(@Param("ad") AdRequest ad)


//    // 한번에 전체, 진행중, 대기중, 종료 공고 개수 반환하는 API
//    @Select("""
//
//    """)


//    // 검색 기능 클릭시 -> 공고제목, 근무지명, 담당자명, 공고번호, 연락처 포함
//    @Select("""
//<script>
//        SELECT *
//        FROM formmail_ad
//        WHERE
//       <if test="ad.status != '전체'">
//        <choose>
//         <when test="ad.status == '전체'">
//
//         </when>
//         <when test="ad.status == '진행중'">
//         </when>
//         <when test="ad.status == '대기중'">
//         </when>
//         <when test="ad.status == '종료'">
//         </when>
//        </choose>
//       </if>
//
//       <if test="ad.status == '전체'">
//        <choose>
//         <when>
//
//         </when>
//        </choose>
//       </if>
//</script>
//    """)

    // 마감 버튼 클릭시 마감 처리할 수 있는 API (오직 마감 기능만)
    @Update("""
        UPDATE formmail_ad
        SET end_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY)
        WHERE aid = #{ad.aid}
    """)
    int updateAdClose(@Param("ad") fmAd ad)

    // 유료상품 등급 수정 API
    @Update("""
        UPDATE formmail_ad
        SET grade = #{ad.grade}
            ,focus = #{ad.focus}
        WHERE aid = #{ad.aid}
    """)
    int updateGrade(@Param("ad") fmAd ad)

    // ---------------------------------------------

    // 잡사이트용 광고 목록 전체 조회 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        ORDER BY updated DESC
        LIMIT #{ad.size} OFFSET #{ad.offset}     
    """)
    List<JobSite> allJobsiteList(@Param("ad") AdRequest ad)

    // 잡사이트용 광고 목록 전체 조회 수 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
    """)
    int allJobsiteListCount()

    // 잡사이트용 grade에 따른 유료 공고 조회 ( 페이징 처리, 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND grade = #{ad.grade}
        ORDER BY updated DESC
        LIMIT #{ad.size} OFFSET #{ad.offset}  
    """)
    List<JobSite> searchGradeJobsite(@Param("ad") AdRequest ad)

    // 잡사이트용 grade에 따른 유료 공고 수 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND grade = #{ad.grade}
    """)
    int searchGradeJobsiteCount(@Param("ad") AdRequest ad)

    // 잡사이트용 포커스 공고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND focus = 1
        ORDER BY updated DESC
        LIMIT #{ad.size} OFFSET #{ad.offset}  
    """)
    List<JobSite> searchFocusJobsite(@Param("ad") AdRequest ad)

    // 잡사이트용 포커스 공고 조회 개수
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND focus = 1
        ORDER BY updated DESC
    """)
    int searchFocusJobsiteCount(@Param("ad") AdRequest ad)

    // 잡 사이트용 title이 포함된 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND title LIKE CONCAT('%', #{ad.title}, '%')     
    """)
    List<JobSite> searchTitleJobsite(@Param("ad") fmAd ad)

    // 잡사이트용 aid 일치하는 광고 상세 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND aid = #{ad.aid}
    """)
    List<JobSite> findOneJobsite(@Param("ad") fmAd ad)

//    // 잡사이트용 aid 일치하는 광고 상세 조회
//    // 광고 상세 조회시 종료 기간 끝난것도 포함하게 수정하였음( 관리자 용 )
    // 01-06 최종적으로 이거 다시 안 쓰기로함.
//    @Select("""
//        SELECT *
//        FROM formmail_ad
//        WHERE
//        aid = #{ad.aid}
//    """)
//    List<JobSite> findOneJobsite(@Param("ad") fmAd ad)

    // 광고 상세 조회시 종료기간 끝난 것인지 체크하기.
    // 진행중인 공고면 1, 아니면 0  // start_date <= 오늘 날짜 <= 종료날짜 or 상시모집
    @Select("""
        SELECT count(*)
        FROM formmail_ad
        WHERE
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        AND aid = #{aid}
    """)
    int checkProgressAd(@Param("aid") String aid)

    // 공고 하나 조회시 조회수 1 증가
    @Update("""
        UPDATE formmail_ad
        SET view_count = #{viewCount}
        WHERE aid = #{aid}
    """)
    int updateViewCount(@Param("aid") String aid, @Param("viewCount") int viewCount)

    // 잡사이트용 aid 일치하는 광고 상세 조회 -> 지원 방법만 뽑기
    // 이거 나중에 login한 유저만 접근 가능하게 막기 위해서임
    @Select("""
        SELECT apply_method
        FROM formmail_ad
        WHERE aid = #{ad.aid}   
    """)
    String applyMethod(@Param("ad") fmAd ad)

    // 잡 사이트용 등록순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        ORDER BY created DESC
        LIMIT #{ad.size} OFFSET #{ad.offset}
    """)
    List<JobSite> orderByCreated(@Param("ad") AdRequest ad)

    // 잡 사이트용 급여 높은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        ORDER BY CAST(max_pay AS UNSIGNED) DESC
        LIMIT #{ad.size} OFFSET #{ad.offset}     
    """)
    List<JobSite> orderByMaxPay(@Param("ad") AdRequest ad)

    // 잡 사이트용 근무일수 적은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE 
        start_date <= CURDATE()
        AND (end_date IS NULL OR end_date >= CURDATE())
        ORDER BY (LENGTH(work_day) - LENGTH(REPLACE(work_day, ',', '')) + 1) ASC
        LIMIT #{ad.size} OFFSET #{ad.offset}    
    """)
    List<JobSite> orderByWorkDay(@Param("ad") AdRequest ad)

//    // 잡 사이트용 근무시간 짧은 순으로 광고 조회 ( 종료기간 끝난것 조회 x )
//    @Select("""
//        SELECT *
//        FROM formmail_ad
//        WHERE CURDATE() BETWEEN start_date AND end_date
//        ORDER BY work_time ASC
//        LIMIT #{paging.size} OFFSET #{paging.offset}
//    """)
//    List<JobSite> orderByWorkTime(@Param("paging") Paging paging)


    // 2024-11-29 잡사이트용 조건, 정렬  / 추후 수정 필요하지만 일단 틀 만들어놓기
    // 기본적으로 페이징 처리와 end_date >= CURDATE() 필수 -> 종료기간 끝난거 제외하기 위해

    // 생각해보니 start_date 와 end_date 사이여야 할 거 같은데, 위에 전부 수정해야하나?

//    // 일단 정렬 , 등록일, 정렬 조건 없이 시/도, 시/군/구 , 동/읍/면에 대해서만
//    @Select("""
//    <script>
//        SELECT *
//        FROM formmail_ad
//        WHERE CURDATE() BETWEEN start_date AND end_date
//        <if test="ad.regions != null and ad.regions.size() > 0">
//            <foreach item="region" index="index" collection="ad.regions" open="AND (" separator="OR" close=")">
//                (sido = #{region.sido} AND sigungu = #{region.sigungu}
//
//                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
//                 )
//                </if>
//                <if test="region.dongEubMyun != null">
//                AND dong_eub_myun = #{region.dongEubMyun})
//                </if>
//            </foreach>
//        </if>
//        LIMIT #{ad.size}
//        OFFSET #{ad.offset}
//    </script>
//    """)
//    List<JobSite> selectByRegions(@Param("ad") AdRequest ad)

    // 시/도, 시/군/구 , 동/읍/면
    // 정렬 조건 추가
    @Select("""
    <script>
        SELECT *
        FROM formmail_ad
        WHERE
        <if test="ad.adType == '단기'">
         work_period IN ("1일", "1주일 이하", "1주일 ~ 1개월") AND
        </if>
        <if test="ad.adType == '급구'"> 
         end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND
        </if>
        <if test="ad.adType == '추천'">
         grade = '1' AND
        </if> 
        <if test="ad.keyword != null">
          title LIKE CONCAT('%', #{ad.keyword}, '%') AND
        </if> 
        <if test="ad.registerType != null">
            <choose>
                <when test="ad.registerType == '오늘 등록'">
                    (end_date IS NULL OR end_date >= CURDATE()) AND start_date = CURDATE()
                </when>
                <when test="ad.registerType == '3일이내 등록'">
                    (end_date IS NULL OR end_date >= CURDATE())
           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND CURDATE() 
                </when>                 
                <when test="ad.registerType == '7일이내 등록'">
                    (end_date IS NULL OR end_date >= CURDATE())
           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND CURDATE()
                </when>                              

            </choose>
        </if>
        <if test="ad.registerType == null">
                  <![CDATA[start_date <= CURDATE() AND 
                         (end_date IS NULL OR end_date >= CURDATE())]]>
        </if>
<if test="ad.regions != null and ad.regions.size() > 0">
    AND (
        <foreach item="region" index="index" collection="ad.regions" separator="OR">
            <if test="region.sido != '전국'">
            (
                (sido = #{region.sido}
                 <if test="region.sigungu != null">
                    AND sigungu = #{region.sigungu}
                 </if>
                 <if test="region.dongEubMyun != null and region.dongEubMyun != ''">
                    AND dong_eub_myun = #{region.dongEubMyun}
                 </if>)
                OR
                (sido2 = #{region.sido}
                 <if test="region.sigungu != null">
                    AND sigungu2 = #{region.sigungu}
                 </if>
                 <if test="region.dongEubMyun != null and region.dongEubMyun != ''">
                    AND dong_eub_myun2 = #{region.dongEubMyun}
                 </if>)
                OR
                (sido3 = #{region.sido}
                 <if test="region.sigungu != null">
                    AND sigungu3 = #{region.sigungu}
                 </if>
                 <if test="region.dongEubMyun != null and region.dongEubMyun != ''">
                    AND dong_eub_myun3 = #{region.dongEubMyun}
                 </if>)
            )
            </if>
            <if test="region.sido == '전국'"> 1=1 </if>  
        </foreach>
    )
</if>

        <if test="ad.salaryType != null">
         AND salary_type = #{ad.salaryType}        
        </if>
        <if test="ad.sortType != null">
            <choose>
                <when test="ad.sortType == '최근등록순'">
                    ORDER BY updated DESC
                </when>
                <otherwise>
                    ORDER BY salary DESC
                </otherwise>
            </choose>
        </if>
        LIMIT #{ad.size}
        OFFSET #{ad.offset}
    </script>
    """)
    List<JobSite> selectByRegionsSort(@Param("ad") AdRequest ad)

    //위 조건 개수
    @Select("""
    <script>
        SELECT count(*)
        FROM formmail_ad
        WHERE
        <if test="ad.adType == '단기'">
         work_period IN ("1일", "1주일 이하", "1주일 ~ 1개월") AND
        </if>
        <if test="ad.adType == '급구'"> 
         end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND
        </if>
        <if test="ad.adType == '추천'">
         grade = '1' AND
        </if>
        <if test="ad.keyword != null">
          title LIKE CONCAT('%', #{ad.keyword}, '%') AND
        </if> 
        <if test="ad.registerType != null">
            <choose>
                <when test="ad.registerType == '오늘 등록'">
                    (end_date IS NULL OR end_date >= CURDATE()) AND start_date = CURDATE()
                </when>
                <when test="ad.registerType == '3일이내 등록'">
                    (end_date IS NULL OR end_date >= CURDATE())
           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND CURDATE() 
                </when>                 
                <when test="ad.registerType == '7일이내 등록'">
                    (end_date IS NULL OR end_date >= CURDATE())
           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND CURDATE()
                </when>                              

            </choose>
        </if>
        <if test="ad.registerType == null">
                            <![CDATA[start_date <= CURDATE() AND 
                         (end_date IS NULL OR end_date >= CURDATE())]]>
        </if>
        
        <if test="ad.regions != null and ad.regions.size() > 0">
         <foreach item="region" index="index" collection="ad.regions" open="AND (" separator="OR" close=")">
              <if test="region.sido != '전국'">
              (
                (sido = #{region.sido} 
                
                <if test="region.sigungu != null">
                AND sigungu = #{region.sigungu} 
                </if>
                
                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
                 )
                </if>
                <if test="region.dongEubMyun != null">
                AND dong_eub_myun = #{region.dongEubMyun})
                </if>
                )
                OR
                (
                 (sido2 = #{region.sido} 
                
                <if test="region.sigungu != null">
                AND sigungu2 = #{region.sigungu} 
                </if>
                
                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
                 )
                </if>
                <if test="region.dongEubMyun != null">
                AND dong_eub_myun2 = #{region.dongEubMyun})
                </if>
                )
                OR
                (
                (sido3 = #{region.sido} 
                
                <if test="region.sigungu != null">
                AND sigungu3 = #{region.sigungu} 
                </if>
                
                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
                 )
                </if>
                <if test="region.dongEubMyun != null">
                AND dong_eub_myun3 = #{region.dongEubMyun})
                </if>
                )
              </if>
              <if test="region.sido == '전국'"> 1=1 </if>  
            </foreach>

        </if>
        <if test="ad.salaryType != null">
         AND salary_type = #{ad.salaryType}        
        </if>
        <if test="ad.sortType != null">
            <choose>
                <when test="ad.sortType == '최근등록순'">
                    ORDER BY updated DESC
                </when>
                <otherwise>
                    ORDER BY salary DESC
                </otherwise>
            </choose>
        </if>
    </script>
    """)
    int selectByRegionsSortCount(@Param("ad") AdRequest ad)


//    // 잡사이트용 급구 공고 ( 마감 기한이 3일밖에 남지 않은 공고들 )
//    @Select("""
//    <script>
//        SELECT *
//        FROM formmail_ad
//        WHERE
//        <if test="ad.adType == '단기'">
//         work_period IN ("1일", "1주일이하", "1주일~1개월") AND
//        </if>
//        <if test="ad.adType == '급구'">
//         end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND
//        </if>
//        <if test="ad.adType == '추천'">
//         grade = '1' AND
//        </if>
//        <if test="ad.registerType != null">
//            <choose>
//                <when test="ad.registerType == '오늘 등록'">
//                    end_date >= CURDATE() AND start_date = CURDATE()
//                </when>
//                <when test="ad.registerType == '3일이내 등록'">
//                    end_date >= CURDATE()
//           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND CURDATE()
//                </when>
//                <when test="ad.registerType == '7일이내 등록'">
//                    end_date >= CURDATE()
//           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND CURDATE()
//                </when>
//
//            </choose>
//        </if>
//        <if test="ad.registerType == null">
//            CURDATE() BETWEEN start_date AND end_date
//        </if>
//
//        <if test="ad.regions != null and ad.regions.size() > 0">
//            <foreach item="region" index="index" collection="ad.regions" open="AND (" separator="OR" close=")">
//                (sido = #{region.sido}
//
//                <if test="region.sigungu != null">
//                AND sigungu = #{region.sigungu}
//                </if>
//
//                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
//                 )
//                </if>
//                <if test="region.dongEubMyun != null">
//                AND dong_eub_myun = #{region.dongEubMyun})
//                </if>
//            </foreach>
//        </if>
//        <if test="ad.salaryType != null">
//         AND salary_type = #{ad.salaryType}
//        </if>
//        <if test="ad.sortType != null">
//            <choose>
//                <when test="ad.sortType == '최신등록순'">
//                    ORDER BY created_at DESC
//                </when>
//                <otherwise>
//                    ORDER BY salary DESC
//                </otherwise>
//            </choose>
//        </if>
//        LIMIT #{ad.size}
//        OFFSET #{ad.offset}
//    </script>
//    """)
//    List<JobSite> hurriedAdList(@Param("ad") AdRequest ad)

//    // 잡사이트용 급구 공고 개수( 마감 기한이 3일밖에 남지 않은 공고들 )
//    @Select("""
//    <script>
//        SELECT count(*)
//        FROM formmail_ad
//        WHERE
//        <if test="ad.adType == '단기'">
//         work_period IN ("1일", "1주일이하", "1주일~1개월") AND
//        </if>
//        <if test="ad.adType == '급구'">
//         end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND
//        </if>
//        <if test="ad.adType == '추천'">
//         grade = '1' AND
//        </if>
//        <if test="ad.registerType != null">
//            <choose>
//                <when test="ad.registerType == '오늘 등록'">
//                    end_date >= CURDATE() AND start_date = CURDATE()
//                </when>
//                <when test="ad.registerType == '3일이내 등록'">
//                    end_date >= CURDATE()
//           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND CURDATE()
//                </when>
//                <when test="ad.registerType == '7일이내 등록'">
//                    end_date >= CURDATE()
//           AND start_date BETWEEN DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND CURDATE()
//                </when>
//
//            </choose>
//        </if>
//        <if test="ad.registerType == null">
//            CURDATE() BETWEEN start_date AND end_date
//        </if>
//
//        <if test="ad.regions != null and ad.regions.size() > 0">
//            <foreach item="region" index="index" collection="ad.regions" open="AND (" separator="OR" close=")">
//                (sido = #{region.sido}
//
//                <if test="region.sigungu != null">
//                AND sigungu = #{region.sigungu}
//                </if>
//
//                <if test="region.dongEubMyun == null or region.dongEubMyun == ''">
//                 )
//                </if>
//                <if test="region.dongEubMyun != null">
//                AND dong_eub_myun = #{region.dongEubMyun})
//                </if>
//            </foreach>
//        </if>
//        <if test="ad.salaryType != null">
//         AND salary_type = #{ad.salaryType}
//        </if>
//        <if test="ad.sortType != null">
//            <choose>
//                <when test="ad.sortType == '최신등록순'">
//                    ORDER BY created_at DESC
//                </when>
//                <otherwise>
//                    ORDER BY salary DESC
//                </otherwise>
//            </choose>
//        </if>
//        LIMIT #{ad.size}
//        OFFSET #{ad.offset}
//    </script>
//    """)
//    int hurriedAdListCount(@Param("ad") AdRequest ad)






    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    //-------------------------------------

//    // aid로 cid 찾기
//    @Select("""
//        SELECT cid
//        FROM formmail_ad
//        WHERE aid = #{ad.aid}
//    """)
//    String findCid(@Param("ad") fmAd ad)

//    // aid 입력 후 찾은 cid로 고객사 정보와 유저 정보 찾기
//    @Select("""
//        SELECT
//        fc.company_name
//        , fc.company_branch
//        , fa.r_name
//        , fa.user_name
//        , fa.position
//        , fa.user_id
//        , fc.cid
//        FROM formmail_company fc
//        JOIN formmail_admin fa ON fc.mid = fa.user_id
//        WHERE fc.cid = #{cid}
//        LIMIT 1;
//    """)
//    List<findCompanyAndUser> findCompanyAndUser(@Param("cid") String cid)


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
    // 기업회원용
    @Select("""
        SELECT count(*)
        FROM formmail_file
        WHERE logo_img = #{adImage.logoImg}
        AND company_user_id = #{adImage.companyUserId}
    """)
    int dupImgUrl(@Param("adImage") fmAd adImage)


    // 잡사이트용 광고 목록 전체 조회 (페이징 처리, 종료기간 끝난것 조회 x)
    @Select("""
        SELECT *
        FROM formmail_ad
        WHERE end_date >= CURDATE()
    """)
    List<JobSite> jobSiteListTest()


    // ㅡㅡㅡㅡㅡㅡㅡㅡ 주변 지하철, 거리 등 정보 테이블 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 주변 정보 등록하기
    @Insert("""
        INSERT INTO formmail_ad_near(
           aid
           , near_station
           , distance
           , duration_time
           , line
           , subway_color
        ) VALUES (
           #{near.aid}
           ,#{near.nearStation}
           ,#{near.distance}
           ,#{near.durationTime}
           ,#{near.line}
           ,#{near.subwayColor}
        )
    """)
    int addNearInfo(@Param("near") AdNearInfo near)


    // 공고 수정시 aid 일치하는 것 전체 삭제
    @Delete("""
        DELETE FROM formmail_ad_near
        WHERE aid = #{ad.aid}
    """)
    int deleteNearInfo(@Param("ad") fmAd ad)

    // aid 일치하는 주변 역 정보들 추출
    @Select("""
        SELECT *
        FROM formmail_ad_near
        WHERE aid = #{near.aid}
    """)
    List<AdNearInfo> nearInfoList(@Param("near") AdNearInfo near)

//    @Select("""
//        SELECT count(*)
//        FROM formmail_ad_near
//        WHERE aid = #{near.aid}
//        AND near_station = #{near.nearStation}
//    """)
//    int dupChkNearInfo(@Param("near") AdNearInfo near)
//
//    // 선택 해제시 aid, 지하철역 일치하는 것 삭제
//    @Delete("""
//        DELETE FROM formmail_ad_near
//        WHERE aid = #{near.aid}
//        AND near_station = #{near.nearStation}
//    """)
//    int deleteOneNearInfo(@Param("near") AdNearInfo near)

//    // 등록 성공시 aid 일치하는 애들 전부 Y로 상태 변경
//    @Update("""
//        UPDATE formmail_ad_near
//        SET status = 'Y'
//        WHERE aid = #{ad.aid}
//    """)
//    int updateStatusY(@Param("near") fmAd ad)

    // 시도 -> 시,군,구 목록 조회
    @Select("""
        SELECT *
        FROM formmail_regions
        WHERE sido = #{re.sido}
        ORDER BY sigungu ASC;
    """)
    List<Regions> sigunguList(@Param("re") Regions re)

    // 시,군,구 -> 동,읍,면 목록 조회
    @Select("""
        SELECT *
        FROM formmail_regions
        WHERE sido = #{re.sido} 
        AND sigungu = #{re.sigungu}
        ORDER BY dong_eub_myun ASC;
    """)
    List<Regions> dongEubMyunList(@Param("re") Regions re)



}