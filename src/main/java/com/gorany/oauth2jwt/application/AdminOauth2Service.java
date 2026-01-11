package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.dto.AdminLoginRequest;
import com.gorany.oauth2jwt.dto.HttpServletRequestDto;
import com.gorany.oauth2jwt.dto.TokenDto;
import org.springframework.http.HttpMethod;

public interface AdminOauth2Service {

    TokenDto login(AdminLoginRequest request, HttpServletRequestDto httpRequest);

    JwtPayload authorize(String authorization, HttpMethod method, String path);

    TokenDto refresh(String refreshToken);

    void logout(String authorization, String refreshToken, HttpServletRequestDto httpRequest);
}
