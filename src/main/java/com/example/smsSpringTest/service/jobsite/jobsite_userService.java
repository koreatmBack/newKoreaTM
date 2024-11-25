package com.example.smsSpringTest.service.jobsite;

import com.example.smsSpringTest.mapper.jobsite.JobCommonMapper;
import com.example.smsSpringTest.mapper.jobsite.JobUserMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.jobsite.Cert;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.jobsite.Social;
import com.example.smsSpringTest.model.jobsite.SocialUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.JobUserResponse;
import com.example.smsSpringTest.model.response.jobsite.SocialResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : jobsite 회원 service
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class jobsite_userService {

    private final jobsite_commonService jobsiteCommonService;
    private final JobUserMapper jobUserMapper;
    private final JobCommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${facebook.client-id}")
    private String facebookClientId;

    @Value("${facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${facebook.redirect-uri}")
    private String facebookRedirectUri;

    // 본인인증 문자 코드 일치하는지 확인하기
    public ApiResponse cert(Cert cert) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        cert.setPhone(cert.getPhone().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3"));
        try {
            int certUser = jobUserMapper.certUser(cert);
            if(certUser == 1) {
                // 인증 코드 일치
                apiResponse.setCode("C000");
                apiResponse.setMessage("본인인증 성공");
                jobUserMapper.deleteSmsCode(cert);
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("본인인증 실패 , 인증 번호를 다시 요청해주세요.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("이름, 연락처, 본인인증 다시 입력");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 본인인증 이메일 코드 일치하는지 확인하기
    public ApiResponse certEmail(Cert cert) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int certEmail = jobUserMapper.certUserEmail(cert);
            if(certEmail == 1) {
                // 인증 코드 일치
                apiResponse.setCode("C000");
                apiResponse.setMessage("본인인증 성공");
                jobUserMapper.deleteEmailCode(cert);
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("본인인증 실패 , 인증 번호를 다시 요청해주세요.");
            }
        } catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("이메일, 본인인증 코드 다시 입력");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 아이디 찾기 눌렀을때 가입된 아이디인지 확인하기 (userName, phone or email 필수)
    public ApiResponse findJobUserIdBeforeCert(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
//            if(StringUtils.hasText(user.getPhone())){
//                // 만약 연락처 값이 있으면 010-0000-9999 형식으로 변환하기
//                user.setPhone(user.getPhone().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3"));
//            }
            int findUserId = jobUserMapper.findJobUserIdBeforeCert(user);
            if(findUserId != 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("가입된 정보가 있습니다.");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("가입된 정보가 없습니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error !!! ");
        }

        return apiResponse;
    }


    // 문자 or 이메일 본인 인증 후 넘겨받은 연락처로 Id, 가입일 찾기
    public JobUserResponse findJobUserId(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();

        try {
//            if(StringUtils.hasText(user.getPhone())){
//                // 만약 연락처 값이 있으면 010-0000-9999 형식으로 변환하기
//                user.setPhone(user.getPhone().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3"));
//            }
            String findJobUserId = jobUserMapper.findJobUserId(user).getUserId();
            LocalDate findJobUserCreate = jobUserMapper.findJobUserId(user).getCreatedAt();
            if(!StringUtils.hasText(findJobUserId)){
                jobUserResponse.setCode("E003");
                jobUserResponse.setMessage("Id 찾기 실패");
            } else {
                jobUserResponse.setUserId(findJobUserId);
                jobUserResponse.setCreatedAt(findJobUserCreate);
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("Id 찾기 성공");
            }
        } catch (Exception e) {
                jobUserResponse.setCode("E001");
                jobUserResponse.setMessage("Error !!!");
        }

        return jobUserResponse;
    }


//    // 이메일 본인 인증 후 넘겨받은 이메일 주소로 id, 가입일 찾기
//    public JobUserResponse findJobUserIdFromEmail(JobsiteUser user) throws Exception {
//        JobUserResponse jobUserResponse = new JobUserResponse();
//
//        try {
//            String findJobUserId = jobUserMapper.findJobUserIdFromEmail(user.getEmail()).getUserId();
//            LocalDate findJobUserCreate = jobUserMapper.findJobUserIdFromEmail(user.getEmail()).getCreatedAt();
//            if(!StringUtils.hasText(findJobUserId)){
//                jobUserResponse.setCode("E003");
//                jobUserResponse.setMessage("Id 찾기 실패");
//            } else {
//                jobUserResponse.setUserId(findJobUserId);
//                jobUserResponse.setCreatedAt(findJobUserCreate);
//                jobUserResponse.setCode("C000");
//                jobUserResponse.setMessage("Id 찾기 성공");
//            }
//        } catch (Exception e) {
//            jobUserResponse.setCode("E001");
//            jobUserResponse.setMessage("Error !!!");
//        }
//
//        return jobUserResponse;
//    }


    // 문자 본인 인증 후 회원가입
    public ApiResponse jobSignUp(JobsiteUser user) throws Exception{
        ApiResponse apiResponse = new ApiResponse();

        try {
//            int dupFormMailIdCheck = jobUserMapper.dupFormMailIdCheck(user.getUserId());
//            if(dupFormMailIdCheck != 0){
//                // 폼메일에 같은 id가 있으면
//                apiResponse.setCode("E004");
//                apiResponse.setMessage("폼메일에서 사용중인 id입니다. 다른 id를 입력해주세요");
//                return apiResponse;
//            }
            user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            int result = jobUserMapper.jobSignUp(user);

            if (result == 1) {

                if(StringUtils.hasText(user.getSocialId())){
                    // 만약 소셜 고유id가 있으면
                    Social social = new Social();
                    social.setUserId(user.getUserId());
                    social.setSocialId(user.getSocialId());
                    social.setSocialType(user.getSocialType());
                    jobUserMapper.addSocialData(social);
                }

                apiResponse.setCode("C000");
                apiResponse.setMessage("회원 등록이 완료되었습니다.");
            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("회원 등록 실패 !!");
            }
        } catch (Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("다시 입력해주세요.");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 잡사이트 로그인
    @Transactional
    public JobUserResponse jobLogin(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();

        try {
            String userId = user.getUserId();
            // 입력 받은 비밀번호
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = jobUserMapper.dupPwd(user);

            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);

            if(isMatchPwd){
                // 비밀번호 일치
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPwd);
                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                    log.info("authenticationToken = " + authenticationToken);
                    log.info("authentication = " + authentication);
                    log.info("auth ROLE = " + authentication.getAuthorities());

                    userId = authentication.getName();
                    log.info("userId = " + userId);

                    RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
                    log.info("refToken = " + refToken);
                    RefToken refToken2 = new RefToken();
                    Token token = null;

                    if (refToken != null) {
                        // refresh token이 null이 아닐때
                        LocalDate now = LocalDate.now();
                        String uptDate = refToken.getUptDate();
                        LocalDate parseUptDate = LocalDate.parse(uptDate);
                        log.info("uptDate = " + uptDate);
                        log.info("parseUptDate = " + parseUptDate);

                        Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                        log.info("남은 Refresh Token 유효 기간 밀리seconds = " + remainingMilliseconds);

                        if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                            // refresh token 유효기간이 null 이거나 0보다 같거나 작을때 ( 즉, 만료 되었을 때 )
                            commonMapper.deleteUserToken(userId);

                            // 토큰(access, refresh) 재생성
                            token = jwtTokenProvider.generateToken(authentication);
                            log.info("만료되었을때 재생성한 토큰 = " + token);
                            refToken2.setUserId(userId);
                            refToken2.setGrantType(token.getGrantType());
                            refToken2.setRefreshToken(token.getRefreshToken());
                            commonMapper.addUserToken(refToken2);
                        } else if (now.isAfter(parseUptDate.plusDays(28))) {
                            // 현재 날짜가, uptdate + 28일보다 이후일때
                            // 토큰 재생성
                            token = jwtTokenProvider.generateToken(authentication);
                            log.info("After 28 -> token = " + token);
                            refToken2.setUserId(userId);
                            refToken2.setGrantType(token.getGrantType());
                            refToken2.setRefreshToken(token.getRefreshToken());
                            commonMapper.updateUserToken(refToken2);
                        } else {
                            // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                            // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                            Cookie cookies[] = request.getCookies();

                            // 만약 쿠키가 있다면
                            if(cookies != null) {
                                String accessToken = jwtTokenProvider.extractTokenFromCookies(cookies);
//                                for (Cookie cookie : cookies) {
//                                    if ("accesstoken".equals(cookie.getName())) {
//                                        accessToken = cookie.getValue();
//                                    }
//                                }
//                                String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
//                                log.info("쿠키 유저 정보 테스트 = " + jwtTokenProvider.getAuthentication(accessToken));
//                                log.info("쿠키 유저 이름 테스트 = " + cookieName);
                                if(StringUtils.hasText(accessToken) && userId.equals(jwtTokenProvider.getAuthentication(accessToken).getName())) {
                                    // accesstoken 이라는 쿠키가 있을때
                                    String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
                                    userId = cookieName;
                                    log.info("아직 유효한 cookie = " + accessToken);
                                    Long accessTokenExpiration = jwtTokenProvider.getExpiration(accessToken);
                                    log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                    jobUserResponse.setCode("C001");
                                    String userName = jobUserMapper.userName(userId);
                                    jobUserResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                            accessTokenExpiration/1000 + "초 남았습니다.");
                                    return jobUserResponse;
                                }
                            }

                            token = jwtTokenProvider.accessToken(authentication);
                            log.info("새로 생성한 AccessToken = " + token);
//                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
//                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                        }
                    } else {
                        // refresh token이 null

                        // 토큰 재생성
                        token = jwtTokenProvider.generateToken(authentication);
                        log.info("refToken이 null일때 token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    }

                    if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                        // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                        JobsiteUser user2 = jobUserMapper.findOneJobLoginUser(userId);
                        user2.setRole("user");
                        jobUserResponse.setUser(user2);
                        String userName = jobUserMapper.userName(userId);
                        jobUserResponse.setCode("C000");
                        jobUserResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                        response.addCookie(cookie);
                    } else {
                        // 최종적으로 access 토큰이 없을때
                        jobUserResponse.setCode("E001");
                        jobUserResponse.setMessage("최종적으로 Access Token이 없습니다.");
                    }
                } catch (BadCredentialsException e) {
                    jobUserResponse.setCode("E003");
                    jobUserResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                    log.info(e.getMessage());
                }
            } else {
                // id 값이 없거나(다르거나), 비밀번호 일치 X
                jobUserResponse.setCode("E005");
                jobUserResponse.setMessage("로그인 실패, 로그인 정보가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            log.info("로그인 중 예외 발생: {}", e.getMessage(), e);
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("ERROR!!!");
        }
        return jobUserResponse;
    }

    // 잡사이트 로그아웃 -> 로그아웃시 리프레쉬 토큰, 해당 쿠키도 삭제
    public ApiResponse jobLogout() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        JobsiteUser user = new JobsiteUser();

        Cookie cookies[] = request.getCookies();
        String accessToken = "";

        // 만약 쿠키가 있다면
        for(Cookie cookie : cookies) {
            if("accesstoken".equals(cookie.getName())){
                accessToken = cookie.getValue();
            }
        }

        // 쿠키가 없을때
        if(!StringUtils.hasText(accessToken)) {
            apiResponse.setCode("E401");
            apiResponse.setMessage("로그인 후 다시 이용해주세요.");
            return apiResponse;
        }
        log.info("로그아웃시 accessToken = " + accessToken);

        // AccessToken 검증
        if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
            //AccessToken에서 authentication 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            user.setUserId(authentication.getName());
            log.info("로그아웃할 id = " + user.getUserId());

            for(Cookie cookie : cookies){
                if(accessToken.equals(cookie.getValue())){
                    // 해당 쿠키 삭제
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }

            // db속 refresh token 가져오기
            RefToken refDB = jobsiteCommonService.getUserRefToken(user.getUserId());
            log.info("refDb = " + refDB);
            if(refDB.getRefreshToken() != null) {
                // refresh 토큰 삭제
                commonMapper.deleteUserToken(authentication.getName());
                log.info("refresh 토큰 삭제");
                apiResponse.setCode("C000");
                String userName = jobUserMapper.userName(user.getUserId());
                apiResponse.setMessage(userName + "님 로그아웃 되었습니다.");
            }

        } else {
            apiResponse.setCode("E401");
            apiResponse.setMessage("유효하지 않은 접근입니다.");
        }
        return apiResponse;
    }

    // 잡사이트 회원 정보 수정
    @Transactional
    public JobUserResponse jobUserUpdate(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();

        try {

            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = jobUserMapper.dupPwd(user);

            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);
            if(!isMatchPwd){
                jobUserResponse.setCode("C003");
                jobUserResponse.setMessage("비밀번호가 일치하지 않습니다.");
                return jobUserResponse;
            }

            int jobUserUpdate = jobUserMapper.jobUserUpdate(user);
            if(jobUserUpdate == 1) {
                jobUserResponse.setUser(jobUserMapper.findOneJobUser(user.getUserId()));
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("회원 정보 수정 완료");
            } else {
                jobUserResponse.setCode("C003");
                jobUserResponse.setMessage("비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e){
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }

        return jobUserResponse;
    }

    // 잡사이트 회원 한명 정보 반환
    public JobUserResponse findOneJobUser(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();

        try {
            jobUserResponse.setUser(jobUserMapper.findOneJobUser(user.getUserId()));
            jobUserResponse.setCode("C000");
            jobUserResponse.setMessage("잡사이트 회원 한 명 정보 조회 성공");
        } catch (Exception e) {
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("조회할 회원의 아이디를 입력하세요");
        }
        return jobUserResponse;
    }

    // 잡사이트 전체 회원 정보 반환
    public JobUserResponse findAllJobUser(Paging paging) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();

        try {
            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = jobUserMapper.getUserListCount();

            paging.setOffset(offset);
            jobUserResponse.setJobsiteUserList(jobUserMapper.jobsiteUserList(paging));
            log.info(jobUserMapper.jobsiteUserList(paging).toString());
            log.info("userResponse :  page = " + page + ", size = " + size + ", offset = " + offset + ", totalCount = " + totalCount);

            if(jobUserResponse.getJobsiteUserList() != null && !jobUserResponse.getJobsiteUserList().isEmpty()){
                // 비어있지 않을 때
                int totalPages = (int) Math.ceil((double) totalCount / size);
                jobUserResponse.setTotalPages(totalPages);
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("잡사이트 회원 전체 조회 성공");
            } else {
                // 비어 있을 때
                jobUserResponse.setCode("E002");
                jobUserResponse.setMessage("조회된 계정이 없습니다.");
            }
        } catch (Exception e) {
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("ERROR!!!");
        }
        return jobUserResponse;
    }

    // 회원 탈퇴하기
    public ApiResponse jobResign(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
//            String userId = user.getUserId();

            // 입력 받은 비밀번호
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = jobUserMapper.dupPwd(user);

            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);

            if(isMatchPwd) {
                // 회원 탈퇴하기
                jobUserMapper.resignUser(user);
                apiResponse.setCode("C000");
                apiResponse.setMessage(" 회원 탈퇴 성공 ");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage(" 회원 탈퇴 실패 , 정보 불일치");
            }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error!!! ");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 회원가입시 id 중복 확인 버튼 비동기로 확인 API
    public ApiResponse checkId(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            // 잡사이트에서 체크
            int jobCheckId = jobUserMapper.checkId(user.getUserId());

            // 폼메일에서 체크
            int formCheckId = jobUserMapper.dupFormMailIdCheck(user.getUserId());

            if(jobCheckId == 0 && formCheckId == 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("사용 가능한 ID입니다.");
            } else {
                apiResponse.setCode("E002");
                apiResponse.setMessage("(폼메일, 잡사이트) 이미 사용중인 ID입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
        }

        return apiResponse;
    }

    // 회원가입시 비동기로 email 중복 체크 확인하는 API
    public ApiResponse checkEmail(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int checkEmail = jobUserMapper.checkEmail(user.getEmail());
            if(checkEmail == 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("사용 가능한 email입니다.");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("이미 사용중인 email입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
        }
        return apiResponse;
    }

    // 회원 정보 수정 -> 비밀번호 변경하기 (userId, 기존 pwd, 새로운 pwd)
    public ApiResponse changePwd(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            // 기존 비밀번호 일치하는지 체크
            // 입력 받은 비밀번호
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = jobUserMapper.dupPwd(user);

            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);

            if(isMatchPwd) {
                // 비밀번호 일치하면

                // 입력받은 새로운 비밀번호
                String newPwd = user.getUserNewPwd();
                user.setUserPwd(passwordEncoder.encode(newPwd));
                int changePwd = jobUserMapper.changePwd(user);
                if(changePwd == 1) {
                    apiResponse.setCode("C000");
                    apiResponse.setMessage(" 비밀번호 변경 성공 ");
                } else {
                    apiResponse.setCode("C003");
                    apiResponse.setMessage(" 비밀번호 변경 실패 ");
                }
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage(" 비밀번호 변경 실패 , 정보 불일치");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
        }

        return apiResponse;
    }

    // 회원 id 일치할때 즐겨찾기 삭제
    public ApiResponse deleteFavorite(JobsiteUser user) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {
            int deleteFavorite = jobUserMapper.deleteFavorite(user);
            if(deleteFavorite == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("즐겨찾기 삭제 성공");
            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("즐겨찾기 삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }

        return apiResponse;
    }

    // 회원 id 일치할때 스크랩 삭제
    public ApiResponse deleteClipping(JobsiteUser user) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {
            int deleteClipping = jobUserMapper.deleteClipping(user);
            if(deleteClipping == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("스크랩 삭제 성공");
            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("스크랩 삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }

        return apiResponse;
    }

    // 회원 id 일치할때 즐겨찾기 반환
    public JobUserResponse findFavorite(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();
        try {
            String findFavorite = jobUserMapper.findFavorite(user);
            if(!StringUtils.hasText(findFavorite)){
                jobUserResponse.setCode("E003");
                jobUserResponse.setMessage("즐겨찾기 목록이 없습니다.");
            } else {
                jobUserResponse.setFavorite(findFavorite);
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("즐겨찾기 목록 조회 성공");
            }
        }catch (Exception e) {
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("Error!!!");
        }
        return jobUserResponse;
    }


    // 회원 id 일치할때 스크랩 반환
    public JobUserResponse findClipping(JobsiteUser user) throws Exception {
        JobUserResponse jobUserResponse = new JobUserResponse();
        try {
            String findFavorite = jobUserMapper.findClipping(user);
            if(!StringUtils.hasText(findFavorite)){
                jobUserResponse.setCode("E003");
                jobUserResponse.setMessage("스크랩 목록이 없습니다.");
            } else {
                jobUserResponse.setClipping(findFavorite);
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("스크랩 목록 조회 성공");
            }
        }catch (Exception e) {
            jobUserResponse.setCode("E001");
            jobUserResponse.setMessage("Error!!!");
        }
        return jobUserResponse;
    }

//    // 이메일로 비밀번호 찾기 눌렀을때 본인인증 보내기 전 실행 API (필수 : email)
//    public ApiResponse findJobUserPwdFromEmail(JobsiteUser user) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//
//        try {
//            int findJobUserPwdFromEmail = jobUserMapper.findJobUserPwdFromEmail(user);
//            if(findJobUserPwdFromEmail == 0) {
//                apiResponse.setCode("C003");
//                apiResponse.setMessage("등록된 Id가 없습니다.");
//            } else {
//                apiResponse.setCode("C000");
//                apiResponse.setMessage("등록된 Id가 존재합니다.");
//            }
//        } catch (Exception e) {
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("Error !!!");
//        }
//
//        return apiResponse;
//    }


    // 비밀번호 찾기 -> 재생성하게.

    // 비밀번호 찾기 눌렀을때 본인인증 보내기 전 실행 API (userId, userName, phone or email 필요)
    public ApiResponse findJobUserPwd(JobsiteUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
//            if(StringUtils.hasText(user.getPhone())){
//                // 만약 연락처 값이 있으면 010-0000-9999 형식으로 변환하기
//                user.setPhone(user.getPhone().replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3"));
//            }
            int findJobUserPwd = jobUserMapper.findJobUserPwd(user);
            if(findJobUserPwd == 0) {
                apiResponse.setCode("E002");
                apiResponse.setMessage("등록된 Id가 없습니다.");
            } else {
                apiResponse.setCode("C000");
                apiResponse.setMessage("등록된 Id가 존재합니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error !!!");
        }

        return apiResponse;
    }

    // 본인인증 성공시 새로운 비밀번호 입력 받은 후 DB에 암호화하여 저장
    public ApiResponse updateNewPwd(JobsiteUser user) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {
            if(user.getPhone() == null && user.getEmail() == null){
                apiResponse.setCode("E001");
                apiResponse.setMessage("Error !!!");
                return apiResponse;
            }

            user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            int updateNewPwd = jobUserMapper.updateNewPwd(user);
            if(updateNewPwd == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("비밀번호 수정 성공");
            } else {
                apiResponse.setCode("E002");
                apiResponse.setMessage("비밀번호 수정 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error !!!");
        }

        return apiResponse;
    }



    // 카카오 로그인  ---------------------------

    @Transactional
    public SocialResponse kakaoLogin(String code) throws Exception {

        SocialResponse socialResponse = new SocialResponse();

        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/login/kakao";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + kakaoClientId +
                    "&client_secret=" + kakaoClientSecret +
                    "&redirect_uri=" + kakaoRedirectUri +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();

            br.close();
            bw.close();

//            log.info("카카오 accessToken = " + accessToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getKakaoUserDetail(accessToken);
//        log.info("SocialUser = " + socialUser);
//        log.info("id = " + socialUser.getId());
        int result = jobUserMapper.dupSocialIdCheck(socialUser.getId());

        if(result == 0) {
//            socialUser.setSocialType("kakao");
            socialResponse.setSocialId(socialUser.getId());
            socialResponse.setSocialType("kakao");
//            socialResponse.setSocialUser(socialUser);
            socialResponse.setCode("C003");
            socialResponse.setMessage("최초 로그인 1회 한정 본인 인증이 필요합니다.");
        } else {

            String userId = jobUserMapper.kakaoUserId(socialUser.getId());

            try {

                log.info("userId = " + userId);

                RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
                log.info("refToken = " + refToken);
                RefToken refToken2 = new RefToken();
                Token token = null;

                if (refToken != null) {
                    // refresh token이 null이 아닐때
                    LocalDate now = LocalDate.now();
                    String uptDate = refToken.getUptDate();
                    LocalDate parseUptDate = LocalDate.parse(uptDate);
                    log.info("uptDate = " + uptDate);
                    log.info("parseUptDate = " + parseUptDate);

                    Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                    log.info("남은 Refresh Token 유효 기간 밀리seconds = " + remainingMilliseconds);

                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                        // refresh token 유효기간이 null 이거나 0보다 같거나 작을때 ( 즉, 만료 되었을 때 )
                        commonMapper.deleteUserToken(userId);

                        // 토큰(access, refresh) 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("만료되었을때 재생성한 토큰 = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    } else if (now.isAfter(parseUptDate.plusDays(28))) {
                        // 현재 날짜가, uptdate + 28일보다 이후일때
                        // 토큰 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("After 28 -> token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.updateUserToken(refToken2);
                    } else {
                        // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                        // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                        Cookie cookies[] = request.getCookies();

                        // 만약 쿠키가 있다면
                        if(cookies != null) {
                            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
//                                for (Cookie cookie : cookies) {
//                                    if ("accesstoken".equals(cookie.getName())) {
//                                        accessToken = cookie.getValue();
//                                    }
//                                }
//                                String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
//                                log.info("쿠키 유저 정보 테스트 = " + jwtTokenProvider.getAuthentication(accessToken));
//                                log.info("쿠키 유저 이름 테스트 = " + cookieName);
                            if(StringUtils.hasText(cookieToken) && userId.equals(jwtTokenProvider.getAuthentication(cookieToken).getName())) {
                                // accesstoken 이라는 쿠키가 있을때
                                String cookieName = jwtTokenProvider.getAuthentication(cookieToken).getName();
                                userId = cookieName;
                                log.info("아직 유효한 cookie = " + cookieToken);
                                Long accessTokenExpiration = jwtTokenProvider.getExpiration(cookieToken);
                                log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                socialResponse.setCode("C001");
                                String userName = jobUserMapper.userName(userId);
                                socialResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                        accessTokenExpiration/1000 + "초 남았습니다.");
                                return socialResponse;
                            }
                        }

                        token = jwtTokenProvider.socialAccessToken(userId);
                        log.info("새로 생성한 AccessToken = " + token);
//                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
//                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                    }
                } else {
                    // refresh token이 null

                    // 토큰 재생성
                    token = jwtTokenProvider.socialGenerateToken(userId);
                    log.info("refToken이 null일때 token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.addUserToken(refToken2);
                }

                if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                    // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                    JobsiteUser user2 = jobUserMapper.findOneJobLoginUser(userId);
                    user2.setRole("user");
                    socialResponse.setUser(user2);
                    String userName = jobUserMapper.userName(userId);
                    socialResponse.setCode("C000");
                    socialResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                    Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                    response.addCookie(cookie);
                } else {
                    // 최종적으로 access 토큰이 없을때
                    socialResponse.setCode("E001");
                    socialResponse.setMessage("최종적으로 Access Token이 없습니다.");
                }
            } catch (BadCredentialsException e) {
                socialResponse.setCode("E003");
                socialResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                log.info(e.getMessage());
            }
        }

        return socialResponse;
    }

    // KAKAO API 호출해서 카카오계정(정보) 가져오기
    @Transactional
    private SocialUser getKakaoUserDetail(String accessToken) throws Exception {

        SocialUser socialUser = new SocialUser();
        String host = "https://kapi.kakao.com/v2/user/me";

        try {

            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder res = new StringBuilder();
            while((line=br.readLine())!=null) {
                res.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(res.toString());
            JsonObject kakao_account = (JsonObject) keys.get("kakao_account");
//            log.info("kakao keys = " + keys);
            String id = keys.get("id").toString();
//            String emailWithQuotes = kakao_account.get("email").toString();
//            String email = emailWithQuotes.replaceAll("^\"|\"$", "");

            socialUser.setId(id);
//            socialUser.setEmail(email);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socialUser;
    }


    // 로그인 회원 kakao 소셜 로그인 연동
    public ApiResponse userIntegKakao(String code) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        Cookie cookies[] = request.getCookies();
        String userId = null;
        // 만약 쿠키가 있다면
        if (cookies != null) {
            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
            if (StringUtils.hasText(cookieToken)) {
                userId = jwtTokenProvider.getAuthentication(cookieToken).getName();
            }

        }

        if(!StringUtils.hasText(userId)){
            apiResponse.setCode("E001");
            apiResponse.setMessage("No Cookie ERROR!!!");
            return apiResponse;
        }
        log.info("연동시 userId = " + userId);

        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/kakao/integ";
        String redirectUrl = "https://cafecon.co.kr/v1/jobsite/user/kakao/integ";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + kakaoClientId +
                    "&client_secret=" + kakaoClientSecret +
                    "&redirect_uri=" + redirectUrl +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getKakaoUserDetail(accessToken);

        // 카카오만 쓸때 필요하고, 현재는 x
//        int result = jobUserMapper.kakaoUserChk(socialUser.getId());

        // 소셜 로그인 완료된 id 있는지 찾기
        int socialUserIdCheck = jobUserMapper.dupSocialUserIdCheck(userId);

        // 소셜 로그인 완료된 소셜 고유id 있는지 찾기
        String socialId = socialUser.getId();
        int socialIdCheck = jobUserMapper.dupSocialIdCheck(socialId);

        try {
            if (socialUserIdCheck == 0 && socialIdCheck == 0) {
                Social social = new Social();
//            String email = userMapper.getUserEmail(user.getUserId());
//
//            if(email == null || email.isBlank()) {
//                user.setEmail(socialUser.getEmail());
//                userMapper.editUserEmail(user);
//            }


                social.setUserId(userId);
                social.setSocialId(socialUser.getId());
                social.setSocialType("kakao");

                jobUserMapper.addSocialData(social);
                apiResponse.setCode("C000");
                apiResponse.setMessage("연동 완료");

            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("이미 소셜 로그인 연동완료된 계정입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 카카오 끝

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // NAVER 로그인

    @Transactional
    public SocialResponse naverLogin(String code) throws Exception {

        SocialResponse socialResponse = new SocialResponse();

        String host = "https://nid.naver.com/oauth2.0/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/login/kakao";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&redirect_uri=" + naverRedirectUri +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();

            br.close();
            bw.close();

            log.info("네이버 accessToken = " + accessToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getNaverUserDetail(accessToken);
        log.info("SocialUser = " + socialUser);
        log.info("id = " + socialUser.getId());
        String naverSocialId = socialUser.getId();
        int result = jobUserMapper.dupSocialIdCheck(naverSocialId);

        if(result == 0) {
//            socialUser.setSocialType("naver");
            socialResponse.setSocialId(naverSocialId);
            socialResponse.setSocialType("naver");
//            socialResponse.setSocialUser(socialUser);
            socialResponse.setCode("C003");
            socialResponse.setMessage("최초 로그인 1회 한정 본인 인증이 필요합니다.");
        } else {

            String userId = jobUserMapper.kakaoUserId(naverSocialId);

            try {

                log.info("userId = " + userId);

                RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
                log.info("refToken = " + refToken);
                RefToken refToken2 = new RefToken();
                Token token = null;

                if (refToken != null) {
                    // refresh token이 null이 아닐때
                    LocalDate now = LocalDate.now();
                    String uptDate = refToken.getUptDate();
                    LocalDate parseUptDate = LocalDate.parse(uptDate);
                    log.info("uptDate = " + uptDate);
                    log.info("parseUptDate = " + parseUptDate);

                    Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                    log.info("남은 Refresh Token 유효 기간 밀리seconds = " + remainingMilliseconds);

                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                        // refresh token 유효기간이 null 이거나 0보다 같거나 작을때 ( 즉, 만료 되었을 때 )
                        commonMapper.deleteUserToken(userId);

                        // 토큰(access, refresh) 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("만료되었을때 재생성한 토큰 = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    } else if (now.isAfter(parseUptDate.plusDays(28))) {
                        // 현재 날짜가, uptdate + 28일보다 이후일때
                        // 토큰 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("After 28 -> token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.updateUserToken(refToken2);
                    } else {
                        // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                        // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                        Cookie cookies[] = request.getCookies();

                        // 만약 쿠키가 있다면
                        if(cookies != null) {
                            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
//                                for (Cookie cookie : cookies) {
//                                    if ("accesstoken".equals(cookie.getName())) {
//                                        accessToken = cookie.getValue();
//                                    }
//                                }
//                                String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
//                                log.info("쿠키 유저 정보 테스트 = " + jwtTokenProvider.getAuthentication(accessToken));
//                                log.info("쿠키 유저 이름 테스트 = " + cookieName);
                            if(StringUtils.hasText(cookieToken) && userId.equals(jwtTokenProvider.getAuthentication(cookieToken).getName())) {
                                // accesstoken 이라는 쿠키가 있을때
                                String cookieName = jwtTokenProvider.getAuthentication(cookieToken).getName();
                                userId = cookieName;
                                log.info("아직 유효한 cookie = " + cookieToken);
                                Long accessTokenExpiration = jwtTokenProvider.getExpiration(cookieToken);
                                log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                socialResponse.setCode("C001");
                                String userName = jobUserMapper.userName(userId);
                                socialResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                        accessTokenExpiration/1000 + "초 남았습니다.");
                                return socialResponse;
                            }
                        }

                        token = jwtTokenProvider.socialAccessToken(userId);
                        log.info("새로 생성한 AccessToken = " + token);
//                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
//                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                    }
                } else {
                    // refresh token이 null

                    // 토큰 재생성
                    token = jwtTokenProvider.socialGenerateToken(userId);
                    log.info("refToken이 null일때 token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.addUserToken(refToken2);
                }

                if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                    // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                    JobsiteUser user2 = jobUserMapper.findOneJobLoginUser(userId);
                    user2.setRole("user");
                    socialResponse.setUser(user2);
                    String userName = jobUserMapper.userName(userId);
                    socialResponse.setCode("C000");
                    socialResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                    Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                    response.addCookie(cookie);
                } else {
                    // 최종적으로 access 토큰이 없을때
                    socialResponse.setCode("E001");
                    socialResponse.setMessage("최종적으로 Access Token이 없습니다.");
                }
            } catch (BadCredentialsException e) {
                socialResponse.setCode("E003");
                socialResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                log.info(e.getMessage());
            }
        }

        return socialResponse;
    }

    // NAVER API 호출해서 카카오계정(정보) 가져오기
    @Transactional
    private SocialUser getNaverUserDetail(String accessToken) throws Exception {

        SocialUser socialUser = new SocialUser();
        String host = "https://openapi.naver.com/v1/nid/me";

        try {

            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder res = new StringBuilder();
            while((line=br.readLine())!=null) {
                res.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(res.toString());

            JsonObject naver_response = (JsonObject) keys.get("response");
//            log.info("naver_response = " + naver_response);
            String mobile = naver_response.get("mobile").toString();
            String id = naver_response.get("id").toString();
//            log.info("naver mobile = " + mobile);
//            log.info("naver id = " + id);
            String naverSocialId = id.replaceAll("\"","");
//            log.info("naverSocialId = " + naverSocialId);
            socialUser.setId(naverSocialId);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socialUser;
    }

    // 로그인 회원 naver 소셜 로그인 연동
    public ApiResponse userIntegNaver(String code) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        Cookie cookies[] = request.getCookies();
        String userId = null;
        // 만약 쿠키가 있다면
        if (cookies != null) {
            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
            if (StringUtils.hasText(cookieToken)) {
                userId = jwtTokenProvider.getAuthentication(cookieToken).getName();
            }

        }

        if(!StringUtils.hasText(userId)){
            apiResponse.setCode("E001");
            apiResponse.setMessage("No Cookie ERROR!!!");
            return apiResponse;
        }
        log.info("연동시 userId = " + userId);

        String host = "https://nid.naver.com/oauth2.0/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/naver/integ";
        String redirectUrl = "https://cafecon.co.kr/v1/jobsite/user/naver/integ";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&redirect_uri=" + redirectUrl +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();
//            log.info("연동시 keys = " + keys);
//            log.info("연동시 accessToken = " + accessToken);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getNaverUserDetail(accessToken);

        // 카카오만 쓸때 필요하고, 현재는 x
//        int result = jobUserMapper.kakaoUserChk(socialUser.getId());

        // 소셜 로그인 완료된 id 있는지 찾기
        int socialUserIdCheck = jobUserMapper.dupSocialUserIdCheck(userId);

        // 소셜 로그인 완료된 소셜 고유id 있는지 찾기
        String socialId = socialUser.getId();
        int socialIdCheck = jobUserMapper.dupSocialIdCheck(socialId);

//        log.info("socialId = " + socialId);
//        log.info("socialIdCheck = " + socialIdCheck);

        try {
            if (socialUserIdCheck == 0 && socialIdCheck == 0) {
                Social social = new Social();
//            String email = userMapper.getUserEmail(user.getUserId());
//
//            if(email == null || email.isBlank()) {
//                user.setEmail(socialUser.getEmail());
//                userMapper.editUserEmail(user);
//            }


                social.setUserId(userId);
                social.setSocialId(socialUser.getId());
                social.setSocialType("naver");

                jobUserMapper.addSocialData(social);
                apiResponse.setCode("C000");
                apiResponse.setMessage("연동 완료");

            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("이미 소셜 로그인 연동완료된 계정입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }
    // NAVER 로그인 끝


    // ------- google 로그인 시작

    // Google 로그인
    @Transactional
    public SocialResponse googleLogin(String code) throws Exception {

        SocialResponse socialResponse = new SocialResponse();

        String host = "https://oauth2.googleapis.com/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/login/kakao";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + googleClientId +
                    "&client_secret=" + googleClientSecret +
                    "&redirect_uri=" + googleRedirectUri +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();

            br.close();
            bw.close();

//            log.info("구글 accessToken = " + accessToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getGoogleUserDetail(accessToken);
        log.info("SocialUser = " + socialUser);
        log.info("id = " + socialUser.getId());
        String googleSocialId = socialUser.getId();
        int result = jobUserMapper.dupSocialIdCheck(googleSocialId);

        if(result == 0) {
//            socialUser.setSocialType("naver");
            socialResponse.setSocialId(googleSocialId);
            socialResponse.setSocialType("google");
//            socialResponse.setSocialUser(socialUser);
            socialResponse.setCode("C003");
            socialResponse.setMessage("최초 로그인 1회 한정 본인 인증이 필요합니다.");
        } else {

            String userId = jobUserMapper.kakaoUserId(googleSocialId);

            try {

                log.info("userId = " + userId);

                RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
                log.info("refToken = " + refToken);
                RefToken refToken2 = new RefToken();
                Token token = null;

                if (refToken != null) {
                    // refresh token이 null이 아닐때
                    LocalDate now = LocalDate.now();
                    String uptDate = refToken.getUptDate();
                    LocalDate parseUptDate = LocalDate.parse(uptDate);
                    log.info("uptDate = " + uptDate);
                    log.info("parseUptDate = " + parseUptDate);

                    Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                    log.info("남은 Refresh Token 유효 기간 밀리seconds = " + remainingMilliseconds);

                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                        // refresh token 유효기간이 null 이거나 0보다 같거나 작을때 ( 즉, 만료 되었을 때 )
                        commonMapper.deleteUserToken(userId);

                        // 토큰(access, refresh) 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("만료되었을때 재생성한 토큰 = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    } else if (now.isAfter(parseUptDate.plusDays(28))) {
                        // 현재 날짜가, uptdate + 28일보다 이후일때
                        // 토큰 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("After 28 -> token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.updateUserToken(refToken2);
                    } else {
                        // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                        // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                        Cookie cookies[] = request.getCookies();

                        // 만약 쿠키가 있다면
                        if(cookies != null) {
                            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
//                                for (Cookie cookie : cookies) {
//                                    if ("accesstoken".equals(cookie.getName())) {
//                                        accessToken = cookie.getValue();
//                                    }
//                                }
//                                String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
//                                log.info("쿠키 유저 정보 테스트 = " + jwtTokenProvider.getAuthentication(accessToken));
//                                log.info("쿠키 유저 이름 테스트 = " + cookieName);
                            if(StringUtils.hasText(cookieToken) && userId.equals(jwtTokenProvider.getAuthentication(cookieToken).getName())) {
                                // accesstoken 이라는 쿠키가 있을때
                                String cookieName = jwtTokenProvider.getAuthentication(cookieToken).getName();
                                userId = cookieName;
                                log.info("아직 유효한 cookie = " + cookieToken);
                                Long accessTokenExpiration = jwtTokenProvider.getExpiration(cookieToken);
                                log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                socialResponse.setCode("C001");
                                String userName = jobUserMapper.userName(userId);
                                socialResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                        accessTokenExpiration/1000 + "초 남았습니다.");
                                return socialResponse;
                            }
                        }

                        token = jwtTokenProvider.socialAccessToken(userId);
                        log.info("새로 생성한 AccessToken = " + token);
//                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
//                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                    }
                } else {
                    // refresh token이 null

                    // 토큰 재생성
                    token = jwtTokenProvider.socialGenerateToken(userId);
                    log.info("refToken이 null일때 token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.addUserToken(refToken2);
                }

                if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                    // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                    JobsiteUser user2 = jobUserMapper.findOneJobLoginUser(userId);
                    user2.setRole("user");
                    socialResponse.setUser(user2);
                    String userName = jobUserMapper.userName(userId);
                    socialResponse.setCode("C000");
                    socialResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                    Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                    response.addCookie(cookie);
                } else {
                    // 최종적으로 access 토큰이 없을때
                    socialResponse.setCode("E001");
                    socialResponse.setMessage("최종적으로 Access Token이 없습니다.");
                }
            } catch (BadCredentialsException e) {
                socialResponse.setCode("E003");
                socialResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                log.info(e.getMessage());
            }
        }

        return socialResponse;
    }


    // Google API 호출해서 google 계정(정보) 가져오기
    @Transactional
    private SocialUser getGoogleUserDetail(String accessToken) throws Exception {

        SocialUser socialUser = new SocialUser();
        String host = "https://www.googleapis.com/oauth2/v1/userinfo";

        try {

            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder res = new StringBuilder();
            while((line=br.readLine())!=null) {
                res.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(res.toString());

//            log.info("google key = " + keys);

//            JsonObject google_response = (JsonObject) keys.get("response");
//            log.info("google_response = " + google_response);
//            String mobile = google_response.get("mobile").toString();
            String id = keys.get("id").toString();
//            log.info("naver mobile = " + mobile);
            String googleSocialId = id.replaceAll("\"","");
//            log.info("google id = " + googleSocialId);

            socialUser.setId(googleSocialId);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socialUser;
    }

    // 로그인 회원 google 소셜 로그인 연동
    public ApiResponse userIntegGoogle(String code) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        Cookie cookies[] = request.getCookies();
        String userId = null;
        // 만약 쿠키가 있다면
        if (cookies != null) {
            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
            if (StringUtils.hasText(cookieToken)) {
                userId = jwtTokenProvider.getAuthentication(cookieToken).getName();
            }

        }

        if(!StringUtils.hasText(userId)){
            apiResponse.setCode("E001");
            apiResponse.setMessage("No Cookie ERROR!!!");
            return apiResponse;
        }
        log.info("연동시 userId = " + userId);

        String host = "https://oauth2.googleapis.com/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/google/integ";
        String redirectUrl = "https://cafecon.co.kr/v1/jobsite/user/google/integ";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + googleClientId +
                    "&client_secret=" + googleClientSecret +
                    "&redirect_uri=" + redirectUrl +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();
//            log.info("연동시 keys = " + keys);
//            log.info("연동시 accessToken = " + accessToken);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getGoogleUserDetail(accessToken);

        // 소셜 로그인 완료된 id 있는지 찾기
        int socialUserIdCheck = jobUserMapper.dupSocialUserIdCheck(userId);

        // 소셜 로그인 완료된 소셜 고유id 있는지 찾기
        String socialId = socialUser.getId();
        int socialIdCheck = jobUserMapper.dupSocialIdCheck(socialId);

        try {
            if (socialUserIdCheck == 0 && socialIdCheck == 0) {
                Social social = new Social();

                social.setUserId(userId);
                social.setSocialId(socialUser.getId());
                social.setSocialType("google");

                jobUserMapper.addSocialData(social);
                apiResponse.setCode("C000");
                apiResponse.setMessage("연동 완료");

            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("이미 소셜 로그인 연동완료된 계정입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // ------- facebook 로그인 시작

    // facebook 로그인
    @Transactional
    public SocialResponse facebookLogin(String code) throws Exception {

        SocialResponse socialResponse = new SocialResponse();

        String host = "https://graph.facebook.com/v15.0/oauth/access_token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
//        String redirectUrl = "http://localhost:8080/v1/jobsite/user/login/kakao";

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + facebookClientId +
                    "&client_secret=" + facebookClientSecret +
                    "&redirect_uri=" + facebookRedirectUri +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();

            br.close();
            bw.close();

//            log.info("구글 accessToken = " + accessToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getFacebookUserDetail(accessToken);
        log.info("SocialUser = " + socialUser);
        log.info("id = " + socialUser.getId());
        String facebookSocialId = socialUser.getId();
        int result = jobUserMapper.dupSocialIdCheck(facebookSocialId);

        if(result == 0) {
            socialResponse.setSocialId(facebookSocialId);
            socialResponse.setSocialType("facebook");
//            socialResponse.setSocialUser(socialUser);
            socialResponse.setCode("C003");
            socialResponse.setMessage("최초 로그인 1회 한정 본인 인증이 필요합니다.");
        } else {

            String userId = jobUserMapper.kakaoUserId(facebookSocialId);

            try {

                log.info("userId = " + userId);

                RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
                log.info("refToken = " + refToken);
                RefToken refToken2 = new RefToken();
                Token token = null;

                if (refToken != null) {
                    // refresh token이 null이 아닐때
                    LocalDate now = LocalDate.now();
                    String uptDate = refToken.getUptDate();
                    LocalDate parseUptDate = LocalDate.parse(uptDate);
                    log.info("uptDate = " + uptDate);
                    log.info("parseUptDate = " + parseUptDate);

                    Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                    log.info("남은 Refresh Token 유효 기간 밀리seconds = " + remainingMilliseconds);

                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                        // refresh token 유효기간이 null 이거나 0보다 같거나 작을때 ( 즉, 만료 되었을 때 )
                        commonMapper.deleteUserToken(userId);

                        // 토큰(access, refresh) 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("만료되었을때 재생성한 토큰 = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    } else if (now.isAfter(parseUptDate.plusDays(28))) {
                        // 현재 날짜가, uptdate + 28일보다 이후일때
                        // 토큰 재생성
                        token = jwtTokenProvider.socialGenerateToken(userId);
                        log.info("After 28 -> token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.updateUserToken(refToken2);
                    } else {
                        // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                        // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                        Cookie cookies[] = request.getCookies();

                        // 만약 쿠키가 있다면
                        if(cookies != null) {
                            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
//                                for (Cookie cookie : cookies) {
//                                    if ("accesstoken".equals(cookie.getName())) {
//                                        accessToken = cookie.getValue();
//                                    }
//                                }
//                                String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
//                                log.info("쿠키 유저 정보 테스트 = " + jwtTokenProvider.getAuthentication(accessToken));
//                                log.info("쿠키 유저 이름 테스트 = " + cookieName);
                            if(StringUtils.hasText(cookieToken) && userId.equals(jwtTokenProvider.getAuthentication(cookieToken).getName())) {
                                // accesstoken 이라는 쿠키가 있을때
                                String cookieName = jwtTokenProvider.getAuthentication(cookieToken).getName();
                                userId = cookieName;
                                log.info("아직 유효한 cookie = " + cookieToken);
                                Long accessTokenExpiration = jwtTokenProvider.getExpiration(cookieToken);
                                log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                socialResponse.setCode("C001");
                                String userName = jobUserMapper.userName(userId);
                                socialResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                        accessTokenExpiration/1000 + "초 남았습니다.");
                                return socialResponse;
                            }
                        }

                        token = jwtTokenProvider.socialAccessToken(userId);
                        log.info("새로 생성한 AccessToken = " + token);
//                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
//                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                    }
                } else {
                    // refresh token이 null

                    // 토큰 재생성
                    token = jwtTokenProvider.socialGenerateToken(userId);
                    log.info("refToken이 null일때 token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.addUserToken(refToken2);
                }

                if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                    // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                    JobsiteUser user2 = jobUserMapper.findOneJobLoginUser(userId);
                    user2.setRole("user");
                    socialResponse.setUser(user2);
                    String userName = jobUserMapper.userName(userId);
                    socialResponse.setCode("C000");
                    socialResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                    Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                    response.addCookie(cookie);
                } else {
                    // 최종적으로 access 토큰이 없을때
                    socialResponse.setCode("E001");
                    socialResponse.setMessage("최종적으로 Access Token이 없습니다.");
                }
            } catch (BadCredentialsException e) {
                socialResponse.setCode("E003");
                socialResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                log.info(e.getMessage());
            }
        }

        return socialResponse;
    }


    // facebook API 호출해서 facebook 계정(정보) 가져오기
    @Transactional
    private SocialUser getFacebookUserDetail(String accessToken) throws Exception {

        SocialUser socialUser = new SocialUser();
        String host = "https://graph.facebook.com/v15.0/me";

        try {

            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder res = new StringBuilder();
            while((line=br.readLine())!=null) {
                res.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(res.toString());

//            log.info("facebook key = " + keys);

//            JsonObject google_response = (JsonObject) keys.get("response");
//            log.info("google_response = " + google_response);
//            String mobile = google_response.get("mobile").toString();
            String id = keys.get("id").toString();
//            log.info("naver mobile = " + mobile);
            String googleSocialId = id.replaceAll("\"","");
//            log.info("google id = " + googleSocialId);

            socialUser.setId(googleSocialId);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socialUser;
    }

    // 로그인 회원 facebook 소셜 로그인 연동
    public ApiResponse userIntegFacebook(String code) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        Cookie cookies[] = request.getCookies();
        String userId = null;
        // 만약 쿠키가 있다면
        if (cookies != null) {
            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);
            if (StringUtils.hasText(cookieToken)) {
                userId = jwtTokenProvider.getAuthentication(cookieToken).getName();
            }

        }

        if(!StringUtils.hasText(userId)){
            apiResponse.setCode("E001");
            apiResponse.setMessage("No Cookie ERROR!!!");
            return apiResponse;
        }
        log.info("연동시 userId = " + userId);

        String host = "https://graph.facebook.com/v15.0/oauth/access_token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String accessToken = null;
        String redirectUrl = "https://cafecon.co.kr/v1/jobsite/user/facebook/integ";


        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + facebookClientId +
                    "&client_secret=" + facebookClientSecret +
                    "&redirect_uri=" + redirectUrl +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            accessToken = keys.get("access_token").getAsString();
//            log.info("연동시 keys = " + keys);
//            log.info("연동시 accessToken = " + accessToken);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        SocialUser socialUser = getFacebookUserDetail(accessToken);

        // 소셜 로그인 완료된 id 있는지 찾기
        int socialUserIdCheck = jobUserMapper.dupSocialUserIdCheck(userId);

        // 소셜 로그인 완료된 소셜 고유id 있는지 찾기
        String socialId = socialUser.getId();
        int socialIdCheck = jobUserMapper.dupSocialIdCheck(socialId);

        try {
            if (socialUserIdCheck == 0 && socialIdCheck == 0) {
                Social social = new Social();

                social.setUserId(userId);
                social.setSocialId(socialUser.getId());
                social.setSocialType("facebook");

                jobUserMapper.addSocialData(social);
                apiResponse.setCode("C000");
                apiResponse.setMessage("연동 완료");

            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("이미 소셜 로그인 연동완료된 계정입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

}
