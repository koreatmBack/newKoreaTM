package com.example.smsSpringTest.model;


import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-11-26
 * comment : 지도 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapVO extends ApiResponse {
    private double x;  // 경도 , longitude ex : 37.5361988
    private double y; // 위도, latitude  ex : 127.0831787
    private String university; // 대학교
    private String subway; // 지하철역
    private String durationTime; // 걸리는 시간
    private String distance; // 거리


}
