package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.entity.FormMailAdminEntity;
import com.example.smsSpringTest.model.FormMailAdmin;
import com.example.smsSpringTest.model.User;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.AdminResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.RefResponse;
import com.example.smsSpringTest.service.formMail_adminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : 회원 controller
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping({"/api/v1/formMail_admin", "/v1/formMail_admin"})
public class formMail_adminController {

    private final formMail_adminService formMailAdminService;

    // 회원 등록
    @PostMapping("/join")
    public ApiResponse signUp(@Valid @RequestBody FormMailAdmin admin) throws Exception {
        return formMailAdminService.signUp(admin);
    }

    // 쿠키에 값 있으면 자동 로그인


    // 쿠키 만료시간 보내주기
    @GetMapping("/exper_cookie")
    public AccessResponse experCookie() throws Exception {
        return formMailAdminService.experCookie();
    }

    // 회원 로그인
    @PostMapping("/login")
    public AdminResponse login(@RequestBody FormMailAdminEntity user) throws Exception {

        AdminResponse adminResponse = new AdminResponse();

//        log.info("controller 유저프로필 = " + user);
        adminResponse = formMailAdminService.logIn(user);

        return adminResponse;
    }

//    // 회원 로그아웃
//    @PostMapping("/logout")
//    public ApiResponse logOut(@RequestBody UserProfile user) throws Exception {
//
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse = formMailAdminService.logOut(user);
//        return apiResponse;
//    }

    // jwt 회원 로그아웃
    @PostMapping("/logout")
    public ApiResponse logOut() throws Exception {
        return formMailAdminService.logout();
    }

    // 리프레쉬 토큰 + 어쎄스 토큰 재발급
    @PostMapping("/reissu/RefreshToken")
    public RefResponse reissuRefreshToken(@RequestBody RefToken refToken) throws Exception {
        return formMailAdminService.reissuToken(refToken);
    }

    // access 토큰 재발급 및 쿠키 재발급
    @GetMapping("/reissu/AccessToken")
    public ApiResponse reissuAccessToken() throws Exception {
        return formMailAdminService.reissuAccessToken();
    }

    // 회원 목록
    @PostMapping("/adminList")
    public AdminResponse adminList(@RequestBody FormMailAdmin admin) throws Exception{
        return formMailAdminService.adminList(admin);
    }

    // 회원 한명 정보 반환
    @PostMapping("/findOneAdmin")
    public AdminResponse findOneAdmin(@RequestBody FormMailAdmin admin) throws Exception{
        return formMailAdminService.findOneAdmin(admin);
    }

    // 회원 이름 검색시 해당 회원들 정보 반환
    @PostMapping("/findAdmins")
    public AdminResponse findAdmins(@RequestBody FormMailAdmin admin) throws Exception {
        return formMailAdminService.findAdmins(admin);
    }

    // 회원 정보 수정
    @PutMapping("/updateAdmin")
    public AdminResponse updateAdmin(@RequestBody FormMailAdmin admin) throws Exception{

        return formMailAdminService.updateAdmin(admin);
    }

    // 업무용 연락처 추가 기능
    @PostMapping("/addPhoneNum")
    public ApiResponse addPhoneNum(@RequestBody User user) throws Exception{

        String phoneNumber = user.getMPhone();
        log.info("폰번호 = " + phoneNumber);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse = formMailAdminService.addPhoneNum(phoneNumber);

        return apiResponse;
    }

    // 업무용 연락처 모두 조회하기
    @GetMapping("/allPhoneNumList")
    public AdminResponse allPhoneNumList(){
        AdminResponse adminResponse = new AdminResponse();

        adminResponse = formMailAdminService.allPhoneNumList();

        return adminResponse;
    }

    // 업무용 연락처 삭제하기
    @DeleteMapping("/delPhoneNum")
    public ApiResponse delPhoneNum(@RequestBody User user){

        ApiResponse apiResponse = new ApiResponse();

        String phoneNumber = user.getMPhone();

        apiResponse = formMailAdminService.delPhoneNum(phoneNumber);

        return apiResponse;
    }

    // 입력받은 업무용 연락처로 회원 db에서 번호 일치하는 회원 이름, 포지션 찾기
    @PostMapping("/findUserName")
    public AdminResponse findUserName(@RequestBody User user) throws Exception{

        AdminResponse adminResponse = new AdminResponse();
        String phoneNumber = user.getMPhone();

        adminResponse = formMailAdminService.findUserName(phoneNumber);

        return adminResponse;
    }

    // 폼메일 회원 삭제하기 (TOTALADMIN, ADMIN) 만 가능
    @DeleteMapping("/delete/one")
    public ApiResponse deleteOne(@RequestBody FormMailAdmin admin) throws Exception {
        return formMailAdminService.deleteOne(admin);
    }

    // "채용"팀이며 사용중(use_status = true)인 회원 목록
    @GetMapping("/find/recruitTeam")
    public AdminResponse recruitTeamList() throws Exception {
        return formMailAdminService.recruitTeamList();
    }

}
