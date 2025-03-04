package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-02-14
 * comment : 지원자 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyRequest {
    private String applyStatus;
    private List<Apply> applyIds;
}
