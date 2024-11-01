package com.example.smsSpringTest.model.jobsite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-11-01
 * comment : 소셜 회원가입 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Social {

    private String userId;
    private String socialId;
    private String socialType;
    private String regDate;
    private String uptDate;
}