package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : 설문 응답 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurvRes {

    private int resNo;
    private int qustNo;
    private int optNo;
    private List<Integer> optIds; // 중복체크 가능한 체크박스 담을 array
    private String resCont;
    private LocalDateTime resDate;

}
