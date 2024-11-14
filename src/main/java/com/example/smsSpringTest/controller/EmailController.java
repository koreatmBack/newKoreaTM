package com.example.smsSpringTest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : 신기훈
 * date : 2024-11-14
 * comment : 이메일 controller
 */
@RestController
@RequestMapping({"/api/v1/jobsite/user", "/v1/jobsite/user"})
@Slf4j
@RequiredArgsConstructor
public class EmailController {
}
