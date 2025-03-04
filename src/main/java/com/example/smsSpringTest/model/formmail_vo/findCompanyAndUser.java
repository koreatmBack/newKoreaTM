package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-11
 * comment : 고객사와 유저 정보 반환 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class findCompanyAndUser {

    private String companyName;

    private String companyBranch;

    private String userName;

    @JsonProperty("rName")
    private String rName;

    private String position;

    private String userId;

    private String cid;


}
