package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.entity.formMail_company;
import com.example.smsSpringTest.model.findCompany;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-23
 * comment : 고객사 응답 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse extends ApiResponse{

    private formMail_company formMailCompany;
    private findCompany company;
    private List<findCompany> companyList;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;
}
