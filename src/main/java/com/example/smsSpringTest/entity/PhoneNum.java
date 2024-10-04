package com.example.smsSpringTest.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-09-23
 * comment : 업무용 연락처 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneNum {

//    @JsonProperty("phone_number")
    private String phoneNumber;

//    @JsonProperty("created_at")
    private String createDate;

}
