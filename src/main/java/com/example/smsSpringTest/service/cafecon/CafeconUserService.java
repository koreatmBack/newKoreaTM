package com.example.smsSpringTest.service.cafecon;

import com.example.smsSpringTest.mapper.cafecon.CafeconCommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.mapper.jobsite.JobUserMapper;
import com.example.smsSpringTest.model.cafecon.CafeUser;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.cafecon.CafeconResponse;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 회원 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CafeconUserService {

    private final JobUserMapper jobUserMapper;
    private final CafeconUserMapper cafeconUserMapper;
    private final CafeconCommonMapper cafeCommonMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final CafeconCommonService cafeconCommonService;

    // 문자 본인인증 후 회원가입 (카페콘)
    public ApiResponse cafeconSignUp(CafeUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            String userPwd = user.getUserPwd();
            // 입력한 비밀번호 암호화
            user.setUserPwd(passwordEncoder.encode(userPwd));

            // 약관 필수 동의 안 하면
            if (user.getAgreeTerms().equals("N") || user.getAgreePrivacy().equals("N")) {
                // 필수 둘 중 하나라도 N 이면
                apiResponse.setCode("E004");
                apiResponse.setMessage("필수 동의 항목 체크해주세요.");
                return apiResponse;
            }

            int result = cafeconUserMapper.cafeconSignUp(user);
            if(result == 1){
                apiResponse.setCode("C000");
                apiResponse.setMessage("회원 가입이 완료되었습니다.");
            } else {
                apiResponse.setCode("E003");
                apiResponse.setMessage("회원 가입 실패 !!");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("다시 입력해주세요.");
            log.info(e.getMessage());
        }
        return apiResponse;
    }

    // 회원가입시 id 중복 확인 버튼 비동기로 확인 API
    public ApiResponse checkId(CafeUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            // 잡사이트에서 체크
            int dupChkId = cafeconUserMapper.checkId(user.getUserId());

            if(dupChkId == 0) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("사용 가능한 ID입니다.");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("이미 사용중인 ID입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!!");
        }

        return apiResponse;
    }

    // 카페콘 회원 로그인
    @Transactional
    public CafeconResponse cafeconLogin(CafeUser user) throws Exception {
        CafeconResponse cafeconResponse = new CafeconResponse();

        try {
            String userId = user.getUserId();
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 일치하는지 체크
            String dupPwd = cafeconUserMapper.dupPwd(user);
            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);
            if(isMatchPwd) {
                // 비밀번호 일치
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPwd);
                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                    log.info("authenticationToken = " + authenticationToken);
                    log.info("authentication = " + authentication);
                    log.info("auth ROLE = " + authentication.getAuthorities());

                    userId = authentication.getName();
                    log.info("userId = " + userId);

                    RefToken refToken = cafeCommonMapper.getUserRefreshTokenData(userId);
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
                            cafeCommonMapper.deleteUserToken(userId);

                            // 토큰(access, refresh) 재생성
                            token = jwtTokenProvider.generateToken(authentication);
                            log.info("만료되었을때 재생성한 토큰 = " + token);
                            refToken2.setUserId(userId);
                            refToken2.setGrantType(token.getGrantType());
                            refToken2.setRefreshToken(token.getRefreshToken());
                            cafeCommonMapper.addUserToken(refToken2);
                        } else if (now.isAfter(parseUptDate.plusDays(28))) {
                            // 현재 날짜가, uptdate + 28일보다 이후일때
                            // 토큰 재생성
                            token = jwtTokenProvider.generateToken(authentication);
                            log.info("After 28 -> token = " + token);
                            refToken2.setUserId(userId);
                            refToken2.setGrantType(token.getGrantType());
                            refToken2.setRefreshToken(token.getRefreshToken());
                            cafeCommonMapper.updateUserToken(refToken2);
                        } else {
                            // 현재 날짜가, uptdate + 28일보다 이전이면서, refresh 토큰도 유효할때
                            // 만약 쿠키에 accesstoken이 있으면 (즉, 로그인이 유효하면)
                            Cookie cookies[] = request.getCookies();

                            // 만약 쿠키가 있다면
                            if(cookies != null) {
                                String accessToken = jwtTokenProvider.extractTokenFromCookies(cookies);

                                if(StringUtils.hasText(accessToken) && userId.equals(jwtTokenProvider.getAuthentication(accessToken).getName())) {
                                    // accesstoken 이라는 쿠키가 있을때
                                    String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
                                    userId = cookieName;
                                    log.info("아직 유효한 cookie = " + accessToken);
                                    Long accessTokenExpiration = jwtTokenProvider.getExpiration(accessToken);
                                    log.info("cookie 유효기간 seconds = " + accessTokenExpiration / 1000);

                                    CafeUser user2 = cafeconUserMapper.findOneCafeconLoginUser(userId);
                                    user2.setRole("cafecon");
                                    cafeconResponse.setUser(user2);
                                    log.info("로그인 상태에서 또 로그인시 user = " + user2);
                                    cafeconResponse.setCode("C000");
                                    String userName = jobUserMapper.userName(userId);
                                    cafeconResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                            accessTokenExpiration/1000 + "초 남았습니다.");
                                    return cafeconResponse;
                                }
                            }

                            token = jwtTokenProvider.accessToken(authentication);
                            log.info("새로 생성한 AccessToken = " + token);
                        }
                    } else {
                        // refresh token이 null

                        // 토큰 재생성
                        token = jwtTokenProvider.generateToken(authentication);
                        log.info("refToken이 null일때 token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        cafeCommonMapper.addUserToken(refToken2);
                    }

                    if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                        // 최종적으로 Access token이 있을때
                        CafeUser user2 = cafeconUserMapper.findOneCafeconLoginUser(userId);
                        user2.setRole("cafecon");
                        log.info("로그인 성공시 user = " + user2);
                        cafeconResponse.setUser(user2);
                        String userName = user2.getManagerName();
                        cafeconResponse.setCode("C000");
                        cafeconResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                        response.addCookie(cookie);
                    } else {
                        // 최종적으로 access 토큰이 없을때
                        cafeconResponse.setCode("E001");
                        cafeconResponse.setMessage("최종적으로 Access Token이 없습니다.");
                        log.info("ACCESS TOKEN 없음");
                    }
                } catch (BadCredentialsException e) {
                    cafeconResponse.setCode("E003");
                    cafeconResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                    log.info(e.getMessage());
                }
            } else {
                // id 값이 없거나(다르거나), 비밀번호 일치 X
                cafeconResponse.setCode("E005");
                cafeconResponse.setMessage("로그인 실패, 로그인 정보가 일치하지 않습니다.");
            }


        } catch (Exception e) {
            log.info("로그인 중 예외 발생: {}", e.getMessage(), e);
            cafeconResponse.setCode("E001");
            cafeconResponse.setMessage("ERROR!!!");
        }
        return cafeconResponse;
    }

    // 카페콘 회원 로그아웃
    public ApiResponse cafeconLogout() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        CafeUser user = new CafeUser();

        try {
            Cookie cookies[] = request.getCookies();
            String accessToken = "";

            // 만약 쿠키가 있다면
            for(Cookie cookie : cookies) {
                if("accesstoken".equals(cookie.getName())){
                    accessToken = cookie.getValue();
                }
            }

            // 쿠키가 없다면
            if(!StringUtils.hasText(accessToken)){
                apiResponse.setCode("E401");
                apiResponse.setMessage("로그인 상태가 아닙니다.");
                return apiResponse;
            }

            // AccessToken 검증
            if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
                //AccessToken에서 authentication 가져오기
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                user.setUserId(authentication.getName());
                log.info("로그아웃할 id = " + user.getUserId());

                for(Cookie cookie : cookies) {
                    if(accessToken.equals(cookie.getValue())) {
                        // 해당 쿠키 삭제
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        log.info("로그아웃시 쿠키 삭제 완료");
                        break;
                    }
                }
                // db속 refresh token 가져오기
                RefToken refDB = cafeconCommonService.getUserRefToken(user.getUserId());
                log.info("refDb = " + refDB);
                if (refDB.getRefreshToken() != null) {
                    // refresh 토큰 삭제
                    int deleteRefreshToken = cafeCommonMapper.deleteUserToken(user.getUserId());
                    if (deleteRefreshToken == 1) {
                        log.info("refresh 토큰 삭제");
                    } else {
                        log.info("refresh 토큰 삭제 x ");
                    }
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("로그아웃 되었습니다.");
                }
            } else {
                apiResponse.setCode("E401");
                apiResponse.setMessage("유효하지 않은 접근입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(" Error !!! ");
            log.info("로그아웃시 error : " + e.getMessage());
        }

        return apiResponse;
    }


    // 카페콘 회원 비밀번호 변경
    public ApiResponse changePwd(CafeUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {

            // 입력받은 비밀번호
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = cafeconUserMapper.dupPwd(user);

            // 비밀번호 일치하는지 검증
            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);

            if (isMatchPwd) {
                // 비밀번호 일치하면

                // 입력받은 새로운 비밀번호
                String newPwd = user.getUserNewPwd();
                user.setUserPwd(passwordEncoder.encode(newPwd));
                int changePwd = cafeconUserMapper.changePwd(user);
                if(changePwd == 1) {
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("비밀번호 변경 성공");
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

    // 카페콘 회원 정보 수정
    @Transactional
    public CafeconResponse cafeconUserUpdate(CafeUser user) throws Exception {
        CafeconResponse cafeconResponse = new CafeconResponse();

        try {
            String userPwd = user.getUserPwd();

            // 암호화된 비밀번호 체크
            String dupPwd = cafeconUserMapper.dupPwd(user);

            boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPwd);
            if(!isMatchPwd) {
                cafeconResponse.setCode("C003");
                cafeconResponse.setMessage("비밀번호가 일치하지 않습니다");
                return cafeconResponse;
            }
            int updateCafeconUser = cafeconUserMapper.updateCafeconUser(user);
            if(updateCafeconUser == 1) {
                cafeconResponse.setUser(cafeconUserMapper.findOneCafUser(user.getUserId()));
                cafeconResponse.setCode("C000");
                cafeconResponse.setMessage("회원 정보 수정 완료");
            } else {
                cafeconResponse.setCode("C001");
                cafeconResponse.setMessage("수정 실패");
            }
        } catch (Exception e){
                cafeconResponse.setCode("E001");
                cafeconResponse.setMessage("Error!!!");
                log.info(e.getMessage());
        }
        return cafeconResponse;
    }

    // 회원 포인트 수정 (지급 , 차감)
    public ApiResponse updatePoint(CafeUser user) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int updatePoint = cafeconUserMapper.updatePoint(user);
            if(updatePoint == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("포인트 수정 완료");
            } else {
                apiResponse.setCode("C003");
                apiResponse.setMessage("포인트 수정 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }
        return apiResponse;
    }









}
