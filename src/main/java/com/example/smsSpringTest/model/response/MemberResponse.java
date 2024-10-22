package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : 회원 조회 결과값 리턴
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse extends ApiResponse{

    private Member member;

}
