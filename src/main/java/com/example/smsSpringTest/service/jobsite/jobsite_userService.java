package com.example.smsSpringTest.service.jobsite;

import com.example.smsSpringTest.mapper.jobsite.JobCommonMapper;
import com.example.smsSpringTest.mapper.jobsite.JobUserMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.jobsite.CertSMS;
import com.example.smsSpringTest.model.jobsite.JobsiteUser;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.jobsite.JobUserResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    // 본인인증 코드 일치하는지 확인하기
    public ApiResponse cert(CertSMS certSMS) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            int certUser = jobUserMapper.certUser(certSMS);
            if(certUser == 1) {
                // 인증 코드 일치
                apiResponse.setCode("C000");
                apiResponse.setMessage("본인인증 성공");
                jobUserMapper.deleteSmsCode(certSMS);
            } else {
                apiResponse.setCode("E004");
                apiResponse.setMessage("본인인증 실패 , 인증 번호를 다시 요청해주세요.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("이름, 연락처, 본인인증 다시 입력");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

    // 본인 인증 후 회원가입
    public ApiResponse jobSignUp(JobsiteUser user) throws Exception{
        ApiResponse apiResponse = new ApiResponse();

        try {
            int dupFormMailIdCheck = jobUserMapper.dupFormMailIdCheck(user.getUserId());
            if(dupFormMailIdCheck != 0){
                // 폼메일에 같은 id가 있으면
                apiResponse.setCode("E004");
                apiResponse.setMessage("폼메일에서 사용중인 id입니다. 다른 id를 입력해주세요");
                return apiResponse;
            }
            user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            int result = jobUserMapper.jobSignUp(user);

            if (result == 1) {
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
                            String accessToken = "";

                            // 만약 쿠키가 있다면
                            if(cookies != null) {
                                for (Cookie cookie : cookies) {
                                    if ("accesstoken".equals(cookie.getName())) {
                                        accessToken = cookie.getValue();
                                    }
                                }
                                if(accessToken != null && !accessToken.isBlank()) {
                                    // accesstoken 이라는 쿠키가 있을때
                                    log.info("아직 유효한 cookie = " + accessToken);
                                    Long accessTokenExpiration = jwtTokenProvider.getExpiration(accessToken);
                                    log.info("cookie 유효기간 밀리 seconds = " + accessTokenExpiration);
                                    jobUserResponse.setCode("C000");
                                    String userName = jobUserMapper.userName(userId);
                                    jobUserResponse.setMessage(userName + "님 현재 로그인 상태입니다.");
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
        if(accessToken == null) {
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
            if(refDB.getRefreshToken() != null) {
                // refresh 토큰 삭제
                commonMapper.deleteUserToken(authentication.getName());
                log.info("refresh 토큰 삭제");
                apiResponse.setCode("C000");
                apiResponse.setMessage(authentication.getName() + "님 로그아웃 되었습니다.");
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
            // 비밀번호 수정할 값이 있으면, 비밀번호 암호화
            if(user.getUserPwd() != null) {
//                String newPwd = passwordEncoder.encode(user.getUserPwd());
                user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            }
            int jobUserUpdate = jobUserMapper.jobUserUpdate(user);
            if(jobUserUpdate == 1) {
                jobUserResponse.setUser(jobUserMapper.findOneJobUser(user.getUserId()));
                jobUserResponse.setCode("C000");
                jobUserResponse.setMessage("회원 정보 수정 완료");
            } else {
                jobUserResponse.setCode("E002");
                jobUserResponse.setMessage("변경할 정보를 입력하세요");
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

    // 회원가입시 id 중복 확인 버튼 클릭시 중복 확인 API
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


}
