package com.example.smsSpringTest.model.response.cafecon;

import com.example.smsSpringTest.model.cafecon.*;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CafeconResponse extends ApiResponse {

    private CafeUser user;
    private String userId;
    private String createdAt;
    private List<CafeUser> cafeconUserList;
    private List<Deposit> depositList;
    private List<PointLog> pointLogList;
    private List<LogResult> pointList;
    private TotalResult totalResult;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalCount;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPoint;

}
