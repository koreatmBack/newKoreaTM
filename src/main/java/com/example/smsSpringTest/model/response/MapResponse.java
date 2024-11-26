package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.MapVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

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
public class MapResponse extends ApiResponse{
    private String x;   // 경도 longitude
    private String y;   // 위도 latitutde
    private List<MapVO> mapInfoList;
    private String university;  // 대학교
}
