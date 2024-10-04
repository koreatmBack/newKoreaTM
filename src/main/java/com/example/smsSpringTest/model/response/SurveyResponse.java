package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.Survey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : 설문조사 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResponse extends ApiResponse{

    private List<Survey> surveyList;
    private int survId;

}
