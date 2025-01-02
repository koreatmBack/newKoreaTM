package com.example.smsSpringTest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : 신기훈
 * date : 2024-01-02
 * comment : 메인 페이지 접근 불가능하게하는 controller
 */

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping("/root")
    public ResponseEntity<String> handleRoot() {
        return ResponseEntity.status(404).body("404 Not Found - Root Access Forbidden");
    }
}