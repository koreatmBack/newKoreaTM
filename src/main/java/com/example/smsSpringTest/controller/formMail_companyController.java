package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.entity.formMail_company;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.CompanyResponse;
import com.example.smsSpringTest.service.formMail_companyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-09-23
 * comment : 고객사 controller
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/formMail_company")
public class formMail_companyController {

    private final formMail_companyService formMailCompanyService;

    // 고객사 등록
    @PostMapping("/add")
    public ApiResponse addCompany(@RequestBody formMail_company comp) throws Exception{

        ApiResponse apiResponse = new ApiResponse();

        apiResponse = formMailCompanyService.addCompany(comp);

        return apiResponse;
    }

    // 전체 고객사 조회
    @PostMapping("/companyList")
    public CompanyResponse companyList(@RequestBody Paging paging) throws Exception {

        CompanyResponse companyResponse = new CompanyResponse();

        companyResponse = formMailCompanyService.companyList(paging);

        return companyResponse;
    }

    // 고객사 수정
    @PutMapping("/updateCompany")
    public ApiResponse updateCompany(@RequestBody formMail_company comp) throws Exception{

        ApiResponse apiResponse = new ApiResponse();

        apiResponse = formMailCompanyService.updateCompany(comp);

        return apiResponse;
    }

    // 고객사 삭제
    @DeleteMapping("/deleteCompany")
    public ApiResponse deleteCompany(@RequestBody formMail_company comp) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        apiResponse = formMailCompanyService.deleteCompany(comp);

        return apiResponse;
    }

    // cid 일치하는 고객사 정보 반환
    @PostMapping("/findCompany")
    public CompanyResponse findCompany(@RequestBody formMail_company comp) throws Exception {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse = formMailCompanyService.findCompany(comp);
        return companyResponse;
    }

}
