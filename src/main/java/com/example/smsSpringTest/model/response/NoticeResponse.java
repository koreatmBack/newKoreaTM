package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.jobsite.Notice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-12-16
 * comment : 공지사항,FAQ 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeResponse extends ApiResponse{

    private List<Notice> noticeList;
    private Notice findOneNotice;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;

}
