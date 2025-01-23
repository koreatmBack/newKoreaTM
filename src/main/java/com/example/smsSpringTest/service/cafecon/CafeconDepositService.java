package com.example.smsSpringTest.service.cafecon;

import com.example.smsSpringTest.mapper.cafecon.CafeconCommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconDepositMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.model.Paging;
import com.example.smsSpringTest.model.cafecon.CafeUser;
import com.example.smsSpringTest.model.cafecon.Deposit;
import com.example.smsSpringTest.model.cafecon.PointLog;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.cafecon.CafeconResponse;
import com.example.smsSpringTest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * author : 신기훈
 * date : 2025-01-22
 * comment : 카페콘 입금 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CafeconDepositService {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final CafeconCommonMapper cafeconCommonMapper;
    private final CafeconUserMapper cafeconUserMapper;
    private final CafeconDepositMapper depositMapper;
    private final CafeconCommonService cafeconCommonService;


   // 입금 정보 등록
   public ApiResponse addDepositInfo(Deposit deposit) throws Exception {
       ApiResponse apiResponse = new ApiResponse();

       try {
           String depositNum = generateTransactionId();
           deposit.setNum(depositNum);
           deposit.setChargePoint(deposit.getDepositAmount()); // 충전 포인트 = 입금 금액

           int addDepositInfo = depositMapper.addDepositInfo(deposit);
           if (addDepositInfo == 1) {
               apiResponse.setCode("C000");
               apiResponse.setMessage("입금 요청 성공");
           } else {
               apiResponse.setCode("E001");
               apiResponse.setMessage("입금 요청 실패");
           }
       } catch (Exception e) {
           apiResponse.setCode("E001");
           apiResponse.setMessage("Error!!!");
           log.info(e.getMessage());
       }
       return apiResponse;
   }

   // 상태 변경하기 (보류확인 or 충전완료)
   public ApiResponse changeStatus(Deposit deposit) throws Exception {
       ApiResponse apiResponse = new ApiResponse();

       try {
           int changeStatus = depositMapper.changeStatus(deposit);
           if (changeStatus == 1) {
               // 상태 변경 성공
               if (deposit.getStatus().equals("충전완료")) {
                   // 충전 완료면 카페콘 포인트 로그 찍어야함
                   Deposit deposit1 = depositMapper.findOne(deposit);
                   String userId = deposit1.getUserId(); // 회원 id
                   int chargePoint = deposit1.getChargePoint(); // 충전할 포인트
                   int userPoint = cafeconUserMapper.getUserPoint(userId); // 회원 보유 포인트
                   int currPoint = chargePoint + userPoint; // 충전포인트 + 보유 포인트
                   log.info("충전 포인트 = " + chargePoint);
                   log.info("회원 충전 전 보유포인트 = " + userPoint);
                   log.info("회원 충전 후 보유포인트 = " + currPoint);

                   // 회원 DB 포인트 수정
                   CafeUser user = new CafeUser();
                   user.setUserId(userId);
                   user.setPoint(currPoint);
                   int updateUserPoint = cafeconUserMapper.updatePoint(user);
                   log.info("updateUserPoint = " + updateUserPoint);

                   PointLog pointLog = new PointLog();
                   pointLog.setUserId(userId);
                   pointLog.setGubun("P"); // 지급
                   pointLog.setPoint(chargePoint); // 충전 포인트
                   pointLog.setCurrPoint(currPoint); // 지급 후 현재 포인트
                   pointLog.setLogType("AP"); // AP = 관리자 지급

                   // coupon 테이블에서 orderNo 찾기
                   LocalDate now = LocalDate.now();
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                   String date = now.format(formatter);
                   String findOrderNoCp = cafeconCommonMapper.findOrderNoCoupon(date);
                   String orderNo = "";
                   if(findOrderNoCp != null) {
                       // 만약 쿠폰 테이블에 값이 있으면
                       String findOrderNoPl = cafeconCommonMapper.findOrderNoPointLog(date);
                       if(findOrderNoPl != null) {
                           // 포인트 로그에도 오늘 날짜의 order_no 값이 있으면
                           String remainCp = findOrderNoCp.replace(date, ""); // "0000000_"
                           String remainPl = findOrderNoPl.replace(date, "");
                           int cp = Integer.parseInt(remainCp);
                           int pl = Integer.parseInt(remainPl);
                           String newTrId = "";
                           if(cp > pl) {
                               //만약 쿠폰 테이블이 포인트로그보다 더 클때
                               int nextNumber = cp +1;
                               String newFormat = String.format("%08d", nextNumber);
                               orderNo = date + newFormat; // 2025012300000005
                           } else {
                               // 포인트로그 테이블이 쿠폰 테이블보다 크거나 같을때
                               int nextNumber = pl + 1;
                               String newFormat = String.format("%08d", nextNumber);
                               orderNo = date + newFormat; // 2025012300000005
                           }
                       } else {
                           // 포인트 로그에 오늘 날짜의 order_no 값이 없으면
                           String trId = cafeconCommonMapper.getTrId();
                           if (trId == null) {

                               String num = "00000001";

                               trId = "cafe_" + date + "_" + num;
                           }
                           orderNo = cafeconCommonService.addOrderNo(trId);
                       }
                   } else {
                       // 쿠폰 테이블에 order_no이 없으면
                       String trId = cafeconCommonMapper.getTrId();
                       if (trId == null) {

                           String num = "00000001";

                           trId = "cafe_" + date + "_" + num;
                       }
                       orderNo = cafeconCommonService.addOrderNo(trId);
                   }

                   pointLog.setOrderNo(orderNo);

                   int addPointLog = cafeconCommonMapper.addCompUserPointLog(pointLog);
                   if (addPointLog == 1) {
                       apiResponse.setCode("C000");
                       apiResponse.setMessage("충전완료 + 로그 추가 성공");
                   } else {
                       apiResponse.setCode("C003");
                       apiResponse.setMessage("충전완료 , 로그 추가 실패");
                   }
               } else {
                   apiResponse.setCode("C000");
                   apiResponse.setMessage("보류확인으로 변경 성공");
               }
           } else {
               apiResponse.setCode("C003");
               apiResponse.setMessage("충전완료 처리 실패");
           }
       } catch (Exception e) {
           apiResponse.setCode("E001");
           apiResponse.setMessage("Error!!!");
           log.info(e.getMessage());
       }
       return apiResponse;
   }

    // 모든 회원의 입금 내역 조회
    public CafeconResponse allDepositList(Paging paging) throws Exception {
       CafeconResponse cafeconResponse = new CafeconResponse();

       try {
           int page = paging.getPage(); // 현재 페이지
           int size = paging.getSize(); // 한 페이지에 표시할 수
           int offset = (page - 1) * size; // 시작 위치
           int totalCount = depositMapper.countAllDeposit();

           paging.setOffset(offset);
           cafeconResponse.setDepositList(depositMapper.allDepositList(paging));
           if(cafeconResponse.getDepositList() != null && !cafeconResponse.getDepositList().isEmpty()) {
               int totalPages = (int) Math.ceil((double) totalCount / size);
               cafeconResponse.setTotalPages(totalPages);
               cafeconResponse.setTotalCount(totalCount);
               cafeconResponse.setCode("C000");
               cafeconResponse.setMessage("모든 회원의 입금 내역 조회 성공");
           } else {
               cafeconResponse.setCode("E001");
               cafeconResponse.setMessage("모든 회원의 입금 내역 조회 실패");
           }
       } catch (Exception e) {
           cafeconResponse.setCode("E001");
           cafeconResponse.setMessage("Error !!!!");
           log.info(e.getMessage());
       }
       return cafeconResponse;
    }

    // 회원 한 명의 입금 내역 조회
    public CafeconResponse userDepositList(Deposit deposit) throws Exception {
       CafeconResponse cafeconResponse = new CafeconResponse();
        try {
            int page = deposit.getPage(); // 현재 페이지
            int size = deposit.getSize(); // 한 페이지에 표시할 수
            int offset = (page - 1) * size; // 시작 위치
            int totalCount = depositMapper.countUserDeposit(deposit);

            deposit.setOffset(offset);
            cafeconResponse.setDepositList(depositMapper.userDepositList(deposit));
            if(cafeconResponse.getDepositList() != null && !cafeconResponse.getDepositList().isEmpty()) {
                int totalPages = (int) Math.ceil((double) totalCount / size);
                cafeconResponse.setTotalPages(totalPages);
                cafeconResponse.setTotalCount(totalCount);
                cafeconResponse.setCode("C000");
                cafeconResponse.setMessage("회원 한 명의 입금 내역 조회 성공");
            } else {
                cafeconResponse.setCode("E001");
                cafeconResponse.setMessage("회원 한 명의 입금 내역 조회 실패");
            }
        } catch (Exception e) {
            cafeconResponse.setCode("E001");
            cafeconResponse.setMessage("Error !!!!");
            log.info(e.getMessage());
        }
       return cafeconResponse;
    }


   // 거래번호 생성
   public String generateTransactionId() {
       // 1. 오늘 날짜를 기반으로 접두사 생성
       String datePrefix = "P" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

       // 2. 오늘 날짜의 가장 큰 시퀀스 값 가져오기
       Integer maxSequence = depositMapper.findMaxSequenceForToday(datePrefix);

       // 3. 시퀀스 값을 증가시키기
       int nextSequence = (maxSequence == null) ? 1 : maxSequence + 1;

       // 4. 거래번호 생성 (6자리로 패딩)
       return datePrefix + String.format("%06d", nextSequence);
   }




}
