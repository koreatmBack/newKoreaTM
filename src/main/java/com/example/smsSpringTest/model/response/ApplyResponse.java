package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.Apply;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ApplyResponse extends ApiResponse{

    private List<Apply> applyList;

    private String survey;
    private String formNo;
    private String rName;
    private String userName;
    private String rank;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;

}
