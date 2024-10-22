package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.MemberMapper;
import com.example.smsSpringTest.model.common.JwtUser;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.MemberResponse;
import com.example.smsSpringTest.model.response.RefResponse;
import com.example.smsSpringTest.repository.MemberRepository;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : jwt 인증을 위한 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommonMapper commonMapper;
    private final MemberMapper memberMapper;
    private final HttpServletResponse response;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public ApiResponse signUp(JwtUser user) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        try {

            user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));

            int result = memberMapper.signUp(user);

            if(result == 1) {
                apiResponse.setCode("C000");
                apiResponse.setMessage("회원가입이 완료되었습니다.");
            } else {
                apiResponse.setCode("E001");
                apiResponse.setMessage("오류가 발생했습니다. 다시 시도해주세요.");
            }

        } catch(Exception e){
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }

        return apiResponse;
    }


    // 회원 로그인
    @Transactional
    public MemberResponse login(JwtUser user) throws Exception {
        MemberResponse memberResponse = new MemberResponse();

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPwd());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.info("authenticationToken = " + authenticationToken);
            log.info("authentication = " + authentication);
            String userId = authentication.getName();
            log.info("userId = " + userId);

            RefToken refToken = commonMapper.getUserRefreshTokenData(userId);
            log.info("refToken = " + refToken);
            RefToken refToken2 = new RefToken();
            Token token = null;

            if(refToken != null){
                // 리프레쉬 토큰이 null이 아닐때
                LocalDate now = LocalDate.now();
                String uptDate = refToken.getUptDate();
                LocalDate parseUptDate = LocalDate.parse(uptDate);
                log.info("uptDate = " + uptDate);
                log.info("parseUptDate = " + parseUptDate);

                Long remainingMilliseconds = jwtTokenProvider.getExpiration(refToken.getRefreshToken());
                log.info("남은 리프레시 토큰 유효기간 밀리seconds = " + remainingMilliseconds );
                if(remainingMilliseconds == null || remainingMilliseconds <= 0) {
                    // 리프레시 토큰 유효기간이 null 이거나 0보다 같거나 작을때
                    commonMapper.deleteUserToken(userId);

                    // 토큰(access, refresh) 재생성
                    token = jwtTokenProvider.generateToken(authentication);
                    log.info("레프토큰 null 아닐때, 밀리세컨 null 이거나 0보다 같작일때 token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.addUserToken(refToken2);
                } else if(now.isAfter(parseUptDate.plusDays(28))){
                    // 현재 날짜가 , uptdate + 28일보다 이후일때

                    // 토큰 재생성
                    token = jwtTokenProvider.generateToken(authentication);
                    log.info("After 28 -> token = " + token);
                    refToken2.setUserId(userId);
                    refToken2.setGrantType(token.getGrantType());
                    refToken2.setRefreshToken(token.getRefreshToken());
                    commonMapper.updateUserToken(refToken2);
                } else {
                    // 현재 날짜가, uptdate + 28일보다 이전이면서 , refresh 토큰도 유효할때
                    token = jwtTokenProvider.accessToken(authentication);
                    log.info("값 있을때 token = " + token);
                    Long accessTokenExpiration = jwtTokenProvider.getExpiration(token.getAccessToken());
                    log.info("accessToken 유효기간 밀리seconds = " + accessTokenExpiration);
                }

            } else {
                // refresh 토큰이 null

                // 토큰 재생성
                token = jwtTokenProvider.generateToken(authentication);
                log.info("refToken이 null일때 token = " + token);
                refToken2.setUserId(userId);
                refToken2.setGrantType(token.getGrantType());
                refToken2.setRefreshToken(token.getRefreshToken());
                commonMapper.addUserToken(refToken2);
            }

            log.info("token: {}", token.getAccessToken());

            if(token.getAccessToken() != null && !token.getAccessToken().isBlank()){
                // 최종적으로 access 토큰이 있을때
                memberResponse.setMember(memberMapper.getFrontUserProfile(userId));
                memberResponse.setCode("C000");
                memberResponse.setMessage("로그인 완료");
                response.setHeader(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken());
            } else {
                // 최종적으로 access 토큰이 없을때
                memberResponse.setCode("E001");
                memberResponse.setMessage("최종적으로 Access Token이 없습니다.");
            }

        } catch (BadCredentialsException e) {
            memberResponse.setCode("E003");
            memberResponse.setMessage("아이디 또는 비밀번호를 확인해 주세요.");
            log.info(e.getMessage());
        }

        return memberResponse;
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
                        response.setHeader(HttpHeaders.AUTHORIZATION, "bearer " + token.getAccessToken());

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
}
