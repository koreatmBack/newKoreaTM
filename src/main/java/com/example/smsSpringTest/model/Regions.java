package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-12-23
 * comment : 지역 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Regions {

    private String sido;
    private String sigungu;
    private String dongEubMyun;
}
