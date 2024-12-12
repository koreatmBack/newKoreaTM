package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String email;

    private String userNewPwd; // 새로운 비밀번호 (비밀번호 변경용)

    private String agreeOver15; // 만 15세 이상 약관 동의

    private String agreeTerms; // 이용약관 동의 여부

    private String agreePrivacy; // 개인정보 수집 및 이용 동의 여부

    private String agreeSmsMarketing; // 광고성 정보 수신 동의 (SMS/MMS)

    private String agreeEmailMarketing; // 광고성 정보 수신 동의 (E-mail)

    private LocalDateTime agreeDate; // 약관 동의 날짜
}
