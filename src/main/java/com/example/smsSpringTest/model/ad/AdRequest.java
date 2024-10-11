package com.example.smsSpringTest.model.ad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 결과 담을 vo ( page, size , offset 결과 리스트에 출력되지 않기 위해 )
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRequest {

    private String aid;
    private String cid;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
@JsonSerialize(using = LocalDateSerializer.class)
@JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    private int extensionDay;

    private int totalDay;

//    private String heaven;
//    private String albamon;
//    private String telejob;
//
//    private String adTypeM;
//    private String adTypeH;
    private String adImg; // 광고 이미지 url

    private String logoImg; // 로고 이미지 url

    private String userName; // 담당 관리자명

    private String company; // 고객사명

    private String address; // 고객사 주소지

    private String title; // 광고 제목

    private String workStart; // 근무 시작 시간

    private String workEnd; // 근무 종료 시간

    private String restTime; // 휴식 시간

    private String minPay;  // 월 최소 급여

    private String maxPay;  // 월 최대 급여

    private String workDay; // 근무 요일

    private String adNum; // 광고 번호 4자리

    private String workTime; // 근무 시간

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;

    private int offset;
}
