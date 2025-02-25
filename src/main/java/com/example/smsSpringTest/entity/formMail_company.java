package com.example.smsSpringTest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * author : 신기훈
 * date : 2024-09-23
 * comment : 고객사 entity
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "formmail_company")
public class formMail_company {

    @Id
    @Column(name = "cid")
    private String cid;

    @Column(name = "company_name")
    private String companyName;

    private String gubun;

    private String channel;

    @Column(name = "company_branch")
    private String companyBranch;

    private String manager1;

    private String manager2;

    @Column(name = "c_phone1")
    @JsonProperty("cPhone1")
    private String cPhone1;

    @Column(name = "c_phone2")
    @JsonProperty("cPhone2")
    private String cPhone2;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private FormMailAdminEntity mid;

    @Column(name="survey_type")
    @JsonProperty("surveyType")
    private String surveyType;

    private String partner;

    private String address;

    private String industry;

    private String sido;

    private String sigungu;

    private Boolean surveyProceed; // 면접 질의서 진행 구분
    private Boolean comProceed; // 고객사 진행 구분
}
