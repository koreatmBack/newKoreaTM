package com.example.smsSpringTest.model.formmail_vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-03-04
 * comment : 보험 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Insurance {
//    private int no;
    private String type; // 보험 타입
    private String name; // 보험명

    private List<String> names; //보험명들
    private String searchKeyword; // 검색어
}
