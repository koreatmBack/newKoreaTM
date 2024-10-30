package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : jobsite 유저 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobsiteUser {

    private String userId;

    private String userPwd;

    private String userName;

    private String phone;

    private String address;

    private String sido;

    private String sigungu;

    private String gender;

    private String birth;

    private String photo;

    private String marketing;

    private String addressDetail;

    private String role;

}
