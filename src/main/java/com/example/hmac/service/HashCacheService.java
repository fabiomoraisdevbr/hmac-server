package com.example.hmac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class HashCacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PREFIX = "hmac:";
    private static final Duration TTL = Duration.ofMinutes(15);

    public boolean exists(String hash) {
        System.out.println("Checking if hash exists in cache: " + hash);
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(PREFIX + hash)
        );


    }

    public void save(String hash) {
        redisTemplate.opsForValue().set(
                PREFIX + hash,
                "1",
                TTL
        );

        System.out.println("Hash saved to cache: " + hash);
    }

}