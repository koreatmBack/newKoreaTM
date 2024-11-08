package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private String socialId; // 소셜로그인시 받은 고유 id

    private String socialType; // 소셜로그인시 받은 type

    private String favorite; // 즐겨찾기

    private String clipping; // 스크랩

    private LocalDate createdAt; // 가입일

}
