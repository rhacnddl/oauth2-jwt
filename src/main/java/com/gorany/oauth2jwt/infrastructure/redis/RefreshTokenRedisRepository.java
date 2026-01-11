package com.gorany.oauth2jwt.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    private static final int EXPIRATION_DAYS = 14;
    private static final String KEY_PREFIX = "admin:refresh_token:";

    private final RedisTemplate<String, Object> redisTemplate;

    public Optional<Long> findByToken(String token) {
        Object value = redisTemplate.opsForValue().get(KEY_PREFIX + token);

        if (value instanceof Number number) {
            return Optional.of(number.longValue());
        }

        if (value instanceof String s) {
            try {
                return Optional.of(Long.parseLong(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }

    public Boolean save(String token, Long userId) {
        return redisTemplate.opsForValue()
                            .setIfAbsent(KEY_PREFIX + token, userId, EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public void delete(String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }
}
