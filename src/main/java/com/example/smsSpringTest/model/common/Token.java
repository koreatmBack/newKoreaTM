package com.example.smsSpringTest.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : JwtTokenProvider의 토큰 데이터 저장 vo
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpirationTime;
    private Long refreshTokenExpirationTime;

}
