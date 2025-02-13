package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 지원자 조회용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Apply {

    private String applyId;

    private String aid;

    private String cid;

    private String managerId;

    private String company;

    private String partner;

    private String applyName;

    private String applyBirth;

    private String applyGender;

    private String applyAddress;

    private String applyPhone;

    private String interviewTime;

    private String adminMemo;

    private String sido;

    private String sigungu;

    private String addressDetail;

    private LocalDateTime createdAt;
}
