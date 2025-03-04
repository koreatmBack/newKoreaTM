package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-30
 * comment : 폼메일 어드민 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormMailAdmin {

    private String userId;
    private String userPwd;

    @JsonProperty("rName")
    private String rName;
    @JsonProperty("userName")
    private String userName;
    private String position;
    private String team;
    @JsonProperty("mPhone")
    private String mPhone;
    @JsonProperty("rPhone")
    private String rPhone;
    private String email;
    private String createdAt;  // 등록일
    private String updatedAt;  // 수정일
    private String role;
    private String formNo;
    private String rank;
    private Boolean useStatus;

    private String keyword; //  검색시

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int offset;
}
