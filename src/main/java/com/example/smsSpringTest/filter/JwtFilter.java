package com.example.smsSpringTest.filter;

import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.stream.Collectors;

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
                if("accesstoken".equals(cookie.getName())){
                     cookieToken = cookie.getValue();
                }
            }
        } else {
            log.info("쿠키 없음");
//            cookieToken = resolveToken(request);
        }
            log.info("cookieToken = " + cookieToken);


            log.info("requestURI: {}", requestURI);

            // 토큰 조회가 필요한 API 일때
       if(!(isAllowedURI(requestURI))) {
           if(!StringUtils.hasText(cookieToken)){
               // 만약 쿠키가 없다면
               hasError = true;
           } else {
               String tokenStatus = jwtTokenProvider.validateToken(cookieToken);

               // 쿠키가 있지만, 그 쿠키가 ACCESS가 아닐때
               if (StringUtils.hasText(cookieToken) && !tokenStatus.equals("ACCESS")) {
                   log.info("유효하지 않은 접근");
                   ApiResponse apiResponse = new ApiResponse("E401", "로그인 후 이용 부탁드립니다.");
                   response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                   response.setCharacterEncoding("utf-8");
                   new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                   return;
               }
//           if (!(isAllowedURI(requestURI)) && !StringUtils.hasText(cookieToken)) {
//               log.info("쿠키 없으므로 접근 불가능, 재 로그인 필요함");
//               ApiResponse apiResponse = new ApiResponse("E401", "로그인 후 이용 부탁드립니다.");
//               response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//               response.setCharacterEncoding("utf-8");
//               new ObjectMapper().writeValue(response.getWriter(), apiResponse);
//               return;
//           }

//           if (!StringUtils.hasText(cookieToken)) {
//               log.info("JWT 토큰이 존재하지 않거나 비어 있습니다, uri: {}", requestURI);
//               filterChain.doFilter(request, response);
//               return;
//           }


//           if (!tokenStatus.equals("ACCESS")) {
//               ApiResponse apiResponse = new ApiResponse("E401", "유효하지 않은 접근입니다.");
//               response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//               response.setCharacterEncoding("utf-8");
//               new ObjectMapper().writeValue(response.getWriter(), apiResponse);
//               return;
//           }


               Authentication authentication = jwtTokenProvider.getAuthentication(cookieToken);
               String authorities = authentication.getAuthorities().stream()
                       .map(GrantedAuthority::getAuthority)
                       .collect(Collectors.joining(","));

               if(authorities.equals("ROLE_USER")) {
                   if(!isUserEndpoint(requestURI)){
                       //권한이 잡사이트 유저인데, 유저 권한없는 엔드포인트 접근
                       hasError = true;
                   }
               }


               // validateToken 으로 유효성 검사
               if (tokenStatus.equals("ACCESS") && !hasError) {

                   log.info("JWT Filter role check = " + jwtTokenProvider.getAuthentication(cookieToken).getAuthorities());

//                    String blackListChk = jwtTokenProvider.getRedisAccessToken(resolveToken);
//                    Claims claims = jwtTokenProvider.parseClaims(resolveToken);
//                    String customId = claims.getSubject() + "_edit";
//                    String pwdEditTime = jwtTokenProvider.getPwdEditTime(customId);

                   setAuthenticationAndRequest(request, response, cookieToken);

               } else {
                   hasError = true;
                   log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
               }
           } // 쿠키 있을때 끝
//        }
       }

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
//        response.setHeader(HttpHeaders.AUTHORIZATION, request.getHeader(AUTHORIZATION_HEADER));
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
                "/v1/formMail/sendSms", "/api/v1/formMail/sendSms",
                "/v1/formMail/cert/sms", "/api/v1/formMail/cert/sms",
                "/v1/formMail_admin/join", "/api/v1/formMail_admin/join",
                "/v1/formMail_admin/login", "/api/v1/formMail_admin/login",
                "/v1/formMail_admin/exper_cookie", "/api/v1/formMail_admin/exper_cookie",
                "/v1/formMail_common/login", "/api/v1/formMail_common/login",
                "/v1/formMail_common/join", "/api/v1/formMail_common/join",
                "/v1/jobsite/user/join", "/api/v1/jobsite/user/join",
                "/v1/jobsite/user/login/**", "/api/v1/jobsite/user/login/**",
                "/v1/jobsite/user/cert", "/api/v1/jobsite/user/cert",
                "/v1/jobsite/user/check/id", "/api/v1/jobsite/user/check/id",
                // 잡사이트 조회용
                "/v1/formMail_ad/allJobsiteList", "/api/v1/formMail_ad/allJobsiteList",
                "/v1/formMail_ad/findOneJobsite", "/api/v1/formMail_ad/findOneJobsite",
                "/v1/formMail_ad/searchTitleList", "/api/v1/formMail_ad/searchTitleList",
                "/v1/formMail_ad/orderByCreated", "/api/v1/formMail_ad/orderByCreated",
                "/v1/formMail_ad/orderByWorkDay", "/api/v1/formMail_ad/orderByWorkDay",
                "/v1/formMail_ad/orderByMaxPay", "/api/v1/formMail_ad/orderByMaxPay",
                "/v1/formMail_ad/orderByWorkTime", "/api/v1/formMail_ad/orderByWorkTime",
                // 잡사이트 지원자 등록
                "/v1/jobsite/common/addApply", "/api/v1/jobsite/common/addApply"
                // kakao 로그인
                , "/v1/social/**", "/v1/jobsite/user/kakao/**"
                , "/api/v1/social/**", "/api/v1/jobsite/user/kakao/**"

        };

        for (String allowedURI : allowedURIs) {
            if(pathMatcher.match(allowedURI, requestURI)){
                return true;
            }
        }
        return false;
    }

    // 잡사이트 회원이 이용 가능한 API
    private boolean isUserEndpoint(String requestURI){
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] userEndpoints = {
                "/v1/jobsite/**", "/api/v1/jobsite/**",
                "/v1/formMail_ad/findCompanyAndUser","/api/v1/formMail_ad/findCompanyAndUser"
        };

        for (String userEndpoint : userEndpoints) {
            if(pathMatcher.match(userEndpoint, requestURI)) {
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