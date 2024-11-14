package com.example.smsSpringTest.controller.jobsite;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.jobsite.CertSMS;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.JobUserResponse;
import com.example.smsSpringTest.model.response.jobsite.SocialResponse;
import com.example.smsSpringTest.service.jobsite.jobsite_userService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "본인인증 코드 확인", description="본인인증 코드가 일치하는지 확인합니다.")
    public ApiResponse cert(@RequestBody CertSMS certSMS) throws Exception{
        return jobsiteUserService.cert(certSMS);
    }

    // 본인 인증 후 넘겨받은 연락처로 Id, 가입일 찾기
    @PostMapping("/find/id")
    @Operation(summary = "Id, 가입일을 조회합니다.", description="본인인증 성공 후 넘겨받은 연락처로 id, 가입일 조회합니다.")
    public JobUserResponse findJobUserId(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findJobUserId(user);
    }

    // 연락처로 비밀번호 찾기 눌렀을때 본인인증 보내기 전 실행 API (userId, userName, phone 필요)
    @PostMapping("/find/pwd")
    @Operation(summary = "비밀번호 찾기 눌렀을 때 계정이 존재하는지 확인", description="비밀번호 찾기 눌렀을 때 본인인증 보내기 전 실행하는 API로 userId, userName, phone 필수값")
    public ApiResponse findJobUserPwd(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findJobUserPwd(user);
    }

    // 본인인증 성공시 새로운 비밀번호 입력 받은 후 DB에 암호화하여 저장
    @PutMapping("/update/pwd")
    @Operation(summary = "새로운 비밀번호 등록", description="본인인증 성공시 새로운 비밀번호 입력받은 후 저장")
    public ApiResponse updateNewPwd(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.updateNewPwd(user);
    }

    // jobsite 회원가입
    @Operation(summary = "회원 가입", description="")
    @PostMapping("/join")
    public ApiResponse jobSignUp(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobSignUp(user);
    }

    // jobsite 로그인
    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description="")
    public JobUserResponse jobLogin(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobLogin(user);
    }

    // jobsite 회원 로그아웃
    @PostMapping("/logout")
    @Operation(summary = "회원 로그아웃", description="")
    public ApiResponse jobLogout() throws Exception {
        return jobsiteUserService.jobLogout();
    }

    // jobsite 회원 정보 수정
    @PutMapping("/update")
    @Operation(summary = "회원 정보 수정", description="")
    public JobUserResponse jobUserUpdate(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobUserUpdate(user);
    }

    // jobsite 회원 한 명 정보 반환
    @PostMapping("/findOne")
    @Operation(summary = "회원 한 명 정보 조회", description="필수 값 : userId")
    public JobUserResponse findOneJobUser(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findOneJobUser(user);
    }

    // jobsite 전체 회원 목록 반환
    @PostMapping("/findAll")
    @Operation(summary = "회원 전체 목록 조회", description="페이징 처리 , 필수 값 : page, size")
    public JobUserResponse findAllJobUser(@RequestBody Paging paging) throws Exception {
        return jobsiteUserService.findAllJobUser(paging);
    }

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인 API
    @PostMapping("/check/id")
    @Operation(summary = "회원 가입시 id 중복 체크", description="id 입력칸 옆에 중복 확인 API")
    public ApiResponse checkId(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.checkId(user);
    }

    // 회원 id 일치할때 즐겨찾기 삭제
    @PutMapping("/delete/favorite")
    @Operation(summary = "즐겨찾기 삭제", description="필수 값 : userId 일치")
    public ApiResponse deleteFavorite(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.deleteFavorite(user);
    }

    // 회원 id 일치할때 스크랩 삭제
    @PutMapping("/delete/clipping")
    @Operation(summary = "스크랩 삭제", description="필수 값 : userId 일치")
    public ApiResponse deleteClipping(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.deleteClipping(user);
    }

    // 회원 id 일치할때 즐겨찾기 목록 조회
    @PostMapping("/find/favorite")
    @Operation(summary = "즐겨찾기 목록 조회", description="필수 값 : userId 일치")
    public JobUserResponse findFavorite(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findFavorite(user);
    }

    // 회원 id 일치할때 스크랩 목록 조회
    @PostMapping("/find/clipping")
    @Operation(summary = "스크랩 목록 조회", description="필수 값 : userId 일치")
    public JobUserResponse findClipping(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findClipping(user);
    }


    // -- kakao 로그인
    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 소셜 로그인", description="")
    public SocialResponse kakaoLogin(@RequestParam String code) throws Exception{
//        SocialResponse socialResponse = new SocialResponse();
        log.info("code = " + code);

        return jobsiteUserService.kakaoLogin(code);
    }

    // 로그인 회원 kakao 소셜 로그인 연동
    @Operation(summary = "카카오 소셜 로그인 연동", description="이미 등록된 회원 -> 카카오 소셜 로그인 전환시")
    @GetMapping("/kakao/integ")
    public ApiResponse userIntegKakao(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegKakao(code);
    }

    // -------- 카카오 끝

    // NAVER 시작

    // NAVER 소셜 로그인
    @GetMapping("/login/naver")
    @Operation(summary = "네이버 소셜 로그인", description="")
    public SocialResponse naverLogin(@RequestParam String code) throws Exception{
//        SocialResponse socialResponse = new SocialResponse();
        log.info("code = " + code);

        return jobsiteUserService.naverLogin(code);
    }

    // 로그인 회원 naver 소셜 로그인 연동
    @GetMapping("/naver/integ")
    @Operation(summary = "네이버 소셜 로그인 연동", description="이미 등록된 회원 -> 네이버 소셜 로그인 전환시")
    public ApiResponse userIntegNaver(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegNaver(code);
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ NAVER 끝 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // ----- Google 시작
    @GetMapping("/login/google")
    @Operation(summary = "구글 소셜 로그인", description="")
    public SocialResponse googleLogin(@RequestParam String code) throws Exception {
        log.info("code = " + code);
        return jobsiteUserService.googleLogin(code);
    }

    // 로그인 회원 google 소셜 로그인 연동
    @GetMapping("/google/integ")
    @Operation(summary = "구글 소셜 로그인 연동", description="이미 등록된 회원 -> 구글 소셜 로그인 전환시")
    public ApiResponse userIntegGoogle(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegGoogle(code);
    }

    // ----- facebook 시작
    @GetMapping("/login/facebook")
    @Operation(summary = "페이스북 소셜 로그인", description="")
    public SocialResponse facebookLogin(@RequestParam String code) throws Exception {
        log.info("code = " + code);
        return jobsiteUserService.facebookLogin(code);
    }

    // 로그인 회원 google 소셜 로그인 연동
    @GetMapping("/facebook/integ")
    @Operation(summary = "페이스북 소셜 로그인", description="이미 등록된 회원 -> 페이스북 소셜 로그인 전환시")
    public ApiResponse userIntegFacebook(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegFacebook(code);
    }
}
