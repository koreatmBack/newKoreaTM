package com.example.smsSpringTest.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value, Integer expiredTime, TimeUnit timeUnit) {

        if (getValue(key) != null) {
            deleteData(key);
        }

        try {
            if (expiredTime != null || timeUnit != null) {
                redisTemplate.opsForValue().set(key, value, expiredTime, timeUnit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw ExceptionFactory.getException(ErrorCode.E501);
        }
    }

    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }


    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setListData(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
//            throw ExceptionFactory.getException(ErrorCode.E501);
        }
    }

    public Long size(String key) {
        List<Object> allElements = redisTemplate.opsForList().range(key, 0, -1);
        return redisTemplate.opsForList().size(key);
    }

    public String index(String key, Integer i) {
        return redisTemplate.opsForList().index(key, i).toString();
    }
}