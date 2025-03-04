package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-02-18
 * comment : 인터뷰 메모 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewMemo {

    private String mid;
    private String applyId;
    private String content;
    private String regDate;
    private String managerId;
    private String managerName;

//    private String rName; // 요청받을 값
//    private String userId; // 요청받을 값

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int offset;
}
