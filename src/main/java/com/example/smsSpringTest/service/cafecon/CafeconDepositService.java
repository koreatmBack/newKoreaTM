package com.example.smsSpringTest.service.cafecon;

import com.example.smsSpringTest.mapper.cafecon.CafeconCommonMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconDepositMapper;
import com.example.smsSpringTest.mapper.cafecon.CafeconUserMapper;
import com.example.smsSpringTest.model.cafecon.CafeUser;
import com.example.smsSpringTest.model.cafecon.Deposit;
import com.example.smsSpringTest.model.cafecon.PointLog;
import com.example.smsSpringTest.model.response.ApiResponse;
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
