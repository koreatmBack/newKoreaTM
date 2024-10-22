package com.example.smsSpringTest.model.common;

import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : 회원 토큰 저장 vo : RefreshToken
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefToken extends ApiResponse {

    private String userId;
    private String grantType;
    private String refreshToken;
    private String resolveToken;
    private Long refreshTokenExpirationTime;
    private String regDate;
    private String uptDate;
    private String useYn;

}