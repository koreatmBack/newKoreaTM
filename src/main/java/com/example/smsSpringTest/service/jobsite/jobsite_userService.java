package com.example.smsSpringTest.service.jobsite;

import com.example.smsSpringTest.mapper.jobsite.JobCommonMapper;
import com.example.smsSpringTest.mapper.jobsite.JobUserMapper;
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
                            token = jwtTokenProvider.accessToken(authentication);
                            log.info("아직 유효한 AccessToken = " + token);
                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
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
                        jobUserResponse.setUser(jobUserMapper.findOneUser(userId));
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
            RefToken refDB = jwtTokenProvider.getUserRefToken(user.getUserId());
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

}
