package com.example.smsSpringTest.model.jobsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-10-29
 * comment : sms 인증번호 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cert {
    private String userName;
    private String phone;
    private String smsCode;
    private String email;
    private String emailCode;

    private String site; // 카페콘인지 고알바인지 (문자에 포함할 내용)
    private String managerName; // 카페콘에서 사용할 유저이름 ( 담당자명 )
    private String userId; // 카페콘에서 userId 보낼때도 있음
}
