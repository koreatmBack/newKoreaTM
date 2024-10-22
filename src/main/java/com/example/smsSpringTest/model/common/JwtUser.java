package com.example.smsSpringTest.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : jwt 회원 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUser {

    @NotBlank
    private String userId;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPwd;

    @NotBlank
    private String mainAddr;

    private String userName;
    private String phone;
    private String birth;
    private String gender;
    private String email;
    private int point;
    private RoleType Role;
    private String useStatus;
    private String socialType;
    private String agreeYn;
    private String tempId;
    private int loginFailCnt;
    private String beforePwd;
    private String afterPwd;
    private String pointReceivedYn;
    private String promoYn;
    private String leaveDate;
    private String pointExpiryDate;
    private String pointReceicedDate;
    private String agreeDate;
    private String refreshToken;
    private String regDate;
    private String uptDate;
    private String delDate;
}
