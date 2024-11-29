package com.example.smsSpringTest.model.ad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-11-29
 * comment : 지역 검색용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionRequest {
    private String sido;        // 시/도
    private String sigungu;     // 시/군/구
    private String dongEubMyun; // 동/읍/면

}
