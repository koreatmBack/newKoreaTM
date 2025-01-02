package com.example.smsSpringTest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2025-01-02
 * comment : 광고 개수 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdCountResponse extends ApiResponse{

    private int totalAds;

    private int activeAds;

    private int waitAds;

    private int closeAds;

}
