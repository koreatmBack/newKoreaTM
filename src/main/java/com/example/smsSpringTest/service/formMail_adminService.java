package com.example.smsSpringTest.service;

import com.example.smsSpringTest.entity.UserProfile;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.UserMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.RefResponse;
import com.example.smsSpringTest.model.response.UserResponse;
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
import org.springframework.web.bind.annotation.PostMapping;

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
    private final UserMapper userMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

//    private final RedisTemplate<String, Object> redisTemplate;

    // 회원 등록
    @Transactional
    public ApiResponse signUp(UserProfile user) throws Exception {
        log.info("USER = ? " + user);
        ApiResponse apiResponse = new ApiResponse();

        try{
            // id 중복체크 ( 값이 0 -> 중복 없음 )
            int dupChkId = userMapper.userDuplicatedChkId(user.getUserId());
            log.info("중복 값 = " + dupChkId);
            if(dupChkId == 0) {
                // id 중복 없음

                // 비밀번호 암호화
                user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));

                int result = userMapper.signUp(user);

                if (result == 1) {

                    apiResponse.setCode("C000");
                    apiResponse.setMessage("회원 등록이 완료되었습니다.");
                } else {
                    apiResponse.setCode("E003");
                    apiResponse.setMessage("회원 등록 실패 !!");
                }

            } else {
                //id 중복 있음
                apiResponse.setCode("E002");
                apiResponse.setMessage("등록 불가 ! 이미 등록된 ID입니다.");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage(e.getMessage());
            throw new RuntimeException(e);
        }

        return apiResponse;
    }

//    // 로그인
//    @Transactional
//    public UserResponse logIn(UserProfile user) throws Exception {
//
//        UserResponse userResponse = new UserResponse();
//
//        try{
//            String userId = user.getUserId();
//
//            // 아이디 있는지 확인
//            int dupChkId = userMapper.userDuplicatedChkId(userId);
//            if(dupChkId == 0){
//                // 등록된 아이디 없음
//                userResponse.setCode("E004");
//                userResponse.setMessage("등록된 id가 없습니다.");
//
//                if(userId == null){
//                    userResponse.setCode("E004");
//                    userResponse.setMessage("ID를 입력해주세요.");
//                }
//
//            } else {
//                // 아이디 존재
//
//                // 입력 받은 PW
//                String userPwd = user.getUserPwd();
//
//                // 입력받은 ID로 PW 체크 ( 등록된 비밀번호 체크 )
//                String dupPw = userMapper.userPassword(userId);
//
//                // 비밀번호 검증
//                boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPw);
//
//                if(isMatchPwd){
//                    // 입력한 비밀번호와 등록된 비밀번호가 같을때
//                    String userName = userMapper.userName(userId);
//                    log.info("userId = " + userId);
//                    log.info("userPwd = " + userPwd);
//                    log.info("로그인 유저 정보 = " + userMapper.user(userId));
//                    userResponse.setUser(userMapper.user(userId));
////                    userResponse.setUserProfile(userMapper.userProfile(userId));
//                    userResponse.setCode("C000");
//                    userResponse.setMessage("로그인 성공! " + userName+"님 환영합니다.");
//
//
//                    // Redis에 로그인 ID 저장 (1시간 만료 시간 설정)
//                    String sessionKey = "login_" + userId;
//
////                    // Redis에서 로그인 정보 조회
////                    String redisUserId = (String) redisTemplate.opsForValue().get(sessionKey);
//
////                    if (redisUserId != null) {
////                        log.info("Redis에서 로그인 정보를 조회했습니다: " + redisUserId);
////                        // Redis에서 남은 TTL 확인
////                        Long ttl = redisTemplate.getExpire(sessionKey, TimeUnit.SECONDS);
////                        log.info("만료까지 남은 시간은 : " + ttl+"초 입니다.");
////                    } else {
////                       // Redis에 로그인 ID 저장 (1시간 만료 시간 설정)
////                        redisTemplate.opsForValue().set(sessionKey, userId, 1, TimeUnit.HOURS);
////                        log.info("redis에 저장 성공");
////                    }
//
//                } else {
//                    // 입력한 비밀번호와 등록된 비밀번호가 다를때
//                    userResponse.setCode("E005");
//                    userResponse.setMessage("로그인 실패, 비밀번호 다시 확인해주세요");
//                }
//
//            }
//
//        }catch (Exception e){
//            log.error("로그인 중 예외 발생: {}", e.getMessage(), e);
//            userResponse.setCode("E001");
//            userResponse.setMessage("비밀번호를 입력해주세요.");
//        }
//
//        return userResponse;
//    }

//    // 로그아웃
//    public ApiResponse logOut(UserProfile user) throws Exception {
//        ApiResponse apiResponse = new ApiResponse();
//        try{
//            String userId = user.getUserId();
//            String sessionKey = "login_" + userId;
////            redisTemplate.delete(sessionKey); // Redis에서 해당 세션 키 삭제
////            log.info("로그아웃 성공. Redis에서 세션 키가 삭제되었습니다.");
//
//            apiResponse.setCode("C000");
//            apiResponse.setMessage("로그아웃 성공");
//        } catch (Exception e) {
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("로그아웃 에러");
//        }
//        return apiResponse;
//    }

    // jwt 로그인
    @Transactional
    public UserResponse logIn(UserProfile user) throws Exception {

        UserResponse userResponse = new UserResponse();

        try{
            String userId = user.getUserId();

            // 아이디 있는지 확인
            int dupChkId = userMapper.userDuplicatedChkId(userId);
            if(dupChkId == 0){
                // 등록된 아이디 없음
                userResponse.setCode("E004");
                userResponse.setMessage("등록된 id가 없습니다.");

                if(userId == null){
                    userResponse.setCode("E004");
                    userResponse.setMessage("ID를 입력해주세요.");
                }

            } else {
                // 아이디 존재

                // 입력 받은 PW
                String userPwd = user.getUserPwd();

                // 입력받은 ID로 PW 체크 ( 등록된 비밀번호 체크 )
                String dupPw = userMapper.userPassword(userId);

                // 비밀번호 검증
                boolean isMatchPwd = passwordEncoder.matches(userPwd, dupPw);

                if(isMatchPwd){
                    // 입력한 비밀번호와 등록된 비밀번호가 같을때
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPwd);
                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                    log.info("authenticationToken = " + authenticationToken);
                    log.info("authentication = " + authentication);

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

                    log.info("token : {}", token.getAccessToken());

                    if (token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
                        // 최종적으로 Access token이 있을때
                        userResponse.setUserProfile(commonMapper.getFrontUserProfile(userId));

                        String userName = userMapper.userName(userId);
                        userResponse.setCode("C000");
                        userResponse.setMessage("로그인 성공! " + userName + "님 환영합니다.");
                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
                        response.addCookie(cookie);
                    } else {
                        // 최종적으로 access 토큰이 없을때
                        userResponse.setCode("E001");
                        userResponse.setMessage("최종적으로 Access Token이 없습니다.");
                    }

                } catch (BadCredentialsException e){
                    userResponse.setCode("E003");
                    userResponse.setMessage("아이디 또는 비밀번호를 확인해주세요.");
                }
//                    String userName = userMapper.userName(userId);
//                    log.info("userId = " + userId);
//                    log.info("userPwd = " + userPwd);
//                    log.info("로그인 유저 정보 = " + userMapper.user(userId));
//                    userResponse.setUser(userMapper.user(userId));
////                    userResponse.setUserProfile(userMapper.userProfile(userId));
//                    userResponse.setCode("C000");
//                    userResponse.setMessage("로그인 성공! " + userName+"님 환영합니다.");

                } else {
                    // 입력한 비밀번호와 등록된 비밀번호가 다를때
                    userResponse.setCode("E005");
                    userResponse.setMessage("로그인 실패, 비밀번호 다시 확인해주세요");
                }

            }

        }catch (Exception e){
            log.error("로그인 중 예외 발생: {}", e.getMessage(), e);
            userResponse.setCode("E001");
            userResponse.setMessage("비밀번호를 입력해주세요.");
        }

        return userResponse;
    }

    //jwt 로그아웃 -> 로그아웃시 리프레쉬 토큰, 해당 쿠키도 삭제
    public ApiResponse logout() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        UserProfile user = new UserProfile();

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


    // 전체 회원 목록
    public UserResponse userList(Paging paging) {

        UserResponse userResponse = new UserResponse();

        // 캐시 키
        String cacheKey = "userList_" + paging.getPage() + "_" + paging.getSize();
        long cacheTime = 1000 * 60 * 60; // 만료시간 1시간.

        // Redis에서 데이터가 있는지 확인
//        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//            // Redis에서 데이터 가져오기
//            userResponse = (UserResponse) redisTemplate.opsForValue().get(cacheKey);
//            log.info("Redis에서 회원 목록을 조회했습니다.");
//        } else {

            int page = paging.getPage(); // 현재 페이지
            int size = paging.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = userMapper.getUserListCount();

            paging.setOffset(offset);

            userResponse.setUserList(userMapper.userProfileList(paging));
            log.info(userMapper.userProfileList(paging).toString());

            log.info("userResponse :  page = " + page + ", size = " + size + ", offset = " + offset + ", totalCount = " + totalCount);

            if (userResponse.getUserList() != null && !userResponse.getUserList().isEmpty()) {
                // 비어있지 않을 때
                int totalPages = (int) Math.ceil((double) totalCount / size);
                log.info("userResponse :  totalPages = " + totalPages);
                userResponse.setTotalPages(totalPages);
                userResponse.setCode("C000");
                userResponse.setMessage("회원 목록 조회 완료");

//                // Redis에 데이터 저장
//                redisTemplate.opsForValue().set(cacheKey, userResponse, cacheTime, TimeUnit.MILLISECONDS);
//                log.info("회원 목록을 Redis에 캐싱했습니다.");
            } else {
                // 비어있을 때
                userResponse.setCode("E001");
                userResponse.setMessage("조회된 계정이 없습니다.");
            }
//        }
        return userResponse;
    }

    // 회원 한명 정보 반환
    public UserResponse findOneUser(UserProfile user) throws Exception{

        UserResponse userResponse = new UserResponse();
        log.info("회원 정보 : " + user);
        try {
            String userId = user.getUserId();
            userResponse.setUserProfile(userMapper.findOneUser(userId));
            userResponse.setCode("C000");
            userResponse.setMessage("조회 성공");
        } catch (Exception e) {
            userResponse.setCode("E001");
            userResponse.setMessage("다시 조회해주세요.");
        }

        return userResponse;
    }


    // 회원 이름 검색시 해당 회원들 정보 반환
    @PostMapping("/findUsers")
    public UserResponse findUsers(String searchKeyword) throws Exception {
        UserResponse userResponse = new UserResponse();

        try {
            String userName = searchKeyword.toString();
            log.info("userName = " + userName);
//            userResponse.setUserList(userMapper.findUsers(user));
            userResponse.setUserList(userMapper.findUsers(userName));
            log.info(userResponse.getUserList().toString());
            if(!userResponse.getUserList().isEmpty()) {
                userResponse.setCode("C000");
                userResponse.setMessage("이름 검색 성공");
            } else {
                userResponse.setCode("E004");
                userResponse.setMessage("이름 검색 실패");
            }

        } catch (Exception e) {
            userResponse.setCode("E001");
            userResponse.setMessage("에러");
            log.info(e.getMessage());
        }

        return userResponse;
    }


    // 회원 정보 수정
    public UserResponse updateUser(UserProfile user) throws Exception {

        UserResponse userResponse = new UserResponse();

        try {
            log.info("서비스 유저 테스트 = "+String.valueOf(user));
            String userId = user.getUserId();
            if(userId == null) {
                // 아이디가 없을때
                userResponse.setCode("E001");
                userResponse.setMessage("아이디를 입력해주세요");
            } else {
                // 아이디가 있을때
                // 기존 유저 정보
                UserProfile originUser = userMapper.userProfile(user.getUserId());

                log.info("기존 유저 정보 !! = "+originUser.toString());

                // 비밀번호가 null이 아니면 암호화
                if (user.getUserPwd() != null) {
                    String encodedPassword = passwordEncoder.encode(user.getUserPwd());
                    user.setUserPwd(encodedPassword); // 암호화된 비밀번호로 설정
                } else{
                    user.setUserPwd(originUser.getUserPwd());
                }

                log.info("admin 값 = "+String.valueOf(user.isAdmin()));

                // user 업데이트
                user.setRName(user.getRName() != null ? user.getRName() : originUser.getRName());
                user.setUserName(user.getUserName() != null ? user.getUserName() : originUser.getUserName());
                user.setPosition(user.getPosition() != null ? user.getPosition() : originUser.getPosition());
//                user.setAdmin(user.isAdmin() ? user.isAdmin() : originUser.isAdmin());
                user.setTeam(user.getTeam() != null ? user.getTeam() : originUser.getTeam());
                user.setMPhone(user.getMPhone() != null ? user.getMPhone() : originUser.getMPhone());
                user.setRPhone(user.getRPhone() != null ? user.getRPhone() : originUser.getRPhone());

                // 0 = 업데이트 실패, 1 = 업데이트 성공
             int updateUser = userMapper.updateUser(user);

             if(updateUser == 0){
                 userResponse.setCode("E003");
                 userResponse.setMessage("유저 업데이트 실패");
             } else {
                 userResponse.setCode("C000");
                 userResponse.setMessage("유저 업데이트 성공");

                 String pattern = "userList_*"; // 패턴 정의
//                 Set<String> keys = redisTemplate.keys(pattern); // 해당 패턴에 맞는 모든 키 가져오기

//                 redisTemplate.delete(keys); // 전체 회원 목록 캐시를 삭제
             }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userResponse;
    }

    // 업무용 연락처 등록
    public ApiResponse addPhoneNum(String phoneNumber) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try{
            // 입력받은 연락처
            log.info("입력받은 연락처 = " + phoneNumber);
            if(isValidPhoneNumber(phoneNumber)){
                // 형식이 일치하면
                int validPhoneChk = userMapper.validPhoneChk(phoneNumber);
                if(validPhoneChk == 1){
                    // 연락처가 중복이면
                    apiResponse.setCode("E004");
                    apiResponse.setMessage("이미 등록된 연락처입니다.");
                } else {
                    // 연락처가 중복 아닐때
                    userMapper.addPhoneNum(phoneNumber);
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
    public UserResponse allPhoneNumList(){

        UserResponse userResponse = new UserResponse();

        userResponse.setPhoneNumList(userMapper.allPhoneNumList());
        userResponse.setCode("C000");
        userResponse.setMessage("조회 성공");

        return userResponse;
    }

    // 업무용 연락처 삭제
    public ApiResponse delPhoneNum(String phoneNumber) {

        ApiResponse apiResponse = new ApiResponse();

            if(phoneNumber == null){
                apiResponse.setCode("E003");
                apiResponse.setMessage("삭제할 연락처를 입력해주세요.");
            } else {
                int delPhoneNum = userMapper.delPhoneNum(phoneNumber);
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
    public UserResponse findUserName(String phoneNumber) throws Exception {
        UserResponse userResponse = new UserResponse();
        try{
            if(phoneNumber == null){
                // 연락처 입력 못 받았을 때
                userResponse.setCode("E002");
                userResponse.setMessage("조회할 연락처를 입력해주세요.");
            } else {
                // 연락처 입력 받음

                userResponse.setFindUserList(userMapper.findUserList(phoneNumber));
                userResponse.setCode("C000");
                userResponse.setMessage("조회 성공");
            }
        } catch (Exception e) {
            userResponse.setCode("E001");
            userResponse.setMessage("등록되지 않은 연락처입니다.");
        }
        return userResponse;
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
                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
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
            Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
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
    public AccessResponse exper_cookie() throws Exception {
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

}

