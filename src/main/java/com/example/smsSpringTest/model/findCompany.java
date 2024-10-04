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
public class findCompany {

    private String cid;

    private String companyName;

    private String gubun;

    private String manager1;

    @JsonProperty("cPhone1")
    private String cPhone1;

    private String channel;

    private String companyBranch;

    @JsonProperty("mid")
    private String mid;

    @JsonProperty("rName")
    private String rName;

//    @JsonProperty("user_name")
    private String userName;

    private String position;

    private String surveyType;

    private String partner;

}