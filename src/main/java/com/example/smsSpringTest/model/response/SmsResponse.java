package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.SmsForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : sms 전송 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsResponse extends ApiResponse{

    private SmsForm smsForm;
    private String count; // 문자 남은 개수 ?
    private List<SmsForm> smsList;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;

}
