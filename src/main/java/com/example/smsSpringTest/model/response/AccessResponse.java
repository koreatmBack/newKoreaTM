package com.example.smsSpringTest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-25
 * comment : accessToken Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessResponse extends ApiResponse{

    private int limit;  // 쿠키 (access토큰) 남은시간 반환하기

}
