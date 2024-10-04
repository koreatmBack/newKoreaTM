package com.example.smsSpringTest.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : api 리턴 코드, 메시지
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String code;
    private String message;

}
