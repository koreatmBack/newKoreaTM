package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.SmsForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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

}
