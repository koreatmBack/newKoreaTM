package com.example.smsSpringTest.service;

import com.example.smsSpringTest.model.EmailMessage;
import com.example.smsSpringTest.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * author : 신기훈
 * date : 2024-11-14
 * comment : 이메일 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    // 이메일 전송
    public ApiResponse sendEmail(EmailMessage emailMessage) throws MessagingException {
        ApiResponse apiResponse = new ApiResponse();
        String certNumber = randomNumber();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setFrom("skh9805@naver.com");
//            mimeMessageHelper.setFrom("koreatm264@nate.com");
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(certNumber, "email"), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            apiResponse.setCode("C000");
            apiResponse.setMessage("메일 전송 성공");
        } catch (MessagingException e) {
            log.info(e.getMessage());
            apiResponse.setCode("E001");
            apiResponse.setMessage("Error!!!");
        }

        return apiResponse;
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    // 인증 번호 생성
    public static String randomNumber(){
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        return String.valueOf(randomNumber);
    }
}
