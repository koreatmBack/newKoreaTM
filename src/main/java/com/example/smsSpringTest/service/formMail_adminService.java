package com.example.smsSpringTest.service;

import com.example.smsSpringTest.entity.FormMailAdminEntity;
import com.example.smsSpringTest.mapper.AdminMapper;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.mapper.jobsite.JobUserMapper;
import com.example.smsSpringTest.model.FormMailAdmin;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.AdminResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.RefResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * date : 2024-09-20
 * comment : 회원 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class formMail_adminService {

    private final CommonMapper commonMapper;
    private final AdminMapper adminMapper;
    private final JobUserMapper jobUserMapper;
    private final CafeconUserMapper cafeconUserMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

//    private final RedisTemplate<String, Object> redisTemplate;

    // 회원 등록
    @Transactional
    public ApiResponse signUp(FormMailAdmin admin) throws Exception {
        log.info("admin = ? " + admin);
        ApiResponse apiResponse = new ApiResponse();

        try{


            // 폼메일에서 체크
            int formCheckId = jobUserMapper.dupFormMailIdCheck(admin.getUserId());


            if(formCheckId == 1) {
                apiResponse.setCode("E002");
                apiResponse.setMessage("(폼메일) 이미 사용중인 ID입니다.");
                return apiResponse;
            }


                // id 중복 없음

                // 비밀번호 암호화
                admin.setUserPwd(passwordEncoder.encode(admin.getUserPwd()));

                int result = adminMapper.signUp(admin);

                if (result == 1) {
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("회원 등록이 완료되었습니다.");
                } else {
                    apiResponse.setCode("E003");
                    apiResponse.setMessage("회원 등록 실패 !!");
                }

        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!! Id와 pw를 다시 입력하세요.");
            throw new RuntimeException(e);
        }

        return apiResponse;
    }


    // jwt 로그인
    @Transactional
    public AdminResponse logIn(FormMailAdminEntity user) throws Exception {

        AdminResponse adminResponse = new AdminResponse();

        try{
            String userId = user.getUserId();

            // 아이디 있는지 확인
            int dupChkId = adminMapper.userDuplicatedChkId(userId);
            if(dupChkId == 0){
                // 등록된 아이디 없음
                adminResponse.setCode("E004");
                adminResponse.setMessage("등록된 id가 없습니다.");

                if(userId == null){
                    adminResponse.setCode("E004");
                    adminResponse.setMessage("ID를 입력해주세요.");
                }

            } else {
                // 아이디 존재

                // 입력 받은 PW
                String userPwd = user.getUserPwd();

                // 입력받은 ID로 PW 체크 ( 등록된 비밀번호 체크 )
                String dupPw = adminMapper.userPassword(userId);

                // 비밀번호 검증
                boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPw);

                if(isMatchPwd){
                    // 입력한 비밀번호와 등록된 비밀번호가 같을때
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("ADMIN:"+ userId, userPwd);
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
                            token = jwtTokenProvider.AdminGenerateToken(authentication);
                            log.info("만료되었을때 재생성한 토큰 = " + token);
                            refToken2.setUserId(userId);
                            refToken2.setGrantType(token.getGrantType());
                            refToken2.setRefreshToken(token.getRefreshToken());
                            commonMapper.addUserToken(refToken2);
                        } else if (now.isAfter(parseUptDate.plusDays(28))) {
                            // 현재 날짜가, uptdate + 28일보다 이후일때
                            // 토큰 재생성
                            token = jwtTokenProvider.AdminGenerateToken(authentication);
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
                                if(StringUtils.hasText(accessToken) && userId.equals(jwtTokenProvider.getAuthentication(accessToken).getName())) {
                                    // accesstoken 이라는 쿠키가 있을때
                                    String cookieName = jwtTokenProvider.getAuthentication(accessToken).getName();
                                    userId = cookieName;
                                    log.info("아직 유효한 cookie = " + accessToken);
                                    Long accessTokenExpiration = jwtTokenProvider.getExpiration(accessToken);
                                    log.info("cookie 유효기간 seconds = " + accessTokenExpiration / 1000);

                                    FormMailAdmin user2 = adminMapper.findOneAdmin(userId);

                                    adminResponse.setFormMailAdmin(user2);
                                    log.info("로그인 상태에서 또 로그인시 user = " + user2);
                                    adminResponse.setCode("C000");
                                    String userName = adminMapper.userName(userId);
                                    adminResponse.setMessage(userName + "님 현재 로그인 상태입니다. 로그인 만료까지" +
                                            accessTokenExpiration/1000 + "초 남았습니다.");
                                    return adminResponse;
                                }
                            }

                            token = jwtTokenProvider.AdminAccessToken(authentication);
                            log.info("아직 유효한 AccessToken = " + token);
                            Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
                            log.info("accessToken 유효기간 밀리 seconds = " + accessTokenExpiration);
                        }
                    } else {
                        // refresh token이 null

                        // 토큰 재생성
                        token = jwtTokenProvider.AdminGenerateToken(authentication);
                        log.info("refToken이 null일때 token = " + token);
                        refToken2.setUserId(userId);
                        refToken2.setGrantType(token.getGrantType());
                        refToken2.setRefreshToken(token.getRefreshToken());
                        commonMapper.addUserToken(refToken2);
                    }

                    log.info("token : {}", token.getAccessToken());

                    if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                        // 최종적으로 Access token이 있을때
//                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));
                        FormMailAdmin user2 = adminMapper.findOneAdmin(userId);

                        adminResponse.setFormMailAdmin(user2);
                        String userName = adminMapper.userName(userId);
                        adminResponse.setCode("C000");
                        adminResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
//                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());

                        // 관리자용 쿠키 생성하기
                        Cookie cookie = jwtTokenProvider.createCookieAdmin(token.getAccessToken());
                        response.addCookie(cookie);
                    } else {
                        // 최종적으로 access 토큰이 없을때
                        adminResponse.setCode("E001");
                        adminResponse.setMessage("최종적으로 Access Token이 없습니다.");
                    }

                } catch (BadCredentialsException e){
                    adminResponse.setCode("E003");
                    adminResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                }

                } else {
                    // 입력한 비밀번호와 등록된 비밀번호가 다를때
                    adminResponse.setCode("E005");
                    adminResponse.setMessage("로그인 실패, 비밀번호 다시 확인해주세요");
                }

            }

        }catch (Exception e){
            log.error("로그인 중 예외 발생: {}", e.getMessage(), e);
            adminResponse.setCode("E001");
            adminResponse.setMessage("비밀번호를 입력해주세요.");
        }

        return adminResponse;
    }

    //jwt 로그아웃 -> 로그아웃시 리프레쉬 토큰, 해당 쿠키도 삭제
    public ApiResponse logout() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        FormMailAdminEntity user = new FormMailAdminEntity();

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
            RefToken refDB = getUserRefToken(user.getUserId());
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


    // 전체 회원 목록
    public AdminResponse adminList(FormMailAdmin admin) {

        AdminResponse adminResponse = new AdminResponse();

//        // 캐시 키
//        String cacheKey = "userList_" + paging.getPage() + "_" + paging.getSize();
//        long cacheTime = 1000 * 60 * 60; // 만료시간 1시간.

        // Redis에서 데이터가 있는지 확인
//        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//            // Redis에서 데이터 가져오기
//            userResponse = (UserResponse) redisTemplate.opsForValue().get(cacheKey);
//            log.info("Redis에서 회원 목록을 조회했습니다.");
//        } else {

        // 로그인 유저인지 체크
        Cookie cookies[] = request.getCookies();
        String accessToken = "";
//            CafeUser user = new CafeUser();
        String userId = "";
        // 만약 쿠키가 있다면
        for(Cookie cookie : cookies) {
            if("accesstoken".equals(cookie.getName())){
                accessToken = cookie.getValue();
            }
        }

        // 쿠키가 없다면
        if(!StringUtils.hasText(accessToken)){
            adminResponse.setCode("E401");
            adminResponse.setMessage("로그인 상태가 아닙니다.");
            return adminResponse;
        }

        // AccessToken 검증
        if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
            //AccessToken에서 authentication 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            userId = authentication.getName();
        }


            int page = admin.getPage(); // 현재 페이지
            int size = admin.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = adminMapper.getUserListCount(admin);

        admin.setOffset(offset);

            adminResponse.setAdminList(adminMapper.adminProfileList(admin));
//            log.info(adminMapper.adminProfileList(paging).toString());

//            log.info("adminResponse :  page = " + page + ", size = " + size + ", offset = " + offset + ", totalCount = " + totalCount);

            String role = adminMapper.findOneAdmin(userId).getRole();

            if("SUBADMIN".equals(role)) {
                // 회원이랑 team이 일치한 데이터 목록만 리턴
                String team = adminMapper.findOneAdmin(userId).getTeam();
                admin.setTeam(team);
                adminResponse.setAdminList(adminMapper.teamList(admin));
                totalCount = adminMapper.teamListCount(team);
            }
//            else if ("ADMIN".equals(role) || "TOTALADMIN".equals(role)) {
//            // 부서 검색 기능
//            if(StringUtils.hasText(admin.getTeam())){
//
//            }
//        }

            if (adminResponse.getAdminList() != null && !adminResponse.getAdminList().isEmpty()) {
                // 비어있지 않을 때
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("adminResponse :  totalPages = " + totalPages);
                adminResponse.setTotalPages(totalPages);
                adminResponse.setCode("C000");
                adminResponse.setMessage("관리자 목록 조회 완료");

//                // Redis에 데이터 저장
//                redisTemplate.opsForValue().set(cacheKey, userResponse, cacheTime, TimeUnit.MILLISECONDS);
//                log.info("회원 목록을 Redis에 캐싱했습니다.");
            } else {
                // 비어있을 때
                adminResponse.setCode("E001");
                adminResponse.setMessage("조회된 계정이 없습니다.");
            }
//        }
        return adminResponse;
    }

    // 회원 한명 정보 반환
    public AdminResponse findOneAdmin(FormMailAdmin admin) throws Exception{

        AdminResponse adminResponse = new AdminResponse();
        log.info("회원 정보 : " + admin);
        try {
            String userId = admin.getUserId();
            adminResponse.setFormMailAdmin(adminMapper.findOneAdmin(userId));
            adminResponse.setCode("C000");
            adminResponse.setMessage("조회 성공");
        } catch (Exception e) {
            adminResponse.setCode("E001");
            adminResponse.setMessage("다시 조회해주세요.");
        }

        return adminResponse;
    }


    // 회원 이름 검색시 해당 회원들 정보 반환
    public AdminResponse findAdmins(FormMailAdmin admin) throws Exception {
        AdminResponse adminResponse = new AdminResponse();

        try {
            String userName = admin.getKeyword();
            log.info("userName = " + userName);
//            userResponse.setUserList(userMapper.findUsers(user));
            adminResponse.setAdminList(adminMapper.findAdmins(userName));
            log.info(adminResponse.getAdminList().toString());
            if(!adminResponse.getAdminList().isEmpty() && adminResponse.getAdminList() != null) {
                adminResponse.setCode("C000");
                adminResponse.setMessage("이름 검색 성공");
            } else {
                adminResponse.setCode("E004");
                adminResponse.setMessage("이름 검색 실패");
            }
        } catch (Exception e) {
            adminResponse.setCode("E001");
            adminResponse.setMessage("에러");
            log.info(e.getMessage());
        }

        return adminResponse;
    }


    // 회원 정보 수정
    public AdminResponse updateAdmin(FormMailAdmin admin) throws Exception {

        AdminResponse adminResponse = new AdminResponse();

        try {
            log.info("서비스 유저 테스트 = "+String.valueOf(admin));
            String userId = admin.getUserId();
            if(userId == null) {
                // 아이디가 없을때
                adminResponse.setCode("E001");
                adminResponse.setMessage("아이디를 입력해주세요");
            } else {
                // 아이디가 있을때

                // 비밀번호가 null이 아니면 암호화
                if (admin.getUserPwd() != null) {
                    String encodedPassword = passwordEncoder.encode(admin.getUserPwd());
                    admin.setUserPwd(encodedPassword); // 암호화된 비밀번호로 설정
                }

                // 0 = 업데이트 실패, 1 = 업데이트 성공
             int updateUser = adminMapper.updateAdmin(admin);


             if(updateUser == 0){
                 adminResponse.setCode("E003");
                 adminResponse.setMessage("유저 업데이트 실패");
             } else {
                 adminResponse.setCode("C000");
                 adminResponse.setMessage("유저 업데이트 성공");

                 String pattern = "userList_*"; // 패턴 정의
//                 Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기

//                 redisTemplate.delete(keys); // 전체 회원 목록 캐시를 삭제
             }

            }
        } catch (Exception e) {
            adminResponse.setCode("E001");
            adminResponse.setMessage("ERROR!!!");
            log.info(e.getMessage());
        }

        return adminResponse;
    }

    // 업무용 연락처 등록
    public ApiResponse addPhoneNum(String phoneNumber) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try{
            // 입력받은 연락처
            log.info("입력받은 연락처 = " + phoneNumber);
            if(isValidPhoneNumber(phoneNumber)){
                // 형식이 일치하면
                int validPhoneChk = adminMapper.validPhoneChk(phoneNumber);
                if(validPhoneChk == 1){
                    // 연락처가 중복이면
                    apiResponse.setCode("E004");
                    apiResponse.setMessage("이미 등록된 연락처입니다.");
                } else {
                    // 연락처가 중복 아닐때
                    adminMapper.addPhoneNum(phoneNumber);
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("연락처가 저장되었습니다.");
                }
            } else {
                // 형식이 일치하지 않으면
                apiResponse.setCode("E002");
                apiResponse.setMessage("연락처 형식이 올바르지 않습니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("연락처를 입력해주세요.");
        }
        return apiResponse;
    }

    // 연락처 형식 일치하는지
    public boolean isValidPhoneNumber(String phoneNumber) {
        // 숫자와 대시(-)만 허용하고, 앞자리가 2~3자리, 중간 3~4자리, 마지막 4자리 숫자
        String regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        return phoneNumber.matches(regex);
    }

    // 업무용 연락처 모두 조회
    public AdminResponse allPhoneNumList(){

        AdminResponse adminResponse = new AdminResponse();

        adminResponse.setPhoneNumList(adminMapper.allPhoneNumList());
        adminResponse.setCode("C000");
        adminResponse.setMessage("조회 성공");

        return adminResponse;
    }

    // 업무용 연락처 삭제
    public ApiResponse delPhoneNum(String phoneNumber) {

        ApiResponse apiResponse = new ApiResponse();

            if(phoneNumber == null){
                apiResponse.setCode("E003");
                apiResponse.setMessage("삭제할 연락처를 입력해주세요.");
            } else {
                int delPhoneNum = adminMapper.delPhoneNum(phoneNumber);
                if(delPhoneNum == 1){
                    apiResponse.setCode("C000");
                    apiResponse.setMessage("연락처가 삭제되었습니다.");
                } else {
                    apiResponse.setCode("E001");
                    apiResponse.setMessage("등록되지 않은 연락처입니다.");
                }
            }
        return apiResponse;
    }

    // 입력받은 업무용 연락처로 회원 db에서 번호 일치하는 회원 이름, 포지션 찾기
    public AdminResponse findUserName(String phoneNumber) throws Exception {
        AdminResponse adminResponse = new AdminResponse();
        try{
            if(phoneNumber == null){
                // 연락처 입력 못 받았을 때
                adminResponse.setCode("E002");
                adminResponse.setMessage("조회할 연락처를 입력해주세요.");
            } else {
                // 연락처 입력 받음

                adminResponse.setFindUserList(adminMapper.findUserList(phoneNumber));
                adminResponse.setCode("C000");
                adminResponse.setMessage("조회 성공");
            }
        } catch (Exception e) {
            adminResponse.setCode("E001");
            adminResponse.setMessage("등록되지 않은 연락처입니다.");
        }
        return adminResponse;
    }

    // 토큰 재발급 요청
    public RefResponse reissuToken(RefToken refToken) throws Exception {

        RefResponse refResponse = new RefResponse();

        if(refToken.getRefreshToken() != null && !refToken.getRefreshToken().equals("")) {
            int refCnt = commonMapper.getRefreshTokenCount(refToken);

            if (refCnt == 1) {
                RefToken dbRefToken = commonMapper.getRefreshTokenData(refToken);
                Long remainingMilliseconds = jwtTokenProvider.getExpiration(dbRefToken.getRefreshToken());

                if (dbRefToken.getRefreshToken() != null && !dbRefToken.getRefreshToken().isBlank()) {
                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
                        refResponse.setCode("E999");
                        refResponse.setMessage("로그아웃");
                    } else if (refToken.getRefreshToken().equals(dbRefToken.getRefreshToken())) {
                        String resolveToken = refToken.getResolveToken();
                        String bearer_prefix = "bearer ";
                        log.info("resolve Token = " + resolveToken);
                        if (StringUtils.hasText(resolveToken) && resolveToken.startsWith(bearer_prefix)) {
                            resolveToken = resolveToken.substring(7);
                        }

                        Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        Token token = jwtTokenProvider.accessToken(authentication);
//                        response.setHeader(HttpHeaders.AUTHORIZATION, "bearer " + token.getAccessToken());
                        Cookie cookie = jwtTokenProvider.createCookieAdmin(token.getAccessToken());
                        response.addCookie(cookie);

                        log.info("새로 발급받은 access Token = " + token.getAccessToken());
                        refResponse.setRefToken(dbRefToken);
                        refResponse.setCode("C000");
                        refResponse.setMessage("재발급");
                    }
                } else {
                    refResponse.setCode("E999");
                    refResponse.setMessage("로그아웃");
                }
            } else {
                refResponse.setCode("E999");
                refResponse.setMessage("로그아웃");
            }
        } else {
            refResponse.setCode("E999");
            refResponse.setMessage("로그아웃");
        }

        return refResponse;
    }

    // access 토큰 재생성 -> 쿠키에 다시 담아주기 ( 쿠키 갱신하는법 )
    public ApiResponse reissuAccessToken() throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        try {
            // 쿠키 찾기
            Cookie[] cookies = request.getCookies();

            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);

            // 만약 쿠키 없으면
            if(cookieToken == null){
                apiResponse.setCode("E004");
                apiResponse.setMessage("쿠키가 없습니다. 로그인이 필요합니다.");
                return apiResponse;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(cookieToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Token token = jwtTokenProvider.accessToken(authentication);
//                        response.setHeader(HttpHeaders.AUTHORIZATION, "bearer " + token.getAccessToken());
            Cookie cookie = jwtTokenProvider.createCookieAdmin(token.getAccessToken());
            response.addCookie(cookie);

            log.info("새로 발급받은 access Token = " + token.getAccessToken());
            apiResponse.setCode("C000");
            apiResponse.setMessage("access token 및 쿠키 재발급 성공");
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("ERROR!!");
        }

        return apiResponse;
    }

    // 로그인시, 쿠키에 유효한 accessToken이 있으면 비밀번호 따로 입력 안 해도 자동 로그인


    // 쿠키 찾아온 후, 만료 시간 반환
    public AccessResponse experCookie() throws Exception {
        AccessResponse accessResponse = new AccessResponse();

        try {
            // 쿠키 찾기
            Cookie[] cookies = request.getCookies();

            String cookieToken = jwtTokenProvider.extractTokenFromCookies(cookies);

            // 만약 쿠키 없으면
            if(cookieToken == null){
                accessResponse.setCode("E004");
                accessResponse.setMessage("쿠키가 없습니다. 로그인이 필요합니다.");
                return accessResponse;
            }

            Long limit = jwtTokenProvider.getExpiration(cookieToken) / 1000; // 초로 변환
            accessResponse.setCode("C000");
            accessResponse.setMessage(Math.toIntExact(limit) + "초 남았습니다.");
            accessResponse.setLimit(Math.toIntExact(limit));
        } catch (Exception e){
            accessResponse.setCode("E001");
            accessResponse.setMessage("error!!");
            log.info(e.getMessage());
        }

        return accessResponse;
    }


    // DB 에 저장되어 있는 RefreshToken 조회
    public RefToken getUserRefToken(String userId) {

        RefToken refToken = new RefToken();

        if(userId != null && !userId.equals("")) {
            int result = commonMapper.getUserRefreshTokenCount(userId);

            if(result == 1) {
                refToken = commonMapper.getUserRefreshTokenData(userId);
                refToken.setCode("C000");
                refToken.setMessage("조회 완료");
            } else {
                refToken.setCode("E001");
                refToken.setMessage("권한 정보가 없습니다.");
            }

        } else {
            refToken.setCode("E001");
            refToken.setMessage("권한 정보가 없습니다.");
        }

        return refToken;
    }

    // 폼메일 회원 삭제
    public ApiResponse deleteOne(FormMailAdmin admin) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int deleteOne = adminMapper.deleteOne(admin);
            if(deleteOne == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("삭제 완료");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("삭제 실패");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }

        return apiResponse;
    }

}

