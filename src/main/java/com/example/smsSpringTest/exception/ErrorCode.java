package com.example.smsSpringTest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * author : 신기훈
 * date : 2024-09-30
 * comment : 에러 코드
 */

@AllArgsConstructor
public enum ErrorCode {

    E001("E001"),
    E002("E002"),
    E003("E003"),
    E004("E004"),
    E005("E005"),
    E006("E006"),
    E007("E007"),
    E008("E008"),
    E009("E009"),
    E010("E010"),
    E011("E011"),
    E012("E012"),
    E013("E013"),
    E014("E014"),
    E400("E400"),
    E401("E401"),
    E402("E402"),
    E403("E403"),
    E404("E404"),
    E405("E405"),
    E406("E406"),
    E409("E409"),
    E501("E501"),
    E502("E502"),
    E503("E503"),
    E504("E504"),
    E505("E505");

    public static ErrorCode getErrorByCode(final String code) {
        return Arrays.stream(ErrorCode.values())
                .filter(item -> item.code.equals(code))
                .findFirst()
                .orElse(ErrorCode.E504);
    }

    @Getter
    private String code;
}
