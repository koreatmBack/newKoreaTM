package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.response.S3UploadResponse;
import com.example.smsSpringTest.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * author : 신기훈
 * date : 2024-10-02
 * comment : 공통 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/common", "/v1/common"})
@Slf4j
public class CommonController {

    private final CommonService commonService;


    // 폼메일 광고 이미지 올리기 : S3에 이미지 업로드 (DB에는 저장 x) --> 파일 1개 ver.
    @PostMapping("/upload/formMail/{folder}")
    public S3UploadResponse uploadFormMailAd(@PathVariable("folder") String folder, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        S3UploadResponse s3UploadResponse = new S3UploadResponse();

        s3UploadResponse = commonService.uploadFormMailAd(multipartFile, folder);

        return s3UploadResponse;
    }

    @GetMapping("/ip")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = commonService.getClientIp(request);
        return "Client IP: " + clientIp;
    }

//    // 회원 로그인 jwt
//    @PostMapping("/login")
//    public MemberResponse login(@RequestBody JwtUser user) throws Exception {
//
//        MemberResponse memberResponse = new MemberResponse();
//
//        memberResponse = memberService.login(user);
//        System.out.println("user = " + user);
//
//        return memberResponse;
//    }
//
//    // 회원 회원가입 jwt
//    @PostMapping("/join")
//    public ApiResponse join(@RequestBody JwtUser user) throws Exception{
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse = memberService.signUp(user);
//        return apiResponse;
//    }
//
//    // access토큰 만료됐을때, 토큰 재발급 요청
//    @PostMapping("/reissu/token")
//    public RefResponse reissuToken(@RequestBody RefToken refToken) throws Exception {
//
//        return memberService.reissuToken(refToken);
//    }

//    // jwt 로그아웃
//    @PostMapping("/logout")
//    public ApiResponse logout(@NotNull Authentication authentication) throws Exception {
//
//        JwtUser user = new JwtUser();
//        user.setUserId(authentication.getName());
//
//        return memberService.logout(user);
//    }

//    // jwt 로그아웃
//    @PostMapping("/logout")
//    public ApiResponse logout() throws Exception {
//
//        return memberService.logout();
//    }
}
