package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-09-24
 * comment : 고객사 조회용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    private String cid;

    private String companyName;

    private String gubun;

    private String manager1;

    private String manager2;

    @JsonProperty("cPhone1")
    private String cPhone1;

    @JsonProperty("cPhone2")
    private String cPhone2;

    private String channel;

    private String companyBranch;

    @JsonProperty("managerId")
    private String managerId;

    @JsonProperty("rName")
    private String rName;

//    @JsonProperty("user_name")
    private String userName;

    private String position;

    @JsonProperty("mPhone")
    private String mPhone;

    private String surveyType;

    private String partner;

    private String address;

    private String industry;

    private String sido;

    private String sigungu;

    private Boolean surveyProceed; // 면접 질의서 진행 구분
    private Boolean comProceed; // 고객사 진행 구분
}