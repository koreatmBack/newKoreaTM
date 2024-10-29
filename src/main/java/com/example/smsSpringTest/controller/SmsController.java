package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.model.SmsForm;
import com.example.smsSpringTest.model.jobsite.CertSMS;
import com.example.smsSpringTest.model.response.ApiResponse;
import com.example.smsSpringTest.model.response.SmsResponse;
import com.example.smsSpringTest.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : sms 전송 controller
 */

@RestController
@RequestMapping({"/api/v1/formMail", "/v1/formMail"})
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;


    @GetMapping("/smsTest") //최초 폼으로 접속하는 url
    public String smsTest(@ModelAttribute SmsForm smsForm){
        return "smsTest";
    }

//    @PostMapping("/sendSms") // 폼에서 데이터 전송하는 url
//    public String sendSms(@ModelAttribute SmsForm smsForm, Model model){
//
//        try {
//           String result = smsService.sendSms(smsForm);
//           if(result.contains("성공")){
//               log.info("smsForm = " + smsForm);
//               model.addAttribute("result" , result);
//               return "success"; // 성공 페이지 반환
//           } else {
//               model.addAttribute("result" , result);
//               return "fail";
//           }
//        } catch (IOException e) {
//            // 예외 처리
//            log.info("IOException 발생: " + e.getMessage());
//            return "fail"; // 에러 페이지 반환
//        }
//    }

    @PostMapping("/sendSms") // 폼에서 데이터 전송하는 url
    public SmsResponse sendSms(@RequestBody SmsForm smsForm) throws Exception{
        log.info("컨트롤러 smsForm = " + smsForm);
        SmsResponse smsResponse = new SmsResponse();

        smsResponse = smsService.sendSms(smsForm);


        return smsResponse;
    }

    // 잡사이트용 본인인증 (문자 전송)
    @PostMapping("/cert/sms")
    public ApiResponse certificateSMS(@RequestBody CertSMS certSMS) throws IOException {
        return smsService.certificateSMS(certSMS);
    }
}
