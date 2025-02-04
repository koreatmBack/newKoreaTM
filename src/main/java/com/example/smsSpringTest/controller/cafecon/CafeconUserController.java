package com.example.smsSpringTest.controller.cafecon;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.cafecon.CafeUser;
import com.example.smsSpringTest.model.cafecon.PointLog;
import com.example.smsSpringTest.model.jobsite.Cert;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.cafecon.CafeconResponse;
import com.example.smsSpringTest.model.response.cafecon.CouponResponse;
import com.example.smsSpringTest.service.CustomUserDetailsService;
import com.example.smsSpringTest.service.SmsService;
import com.example.smsSpringTest.service.cafecon.CafeconUserService;
import com.example.smsSpringTest.service.jobsite.jobsite_userService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 User controller
 */
@RestController
@RequestMapping({"/api/v1/cafecon/user", "/v1/cafecon/user"})
@Slf4j
@RequiredArgsConstructor
public class CafeconUserController {

    private final jobsite_userService jobsiteUserService;
    private final CafeconUserService cafeconUserService;
    private final SmsService smsService;
    private final CustomUserDetailsService customUserDetailsService;

    // 카페콘 연락처 본인인증 (문자 전송)
    @PostMapping("/cert/sms")
    public ApiResponse certificateSMS(@RequestBody Cert cert) throws IOException {
        cert.setUserName(cert.getManagerName());
        return smsService.certificateSMS(cert);
    }

    // 이미 가입된 연락처인지 체킹
    @PostMapping("/dupCheck/phone")
    public ApiResponse dupCheckPhone(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.dupCheckPhone(user);
    }

    // 문자 본인인증 코드 일치하는지 확인
    @PostMapping("/cert/code")
    @Operation(summary = "문자 본인인증 코드 확인", description="문자 본인인증 코드가 일치하는지 확인합니다.")
    public ApiResponse cert(@RequestBody Cert cert) throws Exception{
        cert.setUserName(cert.getManagerName());
        return jobsiteUserService.cert(cert);
    }

    // 카페콘 회원가입
    @PostMapping("/join")
    public ApiResponse cafeconSignUp(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.cafeconSignUp(user);
    }

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인 API
    @PostMapping("/check/id")
    public ApiResponse checkId(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.checkId(user);
    }

    // 카페콘 로그인
    @PostMapping("/login")
    public CafeconResponse cafeconLogin(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.cafeconLogin(user);
    }

    // 카페콘 로그아웃
    @PostMapping("/logout")
    public ApiResponse cafeconLogout() throws Exception {
        return cafeconUserService.cafeconLogout();
    }

    // 카페콘 회원 탈퇴
    @PutMapping("/delete")
    public ApiResponse deleteUser(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.deleteUser(user);
    }

    // DB에 저장된 비밀번호와 일치하는지 체크
    @PostMapping("/check/pwd")
    public ApiResponse checkUserPwd(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.checkUserPwd(user);
    }

    // 카페콘 비밀번호 변경
    @PutMapping("/change/pwd")
    public ApiResponse changePwd(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.changePwd(user);
    }

    // 카페콘 회원 정보 수정
    @PutMapping("/edit")
    public CafeconResponse cafeconUserUpdate(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.cafeconUserUpdate(user);
    }

    // 카페콘 회원 포인트 수정 ( 관리자가 지급 및 차감하는 것 )
    @PutMapping("/update/point")
    public ApiResponse updatePoint(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.updatePoint(user);
    }

    // 카페콘 회원 한 명 정보 조회
    @PostMapping("/find/one")
    public CafeconResponse findOneCafUser(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findOneCafUser(user);
    }

    // 카페콘 전체 회원 목록 반환
    @PostMapping("/find/all")
    public CafeconResponse findAllCafUser(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findAllCafUser(user);
    }

    // 카페콘 권한 변경
    @PutMapping("/change/role")
    public ApiResponse updateRole(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.updateRole(user);
    }

    // 회원의 쿠폰(기프티콘) 구매 내역 조회
    @PostMapping("/find/couponList")
    public CouponResponse userCouponList(@RequestBody Paging paging) throws Exception {
        return cafeconUserService.userCouponList(paging);
    }

    // 회원의 포인트 내역 조회
    @PostMapping("/find/pointLogList")
    public CafeconResponse userPointLog(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.userPointLog(user);
    }

    // 회원 한 명의 충전 내역 조회
    @PostMapping("/find/chargeList")
    public CafeconResponse userChargeList(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.userChargeList(user);
    }

    // 아이디 찾기 -> 이름, 연락처 입력 후 인증 버튼 클릭시 가입된 아이디인지 확인하기
    @PostMapping("/find/id/before/cert")
    public ApiResponse findCafUserIdBeforeCert(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findCafUserIdBeforeCert(user);
    }

    // 인증 완료 후 ID, 가입일 조회 (managerName, phone 필수)
    @PostMapping("/find/id")
    public CafeconResponse findCafUserId(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findCafUserId(user);
    }

    // 비밀번호 찾기 -> 아이디 , 이름 , 연락처 입력 후 인증버튼 클릭시 가입된 계정인지 확인
    @PostMapping("/find/pwd/before/cert")
    public ApiResponse findCafUserPwdBeforeCert(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findCafUserPwdBeforeCert(user);
    }

    // 시작일 ~ 종료일 사이에서 log_type이 CP 총 개수와 총 금액 리턴하기
    @PostMapping("/find/cpLogList")
    public CafeconResponse findCpLogList(@RequestBody CafeUser user) throws Exception {
        return cafeconUserService.findCpLogList(user);
    }

    // 시작일 ~ 종료일 사이에서 log_type 별로 일마다 포인트 합산 후 type별로 리턴
    @PostMapping("/find/all/pointLog")
    public CafeconResponse findAllPointLog(@RequestBody PointLog pointLog) throws Exception {
        return cafeconUserService.findAllPointLog(pointLog);
    }

}
