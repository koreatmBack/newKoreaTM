package com.example.smsSpringTest.model.response.cafecon;

import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * author : 신기훈
 * date : 2025-02-04
 * comment : 전체 회원 포인트 합산값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddPointResponse extends ApiResponse {

    @JsonProperty("AP") // JSON에서 대문자로 설정
    private int AP;

    @JsonProperty("AD") // JSON에서 대문자로 설정
    private int AD;

    @JsonProperty("CP") // JSON에서 대문자로 설정
    private int CP;

    @JsonProperty("CE") // JSON에서 대문자로 설정
    private int CE;

    @JsonProperty("GI") // JSON에서 대문자로 설정
    private int GI;



}
