package com.example.smsSpringTest.service.jobsite;

import com.example.smsSpringTest.mapper.jobsite.JobCommonMapper;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.formmail.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author : 신기훈
 * date : 2024-10-30
 * comment : 잡사이트 공통 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class jobsite_commonService {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final JobCommonMapper commonMapper;

//    // 토큰 재발급 요청
//    public RefResponse reissuToken(RefToken refToken) throws Exception {
//
//        RefResponse refResponse = new RefResponse();
//
//        if(refToken.getRefreshToken() != null && !refToken.getRefreshToken().equals("")) {
//            int refCnt = commonMapper.getRefreshTokenCount(refToken);
//
//            if (refCnt == 1) {
//                RefToken dbRefToken = commonMapper.getRefreshTokenData(refToken);
//                Long remainingMilliseconds = jwtTokenProvider.getExpiration(dbRefToken.getRefreshToken());
//
//                if (dbRefToken.getRefreshToken() != null && !dbRefToken.getRefreshToken().isBlank()) {
//                    if (remainingMilliseconds == null || remainingMilliseconds <= 0) {
//                        refResponse.setCode("E999");
//                        refResponse.setMessage("로그아웃");
//                    } else if (refToken.getRefreshToken().equals(dbRefToken.getRefreshToken())) {
//                        String resolveToken = refToken.getResolveToken();
//                        String bearer_prefix = "bearer ";
//                        log.info("resolve Token = " + resolveToken);
//                        if (StringUtils.hasText(resolveToken) && resolveToken.startsWith(bearer_prefix)) {
//                            resolveToken = resolveToken.substring(7);
//                        }
//
//                        Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken);
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        Token token = jwtTokenProvider.accessToken(authentication);
////                        response.setHeader(HttpHeaders.AUTHORIZATION, "bearer " + token.getAccessToken());
//                        Cookie cookie = jwtTokenProvider.createCookie(token.getAccessToken());
//                        response.addCookie(cookie);
//                        commonMapper.updateUserToken(dbRefToken);
//                        log.info("새로 발급받은 access Token = " + token.getAccessToken());
//                        refResponse.setRefToken(dbRefToken);
//                        refResponse.setCode("C000");
//                        refResponse.setMessage("재발급");
//                    }
//                } else {
//                    refResponse.setCode("E999");
//                    refResponse.setMessage("로그아웃");
//                }
//            } else {
//                refResponse.setCode("E999");
//                refResponse.setMessage("로그아웃");
//            }
//        } else {
//            refResponse.setCode("E999");
//            refResponse.setMessage("로그아웃");
//        }
//
//        return refResponse;
//    }

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

}
