package com.example.smsSpringTest.controller.jobsite;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.jobsite.CertSMS;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.JobUserResponse;
import com.example.smsSpringTest.service.jobsite.jobsite_userService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : jobsite 회원 controller
 */
@RestController
@RequestMapping({"/api/v1/jobsite/user", "/v1/jobsite/user"})
@Slf4j
@RequiredArgsConstructor
public class jobsite_userController {

    private final jobsite_userService jobsiteUserService;


    // 본인인증 코드 일치하는지 확인
    @PostMapping("/cert")
    public ApiResponse cert(@RequestBody CertSMS certSMS) throws Exception{
        return jobsiteUserService.cert(certSMS);
    }

    // jobsite 회원가입
    @PostMapping("/join")
    public ApiResponse jobSignUp(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobSignUp(user);
    }

    // jobsite 로그인
    @PostMapping("/login")
    public JobUserResponse jobLogin(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobLogin(user);
    }

    // jobsite 회원 로그아웃
    @PostMapping("/logout")
    public ApiResponse jobLogout() throws Exception {
        return jobsiteUserService.jobLogout();
    }

    // jobsite 회원 정보 수정
    @PutMapping("/update")
    public JobUserResponse jobUserUpdate(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobUserUpdate(user);
    }

    // jobsite 회원 한 명 정보 반환
    @PostMapping("/findOne")
    public JobUserResponse findOneJobUser(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findOneJobUser(user);
    }

    // jobsite 전체 회원 목록 반환
    @PostMapping("/findAll")
    public JobUserResponse findAllJobUser(@RequestBody Paging paging) throws Exception {
        return jobsiteUserService.findAllJobUser(paging);
    }
}
