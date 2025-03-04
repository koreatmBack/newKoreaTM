package com.example.smsSpringTest.model.response.formmail;

import com.example.smsSpringTest.model.formmail_vo.Apply;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 지원자 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyResponse extends ApiResponse {

    private List<Apply> applyList;

    private String survey;
    private String formNo;

    @JsonProperty("rName")
    private String rName;

    private String userName;
    private String rank;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;

}
