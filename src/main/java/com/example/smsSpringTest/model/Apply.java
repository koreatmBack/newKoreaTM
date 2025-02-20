package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 지원자 조회용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Apply {

    private int no;

    private String applyId;

    private String aid;

    private String cid;

    private String managerId;

    private String managerName;

    private String company;

    private String partner;

    private String applyName;

    private String applyBirth;

    private String applyGender;

    private String applyAddress;

    private String applyPhone;

    private String interviewTime;

    private String sido;

    private String sigungu;

    private String addressDetail;

    private String applyDate;

    private String applyStatus;

    private String applyPath;

    private String lastModified;

    private String interviewSort; // 면접시간 기준 정렬 키값

    private String surveyStatus; // 면접 질의서

    private String surveyStatusSort; // 면접 질의서 기준 내림차순 정렬

    private String applyCareer; // 신입 or 경력

    private String managerMemo; // 담당자 메모

    private String interviewMemo; // 면접 메모

    private String survey; // 면접질의서 url
    private String blacklist; // 블랙리스트 사유

    private String searchType;    // 검색타입
    private String searchKeyword; // 검색어

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int offset;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String formNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rank;

}
