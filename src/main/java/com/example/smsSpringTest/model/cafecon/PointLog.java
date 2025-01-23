package com.example.smsSpringTest.model.cafecon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2025-01-16
 * comment : 회원 포인트 지급/차감 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointLog {

    private String userId;
    private int point;
    private int currPoint;
    private String gubun;
    private String goodsName;
    private String discountPrice;
    private String logType;
    private String orderNo;
    private String regDate;
    private String uptDate;
    private String useStatus;
    private String startDate;
    private String endDate;
    private int page;
    private int size;
    private int offset;

    // 조인으로 뽑기 위함
    private String trId;
    private String memo;
    private String phone;
    private String searchType;
}