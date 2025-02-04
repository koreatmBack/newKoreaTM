package com.example.smsSpringTest.service.cafecon;

import com.example.smsSpringTest.mapper.cafecon.CafeconCommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.model.cafecon.*;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.AccessResponse;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.cafecon.CouponResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * author : 신기훈
 * date : 2025-01-17
 * comment : 카페콘 공통 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CafeconCommonService {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final CafeconCommonMapper cafeconCommonMapper;
    private final CafeconUserMapper cafeconUserMapper;

    @Value("${biz.auth-code}")
    private String authCode;
    @Value("${biz.auth-token}")
    private String authToken;
    @Value("${biz.client-id}")
    private String clientId;

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
            String userId="";
            if(jwtTokenProvider.validateToken(cookieToken).equals("ACCESS")){
                //AccessToken에서 authentication 가져오기
                Authentication authentication = jwtTokenProvider.getAuthentication(cookieToken);
                userId = authentication.getName();
                log.info("id = " + userId);


            int point = cafeconUserMapper.getUserPoint(userId);

            Long limit = jwtTokenProvider.getExpiration(cookieToken) / 1000; // 초로 변환

                accessResponse.setCode("C000");
                accessResponse.setPoint(point);
                accessResponse.setMessage(Math.toIntExact(limit) + "초 남았습니다.");
                accessResponse.setLimit(Math.toIntExact(limit));
            } else {
                accessResponse.setCode("E001");
                accessResponse.setMessage("쿠키가 유효하지 않습니다!!");
            }
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
            int result = cafeconCommonMapper.getUserRefreshTokenCount(userId);

            if(result == 1) {
                refToken = cafeconCommonMapper.getUserRefreshTokenData(userId);
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


    // 기프티콘 전송
    // 쿠폰 전송하기
    public CouponResponse cafeConSendGiftToBiz(BizApi bizApi) throws Exception {
        CouponResponse couponResponse = new CouponResponse();
        // 로그인 유저인지 체크
        Cookie cookies[] = request.getCookies();
        String accessToken = "";
        CafeUser user = new CafeUser();
        // 만약 쿠키가 있다면
        for(Cookie cookie : cookies) {
            if("accesstoken".equals(cookie.getName())){
                accessToken = cookie.getValue();
            }
        }

        // 쿠키가 없다면
        if(!StringUtils.hasText(accessToken)){
            couponResponse.setCode("E401");
            couponResponse.setMessage("로그인 상태가 아닙니다.");
            return couponResponse;
        }
        // AccessToken 검증
        if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
            //AccessToken에서 authentication 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            user.setUserId(authentication.getName());
            log.info("id = " + user.getUserId());
            bizApi.setUserId(authentication.getName());
        }

//        CouponResponse couponResponse = new CouponResponse();

        String userId = bizApi.getUserId();
        String memo = bizApi.getMemo();
        log.info("bizApi :: " + bizApi);
        int originUserPoint = cafeconUserMapper.getUserPoint(userId);
        int originRealPrice = bizApi.getRealPrice();

        if(originRealPrice > originUserPoint) {
            couponResponse.setCode("E001");
            couponResponse.setMessage("포인트가 부족합니다.");

        } else {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = now.format(formatter);
            String trId = cafeconCommonMapper.getTrId();
            if (trId == null) {

                String num = "00000001";

                trId = "cafe_" + date + "_" + num;
//                trId = "lafe_" + date + "_" + num;
            }
            String orderNo = this.addOrderNo(trId);
            // point_log 테이블에서 order_no 찾기
            String findOrderNo = cafeconCommonMapper.findOrderNoPointLog(date);
            log.info("findOrderNoPL = " + findOrderNo);
            if(findOrderNo != null) {
                // point_log 테이블에 order_no 값이 있으면
                // coupon의 order_no와 point_log의 order_no 크기 비교하기
                String remainCp = orderNo.replace(date, ""); // "0000000_"
                String remainPl = findOrderNo.replace(date, "");
                int cp = Integer.parseInt(remainCp);
                int pl = Integer.parseInt(remainPl);
                String newTrId = "";
                if(pl >= cp) {
                    // point_log의 order_no이 coupon테이블의 order_no보다 클때
                    int nextNumber = pl + 1;
                    String newFormat = String.format("%08d", nextNumber);
                    // 최종 tr_id
                    newTrId = "cafe_" + date + "_" + newFormat;
                    trId = newTrId;
                    orderNo = this.addOrderNo(trId);
                }
            }

            log.info("trId :: " + trId);

            CafeCoupon cafeCoupon = new CafeCoupon();
//            String orderNo = this.addOrderNo(trId);
            String sendPhone = bizApi.getPhoneNo();
            // 발신번호
            String callbackNo = bizApi.getCallbackNo();
//            String managerPhone = cafeConMapper.getCompUserPhone(bizApi);
            String jsonDto = "";

            log.info("(수신) sendPhone :: " + sendPhone);
            log.info("(발신) callbackNo :: " + callbackNo);
            int getRealPrice = bizApi.getRealPrice();
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

            bizApi.setApiCode("0204");
            bizApi.setCustomAuthCode(authCode);
            bizApi.setCustomAuthToken(authToken);
            bizApi.setDevYn("N");
            bizApi.setGoodsCode(bizApi.getGoodsCode());
            bizApi.setTrId(trId);
            bizApi.setOrderNo(orderNo);
            bizApi.setCallbackNo(callbackNo);  // 발신번호
            bizApi.setPhoneNo(sendPhone);
            bizApi.setMmsTitle("기프티콘 전송");
            bizApi.setMmsMsg("이용해 주셔서 감사합니다.");
//            bizApi.setTemplateId("202308010219374");
//            bizApi.setBannerId("202308010243647");
            bizApi.setTemplateId("202403050247761");
            bizApi.setBannerId("202403050282744");
            bizApi.setBizId(clientId);
            bizApi.setGubun("N");
            bizApi.setRevInfoYn("N");

            String uri = "https://bizapi.giftishow.com/bizApi/send" +
                    "?api_code=" + bizApi.getApiCode() +
                    "&custom_auth_code=" + bizApi.getCustomAuthCode() +
                    "&custom_auth_token=" + bizApi.getCustomAuthToken() +
                    "&dev_yn=" + bizApi.getDevYn() +
                    "&goods_code=" + bizApi.getGoodsCode() +
                    "&tr_id=" + bizApi.getTrId() +
                    "&order_no=" + bizApi.getOrderNo() +
                    "&callback_no=" + bizApi.getCallbackNo() +
                    "&phone_no=" + bizApi.getPhoneNo() +
                    "&mms_title=" + URLEncoder.encode(bizApi.getMmsTitle(), StandardCharsets.UTF_8) +
                    "&mms_msg=" + URLEncoder.encode(bizApi.getMmsMsg(), StandardCharsets.UTF_8) +
                    "&template_id=" + bizApi.getTemplateId() +
                    "&banner_id=" + bizApi.getBannerId() +
                    "&user_id=" + bizApi.getBizId() +
                    "&gubun=" + bizApi.getGubun() +
                    "&rev_info_yn=" + bizApi.getRevInfoYn();

//        log.info(uri);

            try {
                HttpResponse<String> response = client.send(
                        HttpRequest.newBuilder()
                                .uri(URI.create(uri))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
                        , HttpResponse.BodyHandlers.ofString());

                JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
                String code = bodyJson.get("code").asText();
                String message = bodyJson.get("message").asText();

                log.info("code :: " + code);
                log.info("message :: " + message);

                cafeCoupon.setUserId(userId);
                cafeCoupon.setMemo(memo);
                cafeCoupon.setOrderNo(bizApi.getOrderNo());
                cafeCoupon.setTrId(bizApi.getTrId());
                cafeCoupon.setPhone(bizApi.getPhoneNo());
                cafeCoupon.setGoodsCode(bizApi.getGoodsCode());

                if(code.equals("0000")) {
                    log.info("구매 성공!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//                    Goods goods = commonMapper.getGoodsLimitDt(bizApi.getGoodsCode());
                    LocalDate nowDt = LocalDate.now();
                    int limit = Integer.parseInt(bizApi.getLimitDay());
                    LocalDate limitDt = nowDt.plusDays(limit);

                    cafeCoupon.setGoodsImgB(bizApi.getGoodsImgB());
                    cafeCoupon.setGoodsName(bizApi.getGoodsName());
                    cafeCoupon.setRealPrice(getRealPrice);
                    cafeCoupon.setDiscountPrice(bizApi.getDiscountPrice());
                    cafeCoupon.setCode(code);
                    cafeCoupon.setMessage(message);
                    cafeCoupon.setLimitDate(String.valueOf(limitDt));

                    log.info("cafeCoupon :: " + cafeCoupon);

                    // 구매할때 쿠폰 테이블에 realPrice, discountPrice 값 넣기
                    int result = cafeconCommonMapper.addCompUserCoupon(cafeCoupon);

                    log.info("result ::: " + result);
                    if(result == 1) {
                        int realPrice = getRealPrice;
                        int userPoint = cafeconUserMapper.getUserPoint(userId);
//                        int remaining = userPoint - realPrice;
                        int discountPrice = bizApi.getDiscountPrice();
                        int remaining = userPoint - discountPrice;
                        CafeUser cafeUser = new CafeUser();
                        cafeUser.setUserId(userId);
                        cafeUser.setPoint(remaining);
                        cafeconUserMapper.updatePoint(cafeUser);

                        // 만약 수신번호, 발신번호가 같으면 CP, 다르면 GI
                        String logType = "";
                        if(sendPhone.equals(callbackNo)){
                            logType = "CP";
                        } else {
                            logType = "GI";
                        }

                        PointLog pointLog = new PointLog();
                        pointLog.setGubun("B");
                        pointLog.setLogType(logType);
                        pointLog.setUserId(userId);
                        pointLog.setPoint(discountPrice);
                        pointLog.setCurrPoint(remaining);
                        pointLog.setGoodsName(bizApi.getGoodsName());
                        pointLog.setDiscountPrice(bizApi.getDiscountPrice());
                        pointLog.setOrderNo(orderNo);
                        pointLog.setTrId(bizApi.getTrId());
                        pointLog.setResendCnt(5);
                        cafeconCommonMapper.addCompUserPointLog(pointLog);

                        couponResponse.setTrId(cafeCoupon.getTrId());
                        couponResponse.setPoint(remaining);
                        couponResponse.setCode("C000");
                        couponResponse.setMessage("구매 완료");
                    } else {
                        cafeCoupon.setCode("0002");
                        cafeCoupon.setMessage("Coupon save fail");
                        cafeconCommonMapper.addFailUserCoupon(cafeCoupon);

                        this.goodsBuyCancel(bizApi);
                        couponResponse.setCode("E002");
                        couponResponse.setMessage("기프티콘 구매 실패하였습니다.");
                    }
                } else {
                    cafeCoupon.setCode(code);
                    cafeCoupon.setMessage(message);
                    cafeconCommonMapper.addFailUserCoupon(cafeCoupon);

                    couponResponse.setCode("E002");
                    couponResponse.setMessage("기프티콘 구매 실패하였습니다.");
                }
            } catch (Exception e) {
                cafeCoupon.setUserId(userId);
                cafeCoupon.setOrderNo(bizApi.getOrderNo());
                cafeCoupon.setTrId(bizApi.getTrId());
                cafeCoupon.setPhone(bizApi.getPhoneNo());
                cafeCoupon.setGoodsCode(bizApi.getGoodsCode());
                cafeCoupon.setCode("E003");
                cafeCoupon.setMessage(e.getClass().getName());

                int tridCnt = cafeconCommonMapper.tridCountChk(cafeCoupon);

                if(tridCnt == 0) {
                    cafeconCommonMapper.addFailUserCoupon(cafeCoupon);
                } else {
                    cafeconCommonMapper.deleteUserCoupon(cafeCoupon);
                }

                try {
                    // 2초 대기
                    Thread.sleep(2000);
                    log.info("2초 대기 성공");
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태 복원
                    log.error("지연 중 인터럽트 발생: {}", interruptedException.getMessage());
                }

                this.goodsBuyCancel(bizApi);
                couponResponse.setCode("E003");
                couponResponse.setMessage("오류가 발생하였습니다.");
                log.info(e.getMessage());
            }
        }

        return couponResponse;
    }

    // orderNo 생성
    String addOrderNo(String trId) throws Exception {

        String removePrefix = trId.replace("cafe_", "");

        return removePrefix.replace("_", "");
    }


    // 쿠폰 구매 취소 (자동 취소용)
    private void goodsBuyCancel(BizApi bizApi) throws Exception {

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        bizApi.setApiCode("0202");
        String jsonDto = "";

        String uri = "https://bizapi.giftishow.com/bizApi/cancel" +
                "?api_code=" + bizApi.getApiCode() +
                "&custom_auth_code=" + bizApi.getCustomAuthCode() +
                "&custom_auth_token=" + bizApi.getCustomAuthToken() +
                "&dev_yn=" + bizApi.getDevYn() +
                "&tr_id=" + bizApi.getTrId() +
                "&user_id=" + bizApi.getBizId();

        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
                    , HttpResponse.BodyHandlers.ofString());

            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
            bodyJson.get("code").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 쿠폰 취소 (API 실행시켜서 직접 취소)
    public ApiResponse cancelCouponToBizapi(BizApi bizApi) throws Exception {

        ApiResponse apiResponse = new ApiResponse();

        CouponResponse couponResponse = new CouponResponse();
        // 로그인 유저인지 체크
        Cookie cookies[] = request.getCookies();
        String accessToken = "";
        CafeUser user = new CafeUser();
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
            log.info("id = " + user.getUserId());
            bizApi.setUserId(authentication.getName());
        }

        int count = cafeconCommonMapper.isUserCouponCheck(bizApi);

        if(count == 1) {
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
            String jsonDto = "";

            bizApi.setApiCode("0202");
            bizApi.setCustomAuthCode(authCode);
            bizApi.setCustomAuthToken(authToken);
            bizApi.setDevYn("N");
            bizApi.setBizId(clientId);

            String uri = "https://bizapi.giftishow.com/bizApi/cancel" +
                    "?api_code=" + bizApi.getApiCode() +
                    "&custom_auth_code=" + bizApi.getCustomAuthCode() +
                    "&custom_auth_token=" + bizApi.getCustomAuthToken() +
                    "&dev_yn=" + bizApi.getDevYn() +
                    "&tr_id=" + bizApi.getTrId() +
                    "&user_id=" + bizApi.getBizId();

            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
                    , HttpResponse.BodyHandlers.ofString());

            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());

            String code = bodyJson.get("code").asText();
            String message = bodyJson.get("message").asText();

            if(code.equals("0000")) {
                CafeCoupon coupon = cafeconCommonMapper.getTrIdCouponData(bizApi.getTrId());

                bizApi.setGoodsCode(coupon.getGoodsCode());

                String userId = bizApi.getUserId();
//                int realPrice = coupon.getRealPrice();
                int discountPrice = coupon.getDiscountPrice();
                int userPoint = cafeconUserMapper.getUserPoint(userId);
                int totalPoint = discountPrice + userPoint;

                CafeUser user2 = new CafeUser();
                user2.setUserId(userId);
                user2.setPoint(totalPoint);

                int result = cafeconUserMapper.updatePoint(user2);

                if(result == 1) {
//                    Goods goods = cafeConMapper.getGoodsPriceData(coupon.getGoodsCode());

//                    LocalDate now = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//                    String date = now.format(formatter);
//                    String trId = cafeconCommonMapper.getTrId();
//                    if (trId == null) {
//
//                        String num = "00000001";
//
//                        trId = "cafe_" + date + "_" + num;
////                trId = "lafe_" + date + "_" + num;
//                    }
//                    String orderNo = this.addOrderNo(trId);
//                    // point_log 테이블에서 order_no 찾기
//                    String findOrderNo = cafeconCommonMapper.findOrderNoPointLog(date);
//
//                    if(findOrderNo != null) {
//                        // point_log 테이블에 order_no 값이 있으면
//                        // coupon의 order_no와 point_log의 order_no 크기 비교하기
//                        String remainCp = orderNo.replace(date, ""); // "0000000_"
//                        String remainPl = findOrderNo.replace(date, "");
//                        int cp = Integer.parseInt(remainCp);
//                        int pl = Integer.parseInt(remainPl);
//                        String newTrId = "";
//                        if(pl >= cp) {
//                            // point_log의 order_no이 coupon테이블의 order_no보다 클때
//                            int nextNumber = pl + 1;
//                            String newFormat = String.format("%08d", nextNumber);
//                            // 최종 tr_id
//                            newTrId = "cafe_" + date + "_" + newFormat;
//                            trId = newTrId;
//                            orderNo = this.addOrderNo(trId);
//                        }
//                    }

//                    PointLog pointLog = new PointLog();
//                    pointLog.setGubun("P");
//                    pointLog.setLogType("CE");
//                    pointLog.setUserId(userId);
//                    pointLog.setTrId(bizApi.getTrId());
//                    pointLog.setPoint(discountPrice);
//                    pointLog.setCurrPoint(totalPoint);
//                    pointLog.setGoodsName(coupon.getGoodsName());
//                    pointLog.setDiscountPrice(coupon.getDiscountPrice());
//                    pointLog.setOrderNo(orderNo);
//
//                    cafeconCommonMapper.addCompUserPointLog(pointLog);
                    String trId = bizApi.getTrId();
                    PointLog pointLog = new PointLog();
                    pointLog.setCurrPoint(totalPoint);
                    pointLog.setTrId(trId);
//                    pointLog.setLogType("CE");
//                    pointLog.setGubun("P");
                    int cancelCoupon = cafeconCommonMapper.cancelCoupon(pointLog);
                    if(cancelCoupon == 0) {
                        apiResponse.setCode("E0805");
                        apiResponse.setMessage("쿠폰 취소가 불가능합니다.");
                        return apiResponse;
                    }
                    pointLog.setResendCnt(0);
                    cafeconCommonMapper.updateResendCnt(pointLog);
                }

                apiResponse.setCode("C000");
                apiResponse.setMessage("구매 취소 처리되었습니다.");
            } else if(code.equals("ERR0805")) {
                apiResponse.setCode("E0805");
                apiResponse.setMessage("쿠폰 취소가 불가능합니다.");
            } else if(code.equals("E0808")) {
                apiResponse.setCode("E0807");
                apiResponse.setMessage("이미 취소 처리된 쿠폰입니다.");
            } else {
                apiResponse.setCode("E9999");
                apiResponse.setMessage("취소 중 오류가 발생하였습니다.");
            }

        } else {
            apiResponse.setCode("E001");
            apiResponse.setMessage("조회된 기프티콘이 없습니다.");
        }

        return apiResponse;
    }

    // 쿠폰 상세 정보
    public CouponResponse getCouponsDetailData(Coupon coupon) throws Exception {

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        BizApi bizApi = new BizApi();
        CouponResponse couponResponse = new CouponResponse();

        Cookie cookies[] = request.getCookies();
        String accessToken = "";
        CafeUser user = new CafeUser();
        // 만약 쿠키가 있다면
        for(Cookie cookie : cookies) {
            if("accesstoken".equals(cookie.getName())){
                accessToken = cookie.getValue();
            }
        }

        // 쿠키가 없다면
        if(!StringUtils.hasText(accessToken)){
            couponResponse.setCode("E401");
            couponResponse.setMessage("로그인 상태가 아닙니다.");
            return couponResponse;
        }
        // AccessToken 검증
        if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
            //AccessToken에서 authentication 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            user.setUserId(authentication.getName());
            log.info("id = " + user.getUserId());
            bizApi.setUserId(authentication.getName());
        }


//        CouponResponse couponResponse = new CouponResponse();

        String userId = bizApi.getUserId();

        bizApi.setApiCode("0201");
        bizApi.setCustomAuthCode(authCode);
        bizApi.setCustomAuthToken(authToken);
        bizApi.setDevYn("N");
        bizApi.setTrId(coupon.getTrId());

        String uri = "https://bizapi.giftishow.com/bizApi/coupons" +
                "?api_code=" + bizApi.getApiCode() +
                "&custom_auth_code=" + bizApi.getCustomAuthCode() +
                "&custom_auth_token=" + bizApi.getCustomAuthToken() +
                "&dev_yn=" + bizApi.getDevYn() +
                "&tr_id=" + bizApi.getTrId();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDto = "";

        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
                    , HttpResponse.BodyHandlers.ofString());

            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());

            log.info("bodyJson: {}", bodyJson);

            CouponDetail couponDetail = objectMapper.treeToValue(bodyJson.findValue("couponInfoList").get(0), CouponDetail.class);

            if(couponDetail != null) {
                couponResponse.setCouponDetail(couponDetail);
                couponResponse.setCode("C000");
                couponResponse.setMessage("상세 정보 조회 완료");
            } else {
                couponResponse.setCode("E002");
                couponResponse.setMessage("상품이 존재하지 않습니다.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return couponResponse;
    }

    // 쿠폰 재전송
    public CouponResponse resendCoupon(BizApi bizApi) throws Exception {
        CouponResponse couponResponse = new CouponResponse();
        // 로그인 유저인지 체크
        Cookie cookies[] = request.getCookies();
        String accessToken = "";
        CafeUser user = new CafeUser();
        // 만약 쿠키가 있다면
        for(Cookie cookie : cookies) {
            if("accesstoken".equals(cookie.getName())){
                accessToken = cookie.getValue();
            }
        }

        // 쿠키가 없다면
        if(!StringUtils.hasText(accessToken)){
            couponResponse.setCode("E401");
            couponResponse.setMessage("로그인 상태가 아닙니다.");
            return couponResponse;
        }
        // AccessToken 검증
        if(jwtTokenProvider.validateToken(accessToken).equals("ACCESS")){
            //AccessToken에서 authentication 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            user.setUserId(authentication.getName());
            log.info("id = " + user.getUserId());
            bizApi.setUserId(authentication.getName());
        }

        log.info("bizApi :: " + bizApi);

        String trId = bizApi.getTrId();


            String jsonDto = "";

            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

            bizApi.setApiCode("0203");
            bizApi.setCustomAuthCode(authCode);
            bizApi.setCustomAuthToken(authToken);
            bizApi.setDevYn("N");
            bizApi.setTrId(trId);
            bizApi.setSmsFlag("N");
            bizApi.setBizId(clientId);


            String uri = "https://bizapi.giftishow.com/bizApi/resend" +
                    "?api_code=" + bizApi.getApiCode() +
                    "&custom_auth_code=" + bizApi.getCustomAuthCode() +
                    "&custom_auth_token=" + bizApi.getCustomAuthToken() +
                    "&dev_yn=" + bizApi.getDevYn() +
                    "&tr_id=" + bizApi.getTrId() +
                    "&sms_flag=" + bizApi.getSmsFlag() +
                    "&user_id=" + bizApi.getBizId();
//        log.info(uri);
        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
                    , HttpResponse.BodyHandlers.ofString());

            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
            String code = bodyJson.get("code").asText();
            String message = bodyJson.get("message").asText();

            log.info("code :: " + code);
            log.info("message :: " + message);

            if(code.equals("0000")) {
                log.info("재전송 성공!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                int resendCnt = 0;
                int newResendCnt = cafeconCommonMapper.resendCnt(trId);
                resendCnt = newResendCnt - 1;
                if(resendCnt < 0) {
                    // 만약 횟수 초과되면
                    couponResponse.setCode("E002");
                    couponResponse.setMessage("기프티콘 재전송 실패하였습니다. 횟수 초과");
                    return couponResponse;
                }
                PointLog pl = new PointLog();
                pl.setTrId(trId);
                pl.setResendCnt(resendCnt);
                int updateResendCnt = cafeconCommonMapper.updateResendCnt(pl);
                couponResponse.setResendCnt(resendCnt);
                couponResponse.setCode("C000");
                couponResponse.setMessage("기프티콘 재전송 성공하였습니다.");
            } else {
                couponResponse.setCode("E002");
                couponResponse.setMessage("기프티콘 재전송 실패하였습니다.");
            }
        } catch (Exception e) {
            couponResponse.setCode("E001");
            couponResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return couponResponse;
    }


    // 취소 내역 있는지 조회
    public ApiResponse findCancelLog(PointLog pl) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        try {
            int findTrid = cafeconCommonMapper.findTrIdCheck(pl);
            if(findTrid != 0) {
                // 취소 내역 있음
                apiResponse.setCode("C000");
                apiResponse.setMessage("취소 내역 있음");
            } else {
                // 취소 내역 없음
                apiResponse.setCode("C003");
                apiResponse.setMessage("취소 내역 없음");
            }
        } catch (Exception e) {
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
            log.info(e.getMessage());
        }
        return apiResponse;
    }


}
