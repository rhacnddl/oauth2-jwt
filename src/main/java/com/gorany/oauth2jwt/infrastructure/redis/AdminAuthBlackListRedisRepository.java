package com.gorany.oauth2jwt.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminAuthBlackListRedisRepository {

    private static final String KEY_PREFIX = "jwt:blacklist:";
    private static final Boolean BLACKLISTED_VALUE = true;

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String jwtToken, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            log.warn("TTL is not positive, skipping blacklist save: ttlSeconds={}", ttlSeconds);
            return;
        }

        String key = KEY_PREFIX + jwtToken;
        redisTemplate.opsForValue().set(key, BLACKLISTED_VALUE, ttlSeconds, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String jwtToken) {
        String key = KEY_PREFIX + jwtToken;

        return redisTemplate.hasKey(key);
    }
}
