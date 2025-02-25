package com.example.smsSpringTest.mapper

import com.example.smsSpringTest.model.Survey
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface SurveyMapper {

    // 2025-02-24 ~
    // 면접 질의서 매퍼



    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

// ---------------- 설문 조사 start ---------------- 일단 사용 X
    // 설문 조사 등록
    @Insert("""
        INSERT INTO formmail_survey(
            surv_id
            , cid
            , survey_type
      ,answerA_1
     ,answerA_2
     ,answerA_3
     ,answerA_4
     ,answerA_5
     ,answerA_6
     ,answerA_7
     ,answerA_8
     ,answerA_9
     ,answerA_10
     ,answerA_11
     ,answerA_12
     ,answerA_13
     ,answerA_14
     ,answerA_15
     ,answerA_16
     ,answerA_17
     ,answerA_18
     ,answerA_19
     ,answerA_20
     ,answerA_21
     ,answerA_22
     ,answerA_23
     ,answerA_24
     ,answerA_25
     ,answerA_26
     ,answerA_27
     ,answerA_28
     ,answerA_29
     ,answerA_30
          ,answerB_1
     ,answerB_2
     ,answerB_3
     ,answerB_4
     ,answerB_5
     ,answerB_6
     ,answerB_7
     ,answerB_8
     ,answerB_9
     ,answerB_10
     ,answerB_11
     ,answerB_12
     ,answerB_13
     ,answerB_14
     ,answerB_15
     ,answerB_16
     ,answerB_17
     ,answerB_18
     ,answerB_19
     ,answerB_20
     ,answerB_21
     ,answerB_22
     ,answerB_23
     ,answerB_24
     ,answerB_25
     ,answerB_26
     ,answerB_27
     ,answerB_28
     ,answerB_29
     ,answerB_30
          ,answerC_1
     ,answerC_2
     ,answerC_3
     ,answerC_4
     ,answerC_5
     ,answerC_6
     ,answerC_7
     ,answerC_8
     ,answerC_9
     ,answerC_10
     ,answerC_11
     ,answerC_12
     ,answerC_13
     ,answerC_14
     ,answerC_15
     ,answerC_16
     ,answerC_17
     ,answerC_18
     ,answerC_19
     ,answerC_20
     ,answerC_21
     ,answerC_22
     ,answerC_23
     ,answerC_24
     ,answerC_25
     ,answerC_26
     ,answerC_27
     ,answerC_28
     ,answerC_29
     ,answerC_30
          ,answerD_1
     ,answerD_2
     ,answerD_3
     ,answerD_4
     ,answerD_5
     ,answerD_6
     ,answerD_7
     ,answerD_8
     ,answerD_9
     ,answerD_10
     ,answerD_11
     ,answerD_12
     ,answerD_13
     ,answerD_14
     ,answerD_15
     ,answerD_16
     ,answerD_17
     ,answerD_18
     ,answerD_19
     ,answerD_20
     ,answerD_21
     ,answerD_22
     ,answerD_23
     ,answerD_24
     ,answerD_25
     ,answerD_26
     ,answerD_27
     ,answerD_28
     ,answerD_29
     ,answerD_30
        ) VALUES (
            #{surv.survId}
            , #{surv.cid}
            , #{surv.surveyType}
                 ,#{surv.answerA1}
     ,#{surv.answerA2}
     ,#{surv.answerA3}
     ,#{surv.answerA4}
     ,#{surv.answerA5}
     ,#{surv.answerA6}
     ,#{surv.answerA7}
     ,#{surv.answerA8}
     ,#{surv.answerA9}
     ,#{surv.answerA10}
     ,#{surv.answerA11}
     ,#{surv.answerA12}
     ,#{surv.answerA13}
     ,#{surv.answerA14}
     ,#{surv.answerA15}
     ,#{surv.answerA16}
     ,#{surv.answerA17}
     ,#{surv.answerA18}
     ,#{surv.answerA19}
     ,#{surv.answerA20}
     ,#{surv.answerA21}
     ,#{surv.answerA22}
     ,#{surv.answerA23}
     ,#{surv.answerA24}
     ,#{surv.answerA25}
     ,#{surv.answerA26}
     ,#{surv.answerA27}
     ,#{surv.answerA28}
     ,#{surv.answerA29}
     ,#{surv.answerA30 }
          ,#{surv.answerB1}
     ,#{surv.answerB2}
     ,#{surv.answerB3}
     ,#{surv.answerB4}
     ,#{surv.answerB5}
     ,#{surv.answerB6}
     ,#{surv.answerB7}
     ,#{surv.answerB8}
     ,#{surv.answerB9}
     ,#{surv.answerB10}
     ,#{surv.answerB11}
     ,#{surv.answerB12}
     ,#{surv.answerB13}
     ,#{surv.answerB14}
     ,#{surv.answerB15}
     ,#{surv.answerB16}
     ,#{surv.answerB17}
     ,#{surv.answerB18}
     ,#{surv.answerB19}
     ,#{surv.answerB20}
     ,#{surv.answerB21}
     ,#{surv.answerB22}
     ,#{surv.answerB23}
     ,#{surv.answerB24}
     ,#{surv.answerB25}
     ,#{surv.answerB26}
     ,#{surv.answerB27}
     ,#{surv.answerB28}
     ,#{surv.answerB29}
     ,#{surv.answerB30}
          ,#{surv.answerC1}
     ,#{surv.answerC2}
     ,#{surv.answerC3}
     ,#{surv.answerC4}
     ,#{surv.answerC5}
     ,#{surv.answerC6}
     ,#{surv.answerC7}
     ,#{surv.answerC8}
     ,#{surv.answerC9}
     ,#{surv.answerC10}
     ,#{surv.answerC11}
     ,#{surv.answerC12}
     ,#{surv.answerC13}
     ,#{surv.answerC14}
     ,#{surv.answerC15}
     ,#{surv.answerC16}
     ,#{surv.answerC17}
     ,#{surv.answerC18}
     ,#{surv.answerC19}
     ,#{surv.answerC20}
     ,#{surv.answerC21}
     ,#{surv.answerC22}
     ,#{surv.answerC23}
     ,#{surv.answerC24}
     ,#{surv.answerC25}
     ,#{surv.answerC26}
     ,#{surv.answerC27}
     ,#{surv.answerC28}
     ,#{surv.answerC29}
     ,#{surv.answerC30}
          ,#{surv.answerD1}
     ,#{surv.answerD2}
     ,#{surv.answerD3}
     ,#{surv.answerD4}
     ,#{surv.answerD5}
     ,#{surv.answerD6}
     ,#{surv.answerD7}
     ,#{surv.answerD8}
     ,#{surv.answerD9}
     ,#{surv.answerD10}
     ,#{surv.answerD11}
     ,#{surv.answerD12}
     ,#{surv.answerD13}
     ,#{surv.answerD14}
     ,#{surv.answerD15}
     ,#{surv.answerD16}
     ,#{surv.answerD17}
     ,#{surv.answerD18}
     ,#{surv.answerD19}
     ,#{surv.answerD20}
     ,#{surv.answerD21}
     ,#{surv.answerD22}
     ,#{surv.answerD23}
     ,#{surv.answerD24}
     ,#{surv.answerD25}
     ,#{surv.answerD26}
     ,#{surv.answerD27}
     ,#{surv.answerD28}
     ,#{surv.answerD29}
     ,#{surv.answerD30}
        )
    """)
    int addSurvey(@Param("surv") Survey surv)

    // 설문 조사 전체 조회
    @Select("""
        SELECT *
        FROM formmail_survey
    """)
    List<Survey> surveyList()

    // survId, cid 일치하는 설문 조회
    @Select("""
        SELECT *
        FROM formmail_survey
        WHERE survey_type = #{surv.surveyType}
        AND cid = #{surv.cid}
    """)
    List<Survey> selectSurveyList(@Param("surv") Survey surv)

    // 이미 등록된 설문지가 있는지 체크
    @Select("""
        SELECT surv_id
        FROM formmail_survey
        WHERE survey_type = #{surv.surveyType}
        AND cid = #{surv.cid}
    """)
    String dupSurvey(@Param("surv") Survey surv)

    // 전체 설문 수
    @Select("""
        SELECT count(*)
        FROM formmail_survey
    """)
    int getSurveyListCount()

    // 설문 조사 수정
    @Update("""
    <script>
        UPDATE formmail_survey
      <set>
        <if test="surv.surveyType != null"> survey_type = #{surv.surveyType}, </if>

                    <!-- answerA -->
        <if test="surv.answerA1 != null"> answerA_1 = #{surv.answerA1}, </if>
        <if test="surv.answerA2 != null"> answerA_2 = #{surv.answerA2}, </if>
        <if test="surv.answerA3 != null"> answerA_3 = #{surv.answerA3}, </if>
        <if test="surv.answerA4 != null"> answerA_4 = #{surv.answerA4}, </if>
        <if test="surv.answerA5 != null"> answerA_5 = #{surv.answerA5}, </if>
        <if test="surv.answerA6 != null"> answerA_6 = #{surv.answerA6}, </if>
        <if test="surv.answerA7 != null"> answerA_7 = #{surv.answerA7}, </if>
        <if test="surv.answerA8 != null"> answerA_8 = #{surv.answerA8}, </if>
        <if test="surv.answerA9 != null"> answerA_9 = #{surv.answerA9}, </if>
        <if test="surv.answerA10 != null"> answerA_10 = #{surv.answerA10}, </if>
        <if test="surv.answerA11 != null"> answerA_11 = #{surv.answerA11}, </if>
        <if test="surv.answerA12 != null"> answerA_12 = #{surv.answerA12}, </if>
        <if test="surv.answerA13 != null"> answerA_13 = #{surv.answerA13}, </if>
        <if test="surv.answerA14 != null"> answerA_14 = #{surv.answerA14}, </if>
        <if test="surv.answerA15 != null"> answerA_15 = #{surv.answerA15}, </if>
        <if test="surv.answerA16 != null"> answerA_16 = #{surv.answerA16}, </if>
        <if test="surv.answerA17 != null"> answerA_17 = #{surv.answerA17}, </if>
        <if test="surv.answerA18 != null"> answerA_18 = #{surv.answerA18}, </if>
        <if test="surv.answerA19 != null"> answerA_19 = #{surv.answerA19}, </if>
        <if test="surv.answerA20 != null"> answerA_20 = #{surv.answerA20}, </if>
        <if test="surv.answerA21 != null"> answerA_21 = #{surv.answerA21}, </if>
        <if test="surv.answerA22 != null"> answerA_22 = #{surv.answerA22}, </if>
        <if test="surv.answerA23 != null"> answerA_23 = #{surv.answerA23}, </if>
        <if test="surv.answerA24 != null"> answerA_24 = #{surv.answerA24}, </if>
        <if test="surv.answerA25 != null"> answerA_25 = #{surv.answerA25}, </if>
        <if test="surv.answerA26 != null"> answerA_26 = #{surv.answerA26}, </if>
        <if test="surv.answerA27 != null"> answerA_27 = #{surv.answerA27}, </if>
        <if test="surv.answerA28 != null"> answerA_28 = #{surv.answerA28}, </if>
        <if test="surv.answerA29 != null"> answerA_29 = #{surv.answerA29}, </if>
        <if test="surv.answerA30 != null"> answerA_30 = #{surv.answerA30}, </if>

        <!-- answerB -->
        <if test="surv.answerB1 != null"> answerB_1 = #{surv.answerB1}, </if>
        <if test="surv.answerB2 != null"> answerB_2 = #{surv.answerB2}, </if>
        <if test="surv.answerB3 != null"> answerB_3 = #{surv.answerB3}, </if>
        <if test="surv.answerB4 != null"> answerB_4 = #{surv.answerB4}, </if>
        <if test="surv.answerB5 != null"> answerB_5 = #{surv.answerB5}, </if>
        <if test="surv.answerB6 != null"> answerB_6 = #{surv.answerB6}, </if>
        <if test="surv.answerB7 != null"> answerB_7 = #{surv.answerB7}, </if>
        <if test="surv.answerB8 != null"> answerB_8 = #{surv.answerB8}, </if>
        <if test="surv.answerB9 != null"> answerB_9 = #{surv.answerB9}, </if>
        <if test="surv.answerB10 != null"> answerB_10 = #{surv.answerB10}, </if>
        <if test="surv.answerB11 != null"> answerB_11 = #{surv.answerB11}, </if>
        <if test="surv.answerB12 != null"> answerB_12 = #{surv.answerB12}, </if>
        <if test="surv.answerB13 != null"> answerB_13 = #{surv.answerB13}, </if>
        <if test="surv.answerB14 != null"> answerB_14 = #{surv.answerB14}, </if>
        <if test="surv.answerB15 != null"> answerB_15 = #{surv.answerB15}, </if>
        <if test="surv.answerB16 != null"> answerB_16 = #{surv.answerB16}, </if>
        <if test="surv.answerB17 != null"> answerB_17 = #{surv.answerB17}, </if>
        <if test="surv.answerB18 != null"> answerB_18 = #{surv.answerB18}, </if>
        <if test="surv.answerB19 != null"> answerB_19 = #{surv.answerB19}, </if>
        <if test="surv.answerB20 != null"> answerB_20 = #{surv.answerB20}, </if>
        <if test="surv.answerB21 != null"> answerB_21 = #{surv.answerB21}, </if>
        <if test="surv.answerB22 != null"> answerB_22 = #{surv.answerB22}, </if>
        <if test="surv.answerB23 != null"> answerB_23 = #{surv.answerB23}, </if>
        <if test="surv.answerB24 != null"> answerB_24 = #{surv.answerB24}, </if>
        <if test="surv.answerB25 != null"> answerB_25 = #{surv.answerB25}, </if>
        <if test="surv.answerB26 != null"> answerB_26 = #{surv.answerB26}, </if>
        <if test="surv.answerB27 != null"> answerB_27 = #{surv.answerB27}, </if>
        <if test="surv.answerB28 != null"> answerB_28 = #{surv.answerB28}, </if>
        <if test="surv.answerB29 != null"> answerB_29 = #{surv.answerB29}, </if>
        <if test="surv.answerB30 != null"> answerB_30 = #{surv.answerB30}, </if>

                <!-- answerC -->
        <if test="surv.answerC1 != null"> answerC_1 = #{surv.answerC1}, </if>
        <if test="surv.answerC2 != null"> answerC_2 = #{surv.answerC2}, </if>
        <if test="surv.answerC3 != null"> answerC_3 = #{surv.answerC3}, </if>
        <if test="surv.answerC4 != null"> answerC_4 = #{surv.answerC4}, </if>
        <if test="surv.answerC5 != null"> answerC_5 = #{surv.answerC5}, </if>
        <if test="surv.answerC6 != null"> answerC_6 = #{surv.answerC6}, </if>
        <if test="surv.answerC7 != null"> answerC_7 = #{surv.answerC7}, </if>
        <if test="surv.answerC8 != null"> answerC_8 = #{surv.answerC8}, </if>
        <if test="surv.answerC9 != null"> answerC_9 = #{surv.answerC9}, </if>
        <if test="surv.answerC10 != null"> answerC_10 = #{surv.answerC10}, </if>
        <if test="surv.answerC11 != null"> answerC_11 = #{surv.answerC11}, </if>
        <if test="surv.answerC12 != null"> answerC_12 = #{surv.answerC12}, </if>
        <if test="surv.answerC13 != null"> answerC_13 = #{surv.answerC13}, </if>
        <if test="surv.answerC14 != null"> answerC_14 = #{surv.answerC14}, </if>
        <if test="surv.answerC15 != null"> answerC_15 = #{surv.answerC15}, </if>
        <if test="surv.answerC16 != null"> answerC_16 = #{surv.answerC16}, </if>
        <if test="surv.answerC17 != null"> answerC_17 = #{surv.answerC17}, </if>
        <if test="surv.answerC18 != null"> answerC_18 = #{surv.answerC18}, </if>
        <if test="surv.answerC19 != null"> answerC_19 = #{surv.answerC19}, </if>
        <if test="surv.answerC20 != null"> answerC_20 = #{surv.answerC20}, </if>
        <if test="surv.answerC21 != null"> answerC_21 = #{surv.answerC21}, </if>
        <if test="surv.answerC22 != null"> answerC_22 = #{surv.answerC22}, </if>
        <if test="surv.answerC23 != null"> answerC_23 = #{surv.answerC23}, </if>
        <if test="surv.answerC24 != null"> answerC_24 = #{surv.answerC24}, </if>
        <if test="surv.answerC25 != null"> answerC_25 = #{surv.answerC25}, </if>
        <if test="surv.answerC26 != null"> answerC_26 = #{surv.answerC26}, </if>
        <if test="surv.answerC27 != null"> answerC_27 = #{surv.answerC27}, </if>
        <if test="surv.answerC28 != null"> answerC_28 = #{surv.answerC28}, </if>
        <if test="surv.answerC29 != null"> answerC_29 = #{surv.answerC29}, </if>
        <if test="surv.answerC30 != null"> answerC_30 = #{surv.answerC30}, </if>

                <!-- answerD -->
        <if test="surv.answerD1 != null"> answerD_1 = #{surv.answerD1}, </if>
        <if test="surv.answerD2 != null"> answerD_2 = #{surv.answerD2}, </if>
        <if test="surv.answerD3 != null"> answerD_3 = #{surv.answerD3}, </if>
        <if test="surv.answerD4 != null"> answerD_4 = #{surv.answerD4}, </if>
        <if test="surv.answerD5 != null"> answerD_5 = #{surv.answerD5}, </if>
        <if test="surv.answerD6 != null"> answerD_6 = #{surv.answerD6}, </if>
        <if test="surv.answerD7 != null"> answerD_7 = #{surv.answerD7}, </if>
        <if test="surv.answerD8 != null"> answerD_8 = #{surv.answerD8}, </if>
        <if test="surv.answerD9 != null"> answerD_9 = #{surv.answerD9}, </if>
        <if test="surv.answerD10 != null"> answerD_10 = #{surv.answerD10}, </if>
        <if test="surv.answerD11 != null"> answerD_11 = #{surv.answerD11}, </if>
        <if test="surv.answerD12 != null"> answerD_12 = #{surv.answerD12}, </if>
        <if test="surv.answerD13 != null"> answerD_13 = #{surv.answerD13}, </if>
        <if test="surv.answerD14 != null"> answerD_14 = #{surv.answerD14}, </if>
        <if test="surv.answerD15 != null"> answerD_15 = #{surv.answerD15}, </if>
        <if test="surv.answerD16 != null"> answerD_16 = #{surv.answerD16}, </if>
        <if test="surv.answerD17 != null"> answerD_17 = #{surv.answerD17}, </if>
        <if test="surv.answerD18 != null"> answerD_18 = #{surv.answerD18}, </if>
        <if test="surv.answerD19 != null"> answerD_19 = #{surv.answerD19}, </if>
        <if test="surv.answerD20 != null"> answerD_20 = #{surv.answerD20}, </if>
        <if test="surv.answerD21 != null"> answerD_21 = #{surv.answerD21}, </if>
        <if test="surv.answerD22 != null"> answerD_22 = #{surv.answerD22}, </if>
        <if test="surv.answerD23 != null"> answerD_23 = #{surv.answerD23}, </if>
        <if test="surv.answerD24 != null"> answerD_24 = #{surv.answerD24}, </if>
        <if test="surv.answerD25 != null"> answerD_25 = #{surv.answerD25}, </if>
        <if test="surv.answerD26 != null"> answerD_26 = #{surv.answerD26}, </if>
        <if test="surv.answerD27 != null"> answerD_27 = #{surv.answerD27}, </if>
        <if test="surv.answerD28 != null"> answerD_28 = #{surv.answerD28}, </if>
        <if test="surv.answerD29 != null"> answerD_29 = #{surv.answerD29}, </if>
        <if test="surv.answerD30 != null"> answerD_30 = #{surv.answerD30}, </if>
      </set>
        WHERE surv_id = #{surv.survId}
        AND cid = #{surv.cid}
    </script>
    """)
    int updateSurvey(@Param("surv") Survey surv)

    // 설문 조사 삭제
    @Delete("""
        DELETE FROM formmail_survey
        WHERE surv_id = #{surv.survId}
        AND cid = #{surv.cid}
    """)
    int deleteSurvey(@Param("surv") Survey surv)

// ---------------- 설문 조사 end ----------------









}