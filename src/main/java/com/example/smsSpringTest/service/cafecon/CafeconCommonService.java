package com.example.smsSpringTest.service.cafecon;

import com.example.smsSpringTest.mapper.cafecon.CafeconCommonMapper;
import com.example.smsSpringTest.model.common.RefToken;
import com.example.smsSpringTest.model.common.Token;
import com.example.smsSpringTest.model.response.AccessResponse;
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

//
//    // 기프티콘 전송
//    // 쿠폰 전송하기
//    public CouponResponse cafeConSendGiftToBiz(BizApi bizApi) throws Exception {
//
//        log.info("bizApi :: " + bizApi);
//
//        CouponResponse couponResponse = new CouponResponse();
//
//        String userId = bizApi.getUserId();
//        String memo = bizApi.getMemo();
//
//        int userPoint = cafeConMapper.getCompUserPoint(bizApi);
//        int realPrice = commonMapper.getItemPrice(bizApi);
//// 2025-01-16 : 나중에 카페콘 서비스화하면 가격 비교는 제외해야함!!!! (프론트에서 비교)
//        if(realPrice > userPoint) {
//            couponResponse.setCode("E001");
//            couponResponse.setMessage("포인트가 부족합니다.");
//
//        } else {
////            String trId = cafeConMapper.getTrId();
//            String trId = cafeConMapper.localGetTrId();
//
//            if(trId == null) {
//                LocalDate now = LocalDate.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//                String date = now.format(formatter);
//                String num = "00000001";
//
//                trId = "cafe_" + date + "_" + num;
////                trId = "lafe_" + date + "_" + num;
//            }
//
//            log.info("trId :: " + trId);
//
//            CafeCoupon cafeCoupon = new CafeCoupon();
//            String orderNo = this.addOrderNo(trId);
//            String sendPhone = bizApi.getPhoneNo();
//            String managerPhone = cafeConMapper.getCompUserPhone(bizApi);
//            String jsonDto = "";
//
//            log.info("sendPhone :: " + sendPhone);
//
//            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
//
//            bizApi.setApiCode("0204");
//            bizApi.setCustomAuthCode("REALd6f625bd88904490938d49ebb9687a3b");
//            bizApi.setCustomAuthToken("fTy%2Bx%2FJCLaWNusNPs07%2F%2Fw%3D%3D");
//            bizApi.setDevYn("N");
//            bizApi.setGoodsCode(bizApi.getGoodsCode());
//            bizApi.setTrId(trId);
//            bizApi.setOrderNo(orderNo);
//            bizApi.setCallbackNo(managerPhone);
//            bizApi.setPhoneNo(sendPhone);
//            bizApi.setMmsTitle("기프티콘이 전송되었습니다.");
//            bizApi.setMmsMsg("이용해 주셔서 감사합니다.");
//            bizApi.setTemplateId("202410170273587");
//            bizApi.setBannerId("202403050282742");
////            bizApi.setTemplateId("202403050247761");
////            bizApi.setBannerId("202403050282744");
//            bizApi.setBizId("millinien@naver.com");
//            bizApi.setGubun("N");
//            bizApi.setRevInfoYn("N");
//
//            String uri = "https://bizapi.giftishow.com/bizApi/send" +
//                    "?api_code=" + bizApi.getApiCode() +
//                    "&custom_auth_code=" + bizApi.getCustomAuthCode() +
//                    "&custom_auth_token=" + bizApi.getCustomAuthToken() +
//                    "&dev_yn=" + bizApi.getDevYn() +
//                    "&goods_code=" + bizApi.getGoodsCode() +
//                    "&tr_id=" + bizApi.getTrId() +
//                    "&order_no=" + bizApi.getOrderNo() +
//                    "&callback_no=" + bizApi.getCallbackNo() +
//                    "&phone_no=" + bizApi.getPhoneNo() +
//                    "&mms_title=" + URLEncoder.encode(bizApi.getMmsTitle(), StandardCharsets.UTF_8) +
//                    "&mms_msg=" + URLEncoder.encode(bizApi.getMmsMsg(), StandardCharsets.UTF_8) +
//                    "&template_id=" + bizApi.getTemplateId() +
//                    "&banner_id=" + bizApi.getBannerId() +
//                    "&user_id=" + bizApi.getBizId() +
//                    "&gubun=" + bizApi.getGubun() +
//                    "&rev_info_yn=" + bizApi.getRevInfoYn();
//
//            try {
//                HttpResponse<String> response = client.send(
//                        HttpRequest.newBuilder()
//                                .uri(URI.create(uri))
//                                .header("Content-Type", "application/json")
//                                .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
//                        , HttpResponse.BodyHandlers.ofString());
//
//                JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
//                String code = bodyJson.get("code").asText();
//                String message = bodyJson.get("message").asText();
//
//                log.info("code :: " + code);
//                log.info("message :: " + message);
//
//                cafeCoupon.setUserId(userId);
//                cafeCoupon.setMemo(memo);
//                cafeCoupon.setOrderNo(bizApi.getOrderNo());
//                cafeCoupon.setTrId(bizApi.getTrId());
//                cafeCoupon.setPhone(bizApi.getPhoneNo());
//                cafeCoupon.setGoodsCode(bizApi.getGoodsCode());
//
//                if(code.equals("0000")) {
//                    log.info("구매 성공!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//                    Goods goods = commonMapper.getGoodsLimitDt(bizApi.getGoodsCode());
//                    LocalDate nowDt = LocalDate.now();
//                    int limit = Integer.parseInt(goods.getLimitDay());
//                    LocalDate limitDt = nowDt.plusDays(limit);
//
//                    cafeCoupon.setGoodsImgB(goods.getGoodsImgB());
//                    cafeCoupon.setGoodsName(goods.getGoodsName());
//                    cafeCoupon.setCode(code);
//                    cafeCoupon.setMessage(message);
//                    cafeCoupon.setLimitDate(String.valueOf(limitDt));
//
//                    log.info("cafeCoupon :: " + cafeCoupon);
//
//                    int result = cafeConMapper.addCompUserCoupon(cafeCoupon);
//
//                    log.info("result ::: " + result);
//                    if(result == 1) {
//                        int remaining = userPoint - realPrice;
//                        CafeUser cafeUser = new CafeUser();
//                        cafeUser.setUserId(userId);
//                        cafeUser.setPoint(remaining);
//                        cafeConMapper.userPointManage(cafeUser);
//
//                        PointLog pointLog = new PointLog();
//                        pointLog.setGubun("B");
//                        pointLog.setLogType("CP");
//                        pointLog.setUserId(userId);
//                        pointLog.setPoint(realPrice);
//                        pointLog.setCurrPoint(remaining);
//                        pointLog.setGoodsName(goods.getGoodsName());
//                        pointLog.setDiscountPrice(goods.getDiscountPrice());
//                        cafeConMapper.addCompUserPointLog(pointLog);
//
//                        couponResponse.setTrId(cafeCoupon.getTrId());
//                        couponResponse.setPoint(remaining);
//                        couponResponse.setCode("C000");
//                        couponResponse.setMessage("구매 완료");
//                    } else {
//                        cafeCoupon.setCode("0002");
//                        cafeCoupon.setMessage("Coupon save fail");
//                        cafeConMapper.addFailUserCoupon(cafeCoupon);
//
//                        this.goodsBuyCancel(bizApi);
//                        couponResponse.setCode("E002");
//                        couponResponse.setMessage("기프티콘 구매 실패하였습니다.");
//                    }
//                } else {
//                    cafeCoupon.setCode(code);
//                    cafeCoupon.setMessage(message);
//                    cafeConMapper.addFailUserCoupon(cafeCoupon);
//
//                    couponResponse.setCode("E002");
//                    couponResponse.setMessage("기프티콘 구매 실패하였습니다.");
//                }
//            } catch (Exception e) {
//                cafeCoupon.setUserId(userId);
//                cafeCoupon.setOrderNo(bizApi.getOrderNo());
//                cafeCoupon.setTrId(bizApi.getTrId());
//                cafeCoupon.setPhone(bizApi.getPhoneNo());
//                cafeCoupon.setGoodsCode(bizApi.getGoodsCode());
//                cafeCoupon.setCode("E003");
//                cafeCoupon.setMessage(e.getClass().getName());
//
//                int tridCnt = cafeConMapper.tridCountChk(cafeCoupon);
//
//                if(tridCnt == 0) {
//                    cafeConMapper.addFailUserCoupon(cafeCoupon);
//                } else {
//                    cafeConMapper.deleteUserCoupon(cafeCoupon);
//                }
//
//                try {
//                    // 2초 대기
//                    Thread.sleep(2000);
//                    log.info("2초 대기 성공");
//                } catch (InterruptedException interruptedException) {
//                    Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태 복원
//                    log.error("지연 중 인터럽트 발생: {}", interruptedException.getMessage());
//                }
//
//                this.goodsBuyCancel(bizApi);
//                couponResponse.setCode("E003");
//                couponResponse.setMessage("오류가 발생하였습니다.");
//                log.info(e.getMessage());
//            }
//        }
//
//        return couponResponse;
//    }
//
//    // orderNo 생성
//    private String addOrderNo(String trId) throws Exception {
//
//        String removePrefix = trId.replace("cafe_", "");
//
//        return removePrefix.replace("_", "");
//    }
//
//
//    // 쿠폰 구매 취소 (자동 취소용)
//    private void goodsBuyCancel(BizApi bizApi) throws Exception {
//
//        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
//
//        bizApi.setApiCode("0202");
//        String jsonDto = "";
//
//        String uri = "https://bizapi.giftishow.com/bizApi/cancel" +
//                "?api_code=" + bizApi.getApiCode() +
//                "&custom_auth_code=" + bizApi.getCustomAuthCode() +
//                "&custom_auth_token=" + bizApi.getCustomAuthToken() +
//                "&dev_yn=" + bizApi.getDevYn() +
//                "&tr_id=" + bizApi.getTrId() +
//                "&user_id=" + bizApi.getBizId();
//
//        try {
//            HttpResponse<String> response = client.send(
//                    HttpRequest.newBuilder()
//                            .uri(URI.create(uri))
//                            .header("Content-Type", "application/json")
//                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
//                    , HttpResponse.BodyHandlers.ofString());
//
//            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
//            bodyJson.get("code").asText();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 쿠폰 취소 (API 실행시켜서 직접 취소)
//    public ApiResponse cancelCouponToBizapi(BizApi bizApi) throws Exception {
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        int count = cafeConMapper.isUserCouponCheck(bizApi);
//
//        if(count == 1) {
//            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
//            String jsonDto = "";
//
//            bizApi.setApiCode("0202");
//            bizApi.setCustomAuthCode("REALd6f625bd88904490938d49ebb9687a3b");
//            bizApi.setCustomAuthToken("fTy%2Bx%2FJCLaWNusNPs07%2F%2Fw%3D%3D");
//            bizApi.setDevYn("N");
//            bizApi.setBizId("millinien@naver.com");
//
//            String uri = "https://bizapi.giftishow.com/bizApi/cancel" +
//                    "?api_code=" + bizApi.getApiCode() +
//                    "&custom_auth_code=" + bizApi.getCustomAuthCode() +
//                    "&custom_auth_token=" + bizApi.getCustomAuthToken() +
//                    "&dev_yn=" + bizApi.getDevYn() +
//                    "&tr_id=" + bizApi.getTrId() +
//                    "&user_id=" + bizApi.getBizId();
//
//            HttpResponse<String> response = client.send(
//                    HttpRequest.newBuilder()
//                            .uri(URI.create(uri))
//                            .header("Content-Type", "application/json")
//                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
//                    , HttpResponse.BodyHandlers.ofString());
//
//            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
//
//            String code = bodyJson.get("code").asText();
//            String message = bodyJson.get("message").asText();
//
//            if(code.equals("0000")) {
//                Coupon coupon = cafeConMapper.getTrIdCouponData(bizApi.getTrId());
//                bizApi.setGoodsCode(coupon.getGoodsCode());
//
//                String userId = bizApi.getUserId();
//                int realPrice = commonMapper.getItemPrice(bizApi);
//                int userPoint = commonMapper.getUserPoint(userId);
//                int totalPoint = realPrice + userPoint;
//
//                User user = new User();
//                user.setUserId(userId);
//                user.setPoint(totalPoint);
//
//                int result = commonMapper.userPointManage(user);
//
//                if(result == 1) {
//                    Goods goods = cafeConMapper.getGoodsPriceData(coupon.getGoodsCode());
//
//                    PointLog pointLog = new PointLog();
//                    pointLog.setGubun("P");
//                    pointLog.setLogType("CE");
//                    pointLog.setUserId(userId);
//                    pointLog.setPoint(realPrice);
//                    pointLog.setCurrPoint(totalPoint);
//                    pointLog.setGoodsName(coupon.getGoodsName());
//                    pointLog.setDiscountPrice(goods.getDiscountPrice());
//
//                    cafeConMapper.addCompUserPointLog(pointLog);
//                }
//
//                apiResponse.setCode("C000");
//                apiResponse.setMessage("구매 취소 처리되었습니다.");
//            } else if(code.equals("ERR0805")) {
//                apiResponse.setCode("E0805");
//                apiResponse.setMessage("쿠폰 취소가 불가능합니다.");
//            } else if(code.equals("E0808")) {
//                apiResponse.setCode("E0807");
//                apiResponse.setMessage("이미 취소 처리된 쿠폰입니다.");
//            } else {
//                apiResponse.setCode("E9999");
//                apiResponse.setMessage("취소 중 오류가 발생하였습니다.");
//            }
//
//        } else {
//            apiResponse.setCode("E001");
//            apiResponse.setMessage("조회된 기프티콘이 없습니다.");
//        }
//
//        return apiResponse;
//    }
//
//    // 쿠폰 상세 정보
//    public CouponResponse getCouponsDetailData(Coupon coupon) throws Exception {
//
//        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
//        BizApi bizApi = new BizApi();
//        CouponResponse couponResponse = new CouponResponse();
//
//        bizApi.setApiCode("0201");
//        bizApi.setCustomAuthCode("REALd6f625bd88904490938d49ebb9687a3b");
//        bizApi.setCustomAuthToken("fTy%2Bx%2FJCLaWNusNPs07%2F%2Fw%3D%3D");
//        bizApi.setDevYn("N");
//        bizApi.setTrId(coupon.getTrId());
//
//        String uri = "https://bizapi.giftishow.com/bizApi/coupons" +
//                "?api_code=" + bizApi.getApiCode() +
//                "&custom_auth_code=" + bizApi.getCustomAuthCode() +
//                "&custom_auth_token=" + bizApi.getCustomAuthToken() +
//                "&dev_yn=" + bizApi.getDevYn() +
//                "&tr_id=" + bizApi.getTrId();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonDto = "";
//
//        try {
//            HttpResponse<String> response = client.send(
//                    HttpRequest.newBuilder()
//                            .uri(URI.create(uri))
//                            .header("Content-Type", "application/json")
//                            .POST(HttpRequest.BodyPublishers.ofString(jsonDto)).build()
//                    , HttpResponse.BodyHandlers.ofString());
//
//            JsonNode bodyJson = JsonMapper.builder().build().readTree(response.body());
//
//            log.info("bodyJson: {}", bodyJson);
//
//            CouponDetail couponDetail = objectMapper.treeToValue(bodyJson.findValue("couponInfoList").get(0), CouponDetail.class);
//
//            if(couponDetail != null) {
//                couponResponse.setCouponDetail(couponDetail);
//                couponResponse.setCode("C000");
//                couponResponse.setMessage("상세 정보 조회 완료");
//            } else {
//                couponResponse.setCode("E002");
//                couponResponse.setMessage("상품이 존재하지 않습니다.");
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return couponResponse;
//    }
}
