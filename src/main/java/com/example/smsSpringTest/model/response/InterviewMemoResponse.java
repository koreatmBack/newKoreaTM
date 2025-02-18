package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.InterviewMemo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-02-18
 * comment : 면접 메모 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewMemoResponse extends ApiResponse{

    private List<InterviewMemo> interviewMemoList;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

}
