package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String userId;

    private String applyName;

    private String gender;
    private String birth;

    @JsonProperty("aPhone")
    private String aPhone;

    private String address;
    private String appliedTime;
    private String interviewTime;
    private String adminMemo;
    private String interviewMemo;
    private String lastAppliedTime;

}
