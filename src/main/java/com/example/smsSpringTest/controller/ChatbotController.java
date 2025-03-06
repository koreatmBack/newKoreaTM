package com.example.smsSpringTest.controller;

import com.example.smsSpringTest.service.ClovaChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * author : 신기훈
 * date : 2025-03-06
 * comment : 챗봇 controller
 */

@RequiredArgsConstructor
//@RestController
@Controller
@RequestMapping({"/api/v1/chatbot", "/v1/chatbot"})
@Slf4j
public class ChatbotController {

    private final ClovaChatbotService clovaChatbotService;

    @GetMapping("")
    public String chatbotPage(Model model){
//        model.addAttribute("chatbotKey", "SlVVUUZReHlkZ05ab0J2eFl4eERxTkhzaG1teWVDQ3Y=");
//        model.addAttribute("domainId", "17047");
//        model.addAttribute("domainCode", "ChatBotSKH");
//        model.addAttribute("domainName", "ChatBotSKH");
//        model.addAttribute("iframeType", "modal");
//        model.addAttribute("language", "ko");
//        return "clova"; // chatbot.html 연결
        return "chatbot"; // chatbot.html 연결
    }

    @PostMapping("/send")
    @ResponseBody
//    @RequestBody
    public String sendMessage(@RequestParam String message) throws Exception {
//        log.info("Received message: " + message);
        return clovaChatbotService.sendMessage(message);
    }

    @PostMapping("/open")
    @ResponseBody
//    @RequestBody
    public String openMessage() throws Exception {
        return clovaChatbotService.openMessage();
    }
}
