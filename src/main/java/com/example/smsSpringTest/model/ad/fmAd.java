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
import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class fmAd {

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

//    private String workTime; // 근무 시간

    private String sido;

    private String sigungu;

    private String dongEubMyun; // 동/읍/면

    private Integer grade;

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

    private List<AdNearInfo> nearInfoList; // 주변 정보들 담을 것

    private String status; // 전체, 진행중, 대기, 종료

    private String searchType; // 공고제목, 담당자명, 공고번호, 연락처

    private String keyword; // 검색시 입력한 값

    // 12 - 19 ~
    private String managerName; // 담당자 이름

    private String managerEmail;    // 담당자 이메일

    private String managerPhone;    // 담당자 메인 연락처

    private String managerSubPhone; // 담당자 서브 연락처

    private boolean probation;  // 기간협의

    private boolean periodDiscussion; // 수습 기간

    private boolean workDate;   // 근무요일 체크

    private String workDateDetail; // 요일 참고

    private boolean workTime; // 근무시간 체크

    private String workTimeDetail; // 시간참고

    private boolean ageType; // 연령조건

    private String minAge; // 최소연령

    private String maxAge; // 최고연령

    private boolean endLimit; // 채용 마감 체크

    private String applyUrl; // 지원 URL

    private String zipCode; // 우편번호

    private String addressDetail; // 상세주소(102동 403호 등)

    private String photoList;   // 사진목록

    private boolean adLink; // 광고 이미지 링크

    private String sido2; // 시 / 도 2

    private String sigungu2; // 시 / 군 / 구 2

    private String dongEubMyun2; // 동 / 읍 / 면 2

    private String sido3; // 시 / 도 3

    private String sigungu3; // 시 / 군 / 구 3

    private String dongEubMyun3; // 동 / 읍 / 면 3

    private boolean focus; // 유료광고2

    private LocalDate created; // 등록일

    private LocalDate updated; // 수정일

    private String detailContent; // 상세 내용

    private boolean phoneShow; // 메인 폰 공개 or 비공개

    private boolean subPhoneShow; // 서브 폰 공개 or 비공개

    private String detailImages;

    private String companyUserId; // 기업 회원 아이디

    private int num; // 광고 고유 번호

    private int viewCount; // 조회수
}
