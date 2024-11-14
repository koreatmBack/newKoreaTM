package com.example.smsSpringTest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-11-14
 * comment : 네이버 지도 x, y 값
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NaverMapResponse extends ApiResponse{
    private String x;
    private String y;
}
