package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.common.RefToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-22
 * comment : refreshToken Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefResponse extends ApiResponse {

    private RefToken refToken;

}