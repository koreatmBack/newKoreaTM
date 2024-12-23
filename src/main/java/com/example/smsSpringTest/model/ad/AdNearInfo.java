package com.example.smsSpringTest.model.ad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-12-09
 * comment : 공고 주변 정보들 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdNearInfo {

    private String aid;
    private String nearStation;
    private String distance;
    private String durationTime;
    private String line;
    private String status; // Y or N

    private String type; // 선택인지 선택해제인지

}
