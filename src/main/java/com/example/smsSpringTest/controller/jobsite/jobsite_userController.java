package com.example.smsSpringTest.controller.jobsite;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.jobsite.CertSMS;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.JobUserResponse;
import com.example.smsSpringTest.model.response.jobsite.SocialResponse;
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

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인 API
    @PostMapping("/check/id")
    public ApiResponse checkId(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.checkId(user);
    }

    // 회원 id 일치할때 즐겨찾기 삭제
    @PutMapping("/delete/favorite")
    public ApiResponse deleteFavorite(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.deleteFavorite(user);
    }

    // 회원 id 일치할때 스크랩 삭제
    @PutMapping("/delete/clipping")
    public ApiResponse deleteClipping(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.deleteClipping(user);
    }

    // 회원 id 일치할때 즐겨찾기 목록 조회
    @PostMapping("/find/favorite")
    public JobUserResponse findFavorite(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findFavorite(user);
    }

    // 회원 id 일치할때 스크랩 목록 조회
    @PostMapping("/find/clipping")
    public JobUserResponse findClipping(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findClipping(user);
    }


    // -- kakao 로그인 테스트 위한 코드들
    @GetMapping("/login/kakao")
    public SocialResponse kakaoLogin(@RequestParam String code) throws Exception{
//        SocialResponse socialResponse = new SocialResponse();
        log.info("code = " + code);

        return jobsiteUserService.kakaoLogin(code);
    }

    // 로그인 회원 kakao 소셜 로그인 연동
    @GetMapping("/kakao/integ")
    public ApiResponse userIntegKakao(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegKakao(code);
    }

    // -------- 카카오 끝

    // NAVER 시작

    // NAVER 소셜 로그인
    @GetMapping("/login/naver")
    public SocialResponse naverLogin(@RequestParam String code) throws Exception{
//        SocialResponse socialResponse = new SocialResponse();
        log.info("code = " + code);

        return jobsiteUserService.naverLogin(code);
    }

    // 로그인 회원 naver 소셜 로그인 연동
    @GetMapping("/naver/integ")
    public ApiResponse userIntegNaver(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegNaver(code);
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ NAVER 끝 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // ----- Google 시작
    @GetMapping("/login/google")
    public SocialResponse googleLogin(@RequestParam String code) throws Exception {
        log.info("code = " + code);
        return jobsiteUserService.googleLogin(code);
    }

    // 로그인 회원 google 소셜 로그인 연동
    @GetMapping("/google/integ")
    public ApiResponse userIntegGoogle(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegGoogle(code);
    }

    // ----- facebook 시작
    @GetMapping("/login/facebook")
    public SocialResponse facebookLogin(@RequestParam String code) throws Exception {
        log.info("code = " + code);
        return jobsiteUserService.facebookLogin(code);
    }

    // 로그인 회원 google 소셜 로그인 연동
    @GetMapping("/facebook/integ")
    public ApiResponse userIntegFacebook(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegFacebook(code);
    }
}
