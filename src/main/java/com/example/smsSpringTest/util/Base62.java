package com.example.smsSpringTest.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
//@Transactional(rollbackFor = Exception.class)
@Slf4j
public class Base62 {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // Base62 인코딩
    public String encode(long number) {
        if (number == 0) {
            return Character.toString(BASE62_CHARS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % 62);
            sb.append(BASE62_CHARS.charAt(remainder));
            number /= 62;
        }
        return sb.reverse().toString();
    }

    // URL을 해시값으로 변환
    private long hashUrl(String url) {
        try {
            // SHA-256 알고리즘으로 URL 해싱
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes(StandardCharsets.UTF_8));

            // 해시의 일부를 사용 (앞 8바이트 -> long 변환)
            long hashValue = 0;
            for (int i = 0; i < 8; i++) {
                hashValue = (hashValue << 8) | (hash[i] & 0xFF);
            }

            // 음수 방지를 위해 절대값 반환
            return Math.abs(hashValue);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.", e);
        }
    }

    public long decode(String str) {
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result * 62 + BASE62_CHARS.indexOf(str.charAt(i));
        }
        return result;
    }

    // 단축 URL 생성
    public String generateShortUrl(String originalUrl) {
        long hashValue = hashUrl(originalUrl); // URL 해싱
        return encode(hashValue);             // Base62로 변환
    }

}
