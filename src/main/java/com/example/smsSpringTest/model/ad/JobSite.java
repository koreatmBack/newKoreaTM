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
 * date : 2024-10-11
 * comment : 잡사이트용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobSite {

    private String aid;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    private Integer extensionDay;

    private int totalDay;

    private String heaven;

    private String albamon;

    private String telejob;

    private String adTypeM; // 유료 광고 타입 - 알바몬

    private String adTypeH; // 유료 광고 타입 - 알바천국

    private String adImg; // 광고 이미지 url

    private String logoImg; // 로고 이미지 url

    private String concept; // 컨셉

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

    private String welfare; // 복지 혜택 (체크박스로 선택)

    private String welfare2; // 기타 복지 혜택 (체크 박스에 없는 내용)

    private String experience; // 경력 채용 여부

    private String adNum; // 광고 번호 4자리

    private String workTime; // 근무 시간

    private int grade;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;

    private int offset;

    private String sido;

    private String sigungu;

    private String dongEubMyun; // 동/읍/면

    private String hashtag;

    private String salary; // 급여

    private String salaryType; // 급여 형태(시급, 주급, 일급, 월급, 연봉)

    private String jobType; // 업직종

    private String employmentType; // 고용형태

    private String recruitCount; // 모집인원

    private String workPeriod; // 근무기간

    private String workDays; // 근무요일

    private String gender; // 성별

    private String age; // 나이

    private String education; // 학력

    private String preConditions; // 우대조건

    private String etcConditions; // 기타조건

    private String applyMethod; // 지원방법

    private String nearUniversity; // 주변 대학교

    private double x; // 경도

    private double y; // 위도
}
