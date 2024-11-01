package com.example.smsSpringTest.model.response.jobsite;

import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.jobsite.SocialUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-11-01
 * comment : 소셜 회원 결과값 리턴
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocialResponse extends ApiResponse {

    private SocialUser socialUser;

    private JobsiteUser user;

    private String socialId; // 소셜로그인시 받은 고유 id

    private String socialType; // 소셜로그인시 받은 type
}