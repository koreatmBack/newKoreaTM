package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * author : 신기훈
 * date : 2024-12-16
 * comment : 공지사항, FAQ vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notice {

    private int num; // 글 번호
    private String title; // 글 제목
    private String content; // 글 내용
    private String type; // 글 타입 (B01 = 공지사항 , B02 = FAQ)
    private LocalDateTime created; // 글 등록일
    private LocalDateTime updated; // 글 수정일

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int page;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int size;

    private int offset;


}
