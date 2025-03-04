package com.example.smsSpringTest.model.response.formmail;

import com.example.smsSpringTest.model.formmail_vo.Insurance;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-03-04
 * comment : 보험 정보 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceResponse extends ApiResponse {

    private List<Insurance> insuranceList;

}
