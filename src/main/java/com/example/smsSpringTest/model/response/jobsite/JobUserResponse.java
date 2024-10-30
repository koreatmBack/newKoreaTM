package com.example.smsSpringTest.model.response.jobsite;

import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : jobsite 회원 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobUserResponse extends ApiResponse {

    private JobsiteUser user;

    private List<JobsiteUser> jobsiteUserList;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;
}
