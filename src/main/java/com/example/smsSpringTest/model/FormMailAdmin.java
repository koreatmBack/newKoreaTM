package com.example.smsSpringTest.model;

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
    private boolean admin;
    private String team;
    @JsonProperty("mPhone")
    private String mPhone;
    @JsonProperty("rPhone")
    private String rPhone;
    private String role;
}
