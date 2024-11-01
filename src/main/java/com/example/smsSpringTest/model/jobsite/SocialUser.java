package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-11-01
 * comment : 소셜 로그인 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocialUser {

    private String id;
    private String userId;
    private String socialId;
    private String email;
    private String socialType;
    private String userName;
    private String refreshToken;
}
