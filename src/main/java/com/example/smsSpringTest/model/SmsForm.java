package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : sms 전송 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsForm {
    private String smsType;
    private String subject;
    private String msg;
    @JsonProperty("rPhone")
    private String rPhone;

    private String sPhone;

    @JsonProperty("sPhone1")
    private String sPhone1;
    @JsonProperty("sPhone2")
    private String sPhone2;
    @JsonProperty("sPhone3")
    private String sPhone3;
    private String rDate;
    private String rTime;
    private String testFlag;

    private LocalDateTime date;
}
