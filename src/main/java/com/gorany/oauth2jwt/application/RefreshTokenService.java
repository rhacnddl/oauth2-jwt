package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.RefreshToken;

public interface RefreshTokenService {

    String create(Long userId);

    RefreshToken rotate(String refreshToken);

    void delete(String refreshToken);
}
