package com.example.smsSpringTest.controller.jobsite;

import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.jobsite.BookMark;
import com.example.smsSpringTest.model.jobsite.Cert;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.jobsite.RecentView;
import com.example.smsSpringTest.model.response.AdResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.BookMarkResponse;
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


    // 문자 본인인증 코드 일치하는지 확인
    @PostMapping("/cert")
    @Operation(summary = "문자 본인인증 코드 확인", description="문자 본인인증 코드가 일치하는지 확인합니다.")
    public ApiResponse cert(@RequestBody Cert cert) throws Exception{
        return jobsiteUserService.cert(cert);
    }

    // 이메일 본인인증 코드 일치하는지 확인
    @PostMapping("/cert/email")
    @Operation(summary = "이메일 본인인증 코드 확인", description="이메일 본인인증 코드가 일치하는지 확인합니다.")
    public ApiResponse certEmail(@RequestBody Cert cert) throws Exception {
        return jobsiteUserService.certEmail(cert);
    }

//    // 연락처 본인 인증 후 넘겨받은 이름과 연락처로 Id, 가입일 찾기
//    @PostMapping("/find/id")
//    @Operation(summary = "Id, 가입일을 조회합니다.", description="본인인증 성공 후 넘겨받은 연락처로 id, 가입일 조회합니다.")
//    public JobUserResponse findJobUserIdFromPhone(@RequestBody JobsiteUser user) throws Exception {
//        return jobsiteUserService.findJobUserIdFromPhone(user);
//    }

    // 본인 인증 후 넘겨받은 이름과 이메일 or 연락처로 Id, 가입일 찾기
    @PostMapping("/find/id")
    @Operation(summary = "Id, 가입일을 조회합니다.", description="본인인증 성공 후 넘겨받은 이메일로 id, 가입일 조회합니다.")
    public JobUserResponse findJobUserId(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findJobUserId(user);
    }

    // 아이디 찾기 눌렀을때 가입된 아이디인지 확인하기 (userName, phone or email 필수)
    @PostMapping("/find/id/before/cert")
    public ApiResponse findJobUserIdBeforeCert(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findJobUserIdBeforeCert(user);
    }

    // 비밀번호 찾기 눌렀을때 본인인증 보내기 전 실행 API (userId, userName, phone or email 필요)
    @PostMapping("/find/pwd")
    @Operation(summary = "비밀번호 찾기 눌렀을 때 계정이 존재하는지 확인", description="비밀번호 찾기 눌렀을 때 본인인증 보내기 전 실행하는 API로 userId, userName, phone 필수값")
    public ApiResponse findJobUserPwd(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findJobUserPwd(user);
    }

//    // 이메일로 비밀번호 찾기 눌렀을 때 본인인증 보내기 전 실행 API (email)
//    @PostMapping("/find/pwd/fromEmail")
//    @Operation(summary = "아이디 찾기 or 비밀번호 찾기 눌렀을 때 계정이 존재하는지 확인", description="아이디 찾기 or 비밀번호 찾기 눌렀을 때 본인인증 보내기 전 실행하는 API로 userName, email 필수값")
//    public ApiResponse findJobUserPwdFromEmail(@RequestBody JobsiteUser user) throws Exception {
//        return jobsiteUserService.findJobUserPwdFromEmail(user);
//    }


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

    // jobsite 회원 탈퇴 ( userId, userPwd 일치해야함 )
    @DeleteMapping("/resign")
    public ApiResponse jobResign(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.jobResign(user);
    }

    // 비밀번호 변경창 누를때, 비동기로 먼저 naver 로그인인지 체크할 API
    @PostMapping("/check/social")
    public ApiResponse checkSocialUser(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.checkSocialUser(user);
    }

    // 회원 정보 수정 -> 비밀번호 변경하기 (userId, 기존 pwd, 새로운 pwd)
    @PutMapping("/change/pwd")
    public ApiResponse changePwd(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.changePwd(user);
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
    @Operation(summary = "회원 가입시 id 중복 체크", description="비동기로 id 중복 확인 API")
    public ApiResponse checkId(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.checkId(user);
    }

    // 회원가입시 email 중복 확인하는 API
    @PostMapping("/check/email")
    @Operation(summary = "회원 가입시 email 중복 체크", description="비동기로 email 중복 확인 API")
    public ApiResponse checkEmail(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.checkEmail(user);
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
    @PostMapping("/find/scrape")
    @Operation(summary = "스크랩 목록 조회", description="필수 값 : userId 일치")
    public JobUserResponse findScrape(@RequestBody JobsiteUser user) throws Exception {
        return jobsiteUserService.findScrape(user);
    }

// ------------------------------------------------------

//    // 소셜 로그인 성공시
//    // user객체 반환 , 쿠키 생성
//    @GetMapping("/login/social/success")
//    public SocialResponse socialSuccess(@RequestParam String userId) throws Exception {
//        log.info("소셜 성공시 컨트롤러 호출됨 ? ");
//        return jobsiteUserService.socialSuccess(userId);
//    }

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

    // 로그인 회원 facebook 소셜 로그인 연동
    @GetMapping("/facebook/integ")
    @Operation(summary = "페이스북 소셜 로그인", description="이미 등록된 회원 -> 페이스북 소셜 로그인 전환시")
    public ApiResponse userIntegFacebook(@RequestParam String code) throws Exception {

        return jobsiteUserService.userIntegFacebook(code);
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 스크랩, 좋아요 관련 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 스크랩 or 좋아요 추가시 이미 등록된 것이 있는지 체크하는 API
    @PostMapping("/dup/bookmark/check")
    public ApiResponse dupBookmarkCheck(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.dupBookmarkCheck(mark);
    }

    // 스크랩 or 좋아요 추가
    @PostMapping("/add/bookmark")
    public ApiResponse addBookmark(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.addBookmark(mark);
    }

    // userId , type 일치할 때 스크랩 or 좋아요 전체 조회
    @PostMapping("/find/bookmarkList")
    public BookMarkResponse bookMarkList(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.bookMarkList(mark);
    }

    // userId , type 일치할 때 스크랩 or 좋아요 진행중인 공고 조회
    @PostMapping("/find/bookmarkList/progress")
    public BookMarkResponse progressBookMarkList(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.progressBookMarkList(mark);
    }

    // userId, type, aid 일치할 때 하나 삭제하기
    @DeleteMapping("/delete/bookmark/one")
    public ApiResponse deleteOneBookmark(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.deleteOneBookmark(mark);
    }

    // userId, type, aid 일치할 때 하나 삭제하기
    @DeleteMapping("/delete/bookmark/all")
    public ApiResponse deleteAllBookmark(@RequestBody BookMark mark) throws Exception {
        return jobsiteUserService.deleteAllBookmark(mark);
    }

    // 최근 열람 공고 조회하기
    @PostMapping("/recent/views")
    public AdResponse recentViews(@RequestBody RecentView rv) throws Exception {
        return jobsiteUserService.recentViews(rv);
    }
}
