package com.example.smsSpringTest.filter;

import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : OncePerRequestFilter 상속받은 JWT FILETER
 * 클라이언트 요청 시 JWT 인증을 하기 위해 설치하는 커스텀 필터로, UsernamePasswordAuthenticationFilter 이전에 실행
 */
@Slf4j
@RequiredArgsConstructor
@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장 하는 역할
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        Cookie[] cookies = request.getCookies();

        String requestURI = request.getRequestURI();
        boolean hasError = false;
        String cookieToken = "";
        if(cookies != null){

            log.info("쿠키 있음");
            for(Cookie cookie : cookies) {
                cookieToken = cookie.getValue();
            }
        } else {
            log.info("쿠키 없음");
            cookieToken = resolveToken(request);
        }
            log.info("cookieToken = " + cookieToken);

//        if(isAllowedURI(requestURI)) {
//        // 쿠키 없을때 토큰 가져오는 방식
            String resolveToken = resolveToken(request);

//        String resolveToken = response.

            log.info("requestURI: {}", requestURI);
//            log.info("resolveToken = " + resolveToken);

//            if (!StringUtils.hasText(resolveToken)) {
//                log.debug("JWT 토큰이 존재하지 않거나 비어 있습니다, uri: {}", requestURI);
//                filterChain.doFilter(request, response);
//                return;
//            }

            String tokenStatus = jwtTokenProvider.validateToken(cookieToken);

            if (StringUtils.hasText(cookieToken) && !tokenStatus.equals("ACCESS")) {
                ApiResponse apiResponse = new ApiResponse("E401", "유효하지 않은 접근입니다.");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                return;
            }

//                    Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken);
//                    String authorities = authentication.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority)
//                            .collect(Collectors.joining(","));


            if(StringUtils.hasText(cookieToken) && !hasError) {
                // validateToken 으로 유효성 검사
                if (tokenStatus.equals("ACCESS")) {

//                    String blackListChk = jwtTokenProvider.getRedisAccessToken(resolveToken);
//                    Claims claims = jwtTokenProvider.parseClaims(resolveToken);
//                    String customId = claims.getSubject() + "_edit";
//                    String pwdEditTime = jwtTokenProvider.getPwdEditTime(customId);
                    log.info("이때 resolveToken 있음? " + cookieToken);
                    setAuthenticationAndRequest(request, response, cookieToken);

                } else {
                    hasError = true;
                    log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                }
            }
//        }

        if(!hasError){
            filterChain.doFilter(request, response);
        } else {
            ApiResponse apiResponse = new ApiResponse("E403", "접근 가능한 권한이 없습니다.");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getWriter(), apiResponse);
        }

    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//
//        String requestURI = request.getRequestURI();
//        boolean hasError = false;
//
//        if(!(isAllowedURI(requestURI))) {
//            String resolveToken = resolveToken(request);
//            String tokenStatus = jwtTokenProvider.validateToken(resolveToken);
//
//            log.info("requestURI: {}", requestURI);
//
//            if(StringUtils.hasText(resolveToken) && !tokenStatus.equals("ACCESS")) {
//                ApiResponse apiResponse = new ApiResponse("E401", "유효하지 않은 접근입니다.");
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                response.setCharacterEncoding("utf-8");
//                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
//                return;
//            }
//
//            if(StringUtils.hasText(resolveToken) && (isLoginEndpoint(requestURI))) {
//                hasError = true;
//
//            } else if(!StringUtils.hasText(resolveToken) && (isMemberEndpoint(requestURI))) {
//                hasError = true;
//
//            } else if(isManagerEndpoint(requestURI)) {
//                if(!StringUtils.hasText(resolveToken)) {
//                    hasError = true;
//
//                } else {
//                    Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken);
//                    String authorities = authentication.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority)
//                            .collect(Collectors.joining(","));
//
//                    if(authorities.equals("ROLE_READ")) {
//                        if(!isReadMemberEndpoint(requestURI)) {
//                            hasError = true;
//                        }
//                    } else if(!authorities.equals("ROLE_ADMIN") && !authorities.equals("ROLE_MANAGER")) {
//                        hasError = true;
//                    }
//                }
//            } else if(isAdminEndpoint(requestURI)) {
//                if(!StringUtils.hasText(resolveToken)) {
//                    hasError = true;
//
//                } else {
//                    Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken);
//                    String authorities = authentication.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority)
//                            .collect(Collectors.joining(","));
//
//                    if(authorities.equals("ROLE_READ")) {
//                        if(!isReadMemberEndpoint(requestURI)) {
//                            hasError = true;
//                        }
//                    } else if(!authorities.equals("ROLE_ADMIN")) {
//                        hasError = true;
//                    }
//                }
//            }
//
//            // validateToken 으로 유효성 검사
//            if(StringUtils.hasText(resolveToken) && !hasError) {
//                if(tokenStatus.equals("ACCESS")) {
////                    String blackListChk = jwtTokenProvider.getRedisAccessToken(resolveToken);
//                    Claims claims = jwtTokenProvider.parseClaims(resolveToken);
////                    String customId = claims.getSubject() + "_edit";
////                    String pwdEditTime = jwtTokenProvider.getPwdEditTime(customId);
//
//                    if (StringUtils.hasText(blackListChk) && blackListChk.equals("logout")) {
//                        hasError = true;
//
//                    } else if(StringUtils.hasText(pwdEditTime)) {
//                        try {
//                            Date issuedAt = claims.getIssuedAt();
//                            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//                            Date editTme = format.parse(pwdEditTime);
//
//                            if(issuedAt.compareTo(editTme) < 0) {
//                                tokenExpErrorLogout(response);
//                                return;
//
//                            } else {
//                                setAuthenticationAndRequest(request, response, resolveToken);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        setAuthenticationAndRequest(request, response, resolveToken);
//                    }
//
//                } else {
//                    hasError = true;
//                    log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
//                }
//            }
//        }
//
//        if(!hasError) {
//            filterChain.doFilter(request, response);
//        } else {
//            ApiResponse apiResponse = new ApiResponse("E403", "접근 가능한 권한이 없습니다.");
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding("utf-8");
//            new ObjectMapper().writeValue(response.getWriter(), apiResponse);
//        }
//    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);


//        //test
//        Cookie[] rc = request.getCookies();
//            String cookieTest = request.getHeader("Cookie");
//            log.info("cookieTest = " + cookieTest);
//        //--
//        String cookieName = "";
//        String cookieVal = "";
//        if(rc != null) {
//            for (Cookie cookie : rc) {
//                if (cookie.getValue().equals(bearerToken.substring(7))) {
//                    cookieName = cookie.getName();
//                    cookieVal = cookie.getValue();
//                }
//            }
//            log.info("cookieName = " + cookieName);
//            log.info("cookieVal = " + cookieVal);
//            //--
//        }
        log.info("bearerToken = " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {

            log.info("bearerToken.substring(7) = " + bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setAuthenticationAndRequest(HttpServletRequest request, HttpServletResponse response, String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.setHeader(HttpHeaders.AUTHORIZATION, request.getHeader(AUTHORIZATION_HEADER));
    }

    // 쿠키에서 토큰 가져오기
    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accesstoken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void tokenExpErrorLogout(HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = new ApiResponse("E999", "세션이 만료되었습니다. 다시 로그인해 주세요");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }

    // 토큰 조회가 필요없는 API
    private boolean isAllowedURI(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] allowedURIs = {
                "/v1/formMail_admin/join", "/api/v1/formMail_admin/join",
                "/v1/formMail_admin/login", "/api/v1/formMail_admin/login",
                "/v1/formMail_common/login", "/v1/formMail_common/join"
        };

        for (String allowedURI : allowedURIs) {
            if(pathMatcher.match(allowedURI, requestURI)){
                return true;
            }
        }
        return false;
    }

    /*
    // 토큰 조회가 필요없는 API
    private boolean isAllowedURI(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] allowedURIs = {
                "/api/v1/shop/goods/list",
                "/api/v1/shop/goods/list/{category1Seq}",
                "/api/v1/shop/goods/list/brand/{brandCode}",
                "/api/v1/shop/goods/detail/{goodsCode}",
                "/api/v1/shop/brand/list/**",
                "/api/v1/shop/goods/search/{searchKeyword}",
                "/api/v1/shop/goods/etc/list",
                "/api/v1/shop/get/rand/goods",
                "/api/v1/shop/detail/rand/goods",
                "/api/v1/user/applicants/add",
                "/api/v1/common/reissu/token",
                "/api/v1/user/login",
                "/api/v1/user/join",
                "/api/v1/user/login/kakao",
                "/api/v1/cafecon/login"
        };

        for (String allowedURI : allowedURIs) {
            if (pathMatcher.match(allowedURI, requestURI)) {
                return true;
            }
        }
        return false;
    }

    // 관리자 권한이 필요한 API
    private boolean isAdminEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] adminEndpoints = {
                "/api/v1/user/admin/userlst",
                "/api/v1/user/admin/pwd/upt",
                "/api/v1/user/admin/usrpnt/history",
                "/api/v1/shop/admin/**",
                "/api/v1/shop/care/**",
                "/api/v1/user/admin/manage/point/{gubun}",
                "/api/v1/board/admin/**",
                "/api/v1/board/type/list",
                "/api/v1/user/admin/add/point/{userId}",
                "/api/v1/user/admin/buy/point/{userId}",
                "/api/v1/user/admin/search/datePnt",
                "/api/v1/shop/goods/all/buyList",
                "/api/v1/board/admin/posts",
                "/api/v1/board/admin/paymt/sts",
                "/api/v1/user/admin/get/log",
                "/api/v1/common/get/nice/key",
                "/api/v1/common/del/nice/key",
                "/api/v1/user/proto",
                "/api/v1/shop/goods/log",
                "/api/v1/comp/**",
                "/api/v1/user/applicants/get/list",
                "/api/v1/user/applicants/upt/status",
                "/api/v1/board/add/job/post",
                "/api/v1/board/del/job/post",
                "/api/v1/board/upt/job/data",
                "/api/v1/board/upt/job/n",
                "/api/v1/board/upt/job/y",
                "/api/v1/board/get/job/applylist",
                "/api/v1/board/upt/apply/admchk",
                "/api/v1/user/force/leave"
        };

        for (String endpoint : adminEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

    // 회원 권한이 필요한 API
    private boolean isMemberEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] memberEndpoints = {
                "/api/v1/user/myinfo/**",
                "/api/v1/shop/goods/coupons",
                "/api/v1/shop/goods/send",
                "/api/v1/shop/goods/buyList",
                "/api/v1/user/logout",
                "/api/v1/user/get/point",
                "/api/v1/shop/goods/ins/memo",
                "/api/v1/shop/goods/upt/memo",
                "/api/v1/shop/goods/memo",
                "/api/v1/user/rel/integ/kakao",
                "/api/v1/user/integ/kakao",
                "/api/v1/board/pnt/posts",
                "/api/v1/board/upt/pnt/posts",
                "/api/v1/board/del/posts",
                "/api/v1/board/get/pnt/posts/list",
                "/api/v1/board/get/pnt/posts",
                "/api/v1/user/search/proto",
                "/api/v1/user/mypage/pnt/log",
                "/api/v1/board/get/mypage/applylist",
                "/api/v1/shop/cancel/bizapi"
        };

        for (String endpoint : memberEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

    // 기업 회원 권한이 필요한 API
    private boolean isCompMemberEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] memberEndpoints = {
                "/api/v1/cafecon/comp/**",
        };

        for (String endpoint : memberEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

    // 매니저 회원 권한이 필요한 API
    private boolean isManagerEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] memberEndpoints = {
                "/api/v1/board/manager/**",
                "/api/v1/ad/**",
                "/api/v1/common/manager/**",
                "/api/v1/comp/get/comlist"
        };

        for (String endpoint : memberEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

    // 읽기전용 회원 권한이 필요한 API
    private boolean isReadMemberEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] memberEndpoints = {
                "/api/v1/comp/commcare/status",
                "/api/v1/comp/get/comlist",
                "/api/v1/comp/today/pay/info/list",
                "/api/v1/comp/get/ad",
                "/api/v1/comp/prepay/complist",
                "/api/v1/comp/prepay/list",
                "/api/v1/comp/card/list",
                "/api/v1/comp/search/list",
                "/api/v1/comp/list",
                "/api/v1/comp/get/code",
                "/api/v1/comp/get/pay/list",
                "/api/v1/comp/ad/data",
                "/api/v1/comp/get/detail/pay",
                "/api/v1/ad/comp/list",
                "/api/v1/ad/logo/list",
                "/api/v1/ad/prog/list",
                "/api/v1/ad/get/tier/list"
        };

        for (String endpoint : memberEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

    // 로그인 관련 API
    private boolean isLoginEndpoint(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] loginEndpoints = {
                "/api/v1/user/login",
                "/api/v1/user/join",
                "/api/v1/user/login/kakao",

                "/api/v1/cafecon/login"
        };

        for (String endpoint : loginEndpoints) {
            if (pathMatcher.match(endpoint, requestURI)) {
                return true;
            }
        }

        return false;
    }

*/
}