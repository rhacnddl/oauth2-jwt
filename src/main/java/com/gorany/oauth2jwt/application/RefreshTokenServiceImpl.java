package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.RefreshToken;
import com.gorany.oauth2jwt.exception.RefreshTokenNotFoundException;
import com.gorany.oauth2jwt.infrastructure.redis.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public String create(Long userId) {
        return generateUniqueRefreshToken(userId);
    }

    @Override
    public RefreshToken rotate(String refreshToken) {
        Long userId = refreshTokenRedisRepository.findByToken(refreshToken)
                                                 .orElseThrow(RefreshTokenNotFoundException::new);

        String token = generateUniqueRefreshToken(userId);

        refreshTokenRedisRepository.delete(refreshToken);

        return new RefreshToken(token, userId);
    }

    @Override
    public void delete(String refreshToken) {
        refreshTokenRedisRepository.delete(refreshToken);
    }

    private String generateUniqueRefreshToken(Long userId) {
        String token;
        final int maxRetries = 5;
        int attempt = 0;
        do {
            if (attempt >= maxRetries) {
                throw new IllegalStateException("최대 재시도 횟수(" + maxRetries + ") 초과 후에도 고유한 리프레시 토큰을 생성하지 못했습니다.");
            }

            token = UUID.randomUUID().toString();
            attempt++;
        } while (!refreshTokenRedisRepository.save(token, userId));

        return token;
    }
}
