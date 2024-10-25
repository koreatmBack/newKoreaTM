package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.entity.UserProfile;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.User;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.RefResponse;
import com.example.smsSpringTest.model.response.UserResponse;
import com.example.smsSpringTest.service.formMail_adminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
    public ApiResponse signUp(@Valid @RequestBody UserProfile user) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        apiResponse = formMailAdminService.signUp(user);

        return apiResponse;
    }

    // 쿠키에 값 있으면 자동 로그인


    // 쿠키 만료시간 보내주기
    @GetMapping("/exper_cookie")
    public ApiResponse exper_cookie() throws Exception {
        return formMailAdminService.exper_cookie();
    }

    // 회원 로그인
    @PostMapping("/login")
    public UserResponse login(@RequestBody UserProfile user) throws Exception {

        UserResponse userResponse = new UserResponse();

//        log.info("controller 유저프로필 = " + user);
        userResponse = formMailAdminService.logIn(user);

        return userResponse;
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

    // access토큰 만료됐을때, 토큰 재발급 요청 -> 만료시, 로그인하면 자동 재생성되긴함
    @PostMapping("/reissu/token")
    public RefResponse reissuToken(@RequestBody RefToken refToken) throws Exception {
        return formMailAdminService.reissuToken(refToken);
    }

    // 회원 목록
    @PostMapping("/userList")
    public UserResponse userList(@RequestBody Paging paging) throws Exception{

        UserResponse userResponse = new UserResponse();

        userResponse = formMailAdminService.userList(paging);

        return userResponse;
    }

    // 회원 한명 정보 반환
    @PostMapping("/findOneUser")
    public UserResponse findOneUser(@RequestBody UserProfile user) throws Exception{

        UserResponse userResponse = new UserResponse();

        userResponse = formMailAdminService.findOneUser(user);

        return userResponse;
    }

    // 회원 이름 검색시 해당 회원들 정보 반환
    @PostMapping("/findUsers")
    public UserResponse findUsers(@RequestBody Map<String, String> searchKeyword) throws Exception {
        UserResponse userResponse = new UserResponse();
        String userName = searchKeyword.get("searchKeyword");
        userResponse = formMailAdminService.findUsers(userName);

        return userResponse;
    }

    // 회원 정보 수정
    @PutMapping("/updateUser")
    public UserResponse updateUser(@RequestBody UserProfile user) throws Exception{

        UserResponse userResponse = new UserResponse();
        userResponse = formMailAdminService.updateUser(user);
        return userResponse;
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
    public UserResponse allPhoneNumList(){
        UserResponse userResponse = new UserResponse();

        userResponse = formMailAdminService.allPhoneNumList();

        return userResponse;
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
    public UserResponse findUserName(@RequestBody User user) throws Exception{

        UserResponse userResponse = new UserResponse();
        String phoneNumber = user.getMPhone();

        userResponse = formMailAdminService.findUserName(phoneNumber);

        return userResponse;
    }

}
