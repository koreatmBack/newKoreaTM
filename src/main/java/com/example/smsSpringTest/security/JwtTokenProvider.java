package com.example.smsSpringTest.security;

import com.example.smsSpringTest.mapper.AdminMapper;
import com.example.smsSpringTest.mapper.CommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * author : 신기훈
 * date : 2024-10-21
 * comment : Jwt 토큰 생성 및 관리
 */

@Slf4j
@Component
public class JwtTokenProvider {

    private final CommonMapper commonMapper;
    private final CafeconUserMapper cafeconUserMapper;
    private final AdminMapper adminMapper;
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;  // 1시간
//    private static final long ACCESS_TOKEN_EXPIRE_TIME =  360 * 1000 ;  // 6분
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;    // api 작업용 한달

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;  // 30일

    private static final int COOKIE_EXPIRE_TIME = (int) (ACCESS_TOKEN_EXPIRE_TIME / 1000); // 1시간 (초)
//    private static final long Cookie_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_TIME / 1000;

    // 관리자용 쿠키
    private static final long ADMIN_ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;  // 1시간
    private static final int ADMIN_COOKIE_EXPIRE_TIME = (int) (ADMIN_ACCESS_TOKEN_EXPIRE_TIME / 1000); // 1시간 (초)

    private final Key key;


    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CommonMapper commonMapper, CafeconUserMapper cafeconUserMapper, AdminMapper adminMapper) {
        this.commonMapper = commonMapper;
        this.cafeconUserMapper = cafeconUserMapper;
        this.adminMapper = adminMapper;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 관리자용 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 객체 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public Token AdminGenerateToken(Authentication authentication) throws Exception {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성 : 1시간
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ADMIN_ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 : 30일
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ADMIN_ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // accessToken 생성
    public Token AdminAccessToken(Authentication authentication) throws Exception {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ADMIN_ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ADMIN_ACCESS_TOKEN_EXPIRE_TIME)
                .build();
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    // 객체 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public Token generateToken(Authentication authentication) throws Exception {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성 : 1시간
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 : 30일
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // accessToken 생성
    public Token accessToken(Authentication authentication) throws Exception {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .build();
    }

    // refreshToken 생성
    public Token refreshToken(Authentication authentication) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // 소셜 로그인 토큰 생성
    public Token socialGenerateToken(String userId) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim(AUTHORITIES_KEY, "ROLE_USER")
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // 소셜 로그인 AccessToken 생성
    public Token socialAccessToken(String userId) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Access Toekn 생성
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim(AUTHORITIES_KEY, "ROLE_USER")
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .build();
    }

//    // 소셜 로그인 토큰 생성
//    public Token socialGenerateToken(String userId) throws Exception {
//
//        LocalDateTime now = LocalDateTime.now();
//        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
//
//        // Access Toekn 생성
//        String accessToken = Jwts.builder()
//                .setSubject(userId)
//                .claim(AUTHORITIES_KEY, "ROLE_USER")
//                .claim("type", TYPE_ACCESS)
//                .setIssuedAt(currentDate)
//                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Refresh Token 생성
//        String refreshToken = Jwts.builder()
//                .claim("type", TYPE_REFRESH)
//                .setIssuedAt(currentDate)
//                .setExpiration(new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return Token.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
//                .refreshToken(refreshToken)
//                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
//                .build();
//    }
//
//    // 소셜 로그인 AccessToken 생성
//    public Token socialAccessToken(String userId) throws Exception {
//
//        LocalDateTime now = LocalDateTime.now();
//        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
//
//        // Access Toekn 생성
//        String accessToken = Jwts.builder()
//                .setSubject(userId)
//                .claim(AUTHORITIES_KEY, "ROLE_USER")
//                .claim("type", TYPE_ACCESS)
//                .setIssuedAt(currentDate)
//                .setExpiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return Token.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
//                .build();
//    }


    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {

        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없습니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return "ACCESS";
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return "FAULT";
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return "EXPIRED";
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return "UNSUPPORT";
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return "FAIL";
        }

    }

    // accessToken 검증, 파싱
    public Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
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

//    // 로그아웃 시 Redis 에 담긴 AccessToken 조회
//    public String getRedisAccessToken(String accessToken) {
//
//        return (String) redisTemplate.opsForValue().get(accessToken);
//    }
//
//    // redis에 저장된 비밀번호 변경 시간 조회
//    public String getPwdEditTime(String customId) {
//
//        return (String) redisTemplate.opsForValue().get(customId);
//    }

    // 토큰 남은 유효시간 체크
    public Long getExpiration(String resolveToken) {
        try {
            // 토큰 남은 유효시간
            Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(resolveToken).getBody().getExpiration();
            long now = new Date().getTime();

            if (expiration.before(new Date())) {
                // 토큰이 이미 만료되었음을 감지하고 예외 처리
                throw new ExpiredJwtException(null, null, "Token expired");
            }

            return (expiration.getTime() - now);
        } catch (ExpiredJwtException ex) {
            log.info("expiration 에러");
            return null;
        }
    }

    // 쿠키 생성
    public Cookie createCookie(String accesstoken) {
//        String cookieName = "accesstoken_"+userId;
        String cookieName = "accesstoken";
        String cookieValue = accesstoken; // 쿠키벨류엔 글자제한이 있으므로, 벨류로 만들어담아준다.
        log.info("cookieValue = " + cookieValue);
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
//        cookie.setPath("/");
        cookie.setHttpOnly(true);  //httponly 옵션 설정
//        cookie.setHttpOnly(false);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(COOKIE_EXPIRE_TIME); //쿠키 만료시간 설정 - accesstoken과 일치하게
        return cookie;
    }

    // 관리자용 쿠키 생성
    public Cookie createCookieAdmin(String accesstoken) {
//        String cookieName = "accesstoken_"+userId;
        String cookieName = "accesstoken";
        String cookieValue = accesstoken; // 쿠키벨류엔 글자제한이 있으므로, 벨류로 만들어담아준다.
        log.info("cookieValue = " + cookieValue);
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
//        cookie.setPath("/");
        cookie.setHttpOnly(true);  //httponly 옵션 설정
//        cookie.setHttpOnly(false);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(ADMIN_COOKIE_EXPIRE_TIME); //쿠키 만료시간 설정 - accesstoken과 일치하게
        return cookie;
    }

    // 쿠키에서 토큰 가져오기
    public String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accesstoken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 쿠키 정보 토대로, userId 획득 후 role 가져오기
    public String findUserRole(String userId, String type){
        String role = "";
        if(type.equals("CAFECON")) {
            role = cafeconUserMapper.findRole(userId);
        } else if(type.equals("FORMMAIL")) {
            role = adminMapper.findOneAdmin(userId).getRole();
        }
        return role;
    }


//    // 쿠키 삭제 -> 만료 시간 0으로 설정하면 자동 삭제됨
//    public Cookie deleteCookie(String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        return cookie;
//    }

}