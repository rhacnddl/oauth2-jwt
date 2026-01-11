package com.gorany.oauth2jwt.presentation;

import com.gorany.oauth2jwt.application.AdminOauth2Service;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.common.utils.TokenConstants;
import com.gorany.oauth2jwt.common.utils.CookiesUtils;
import com.gorany.oauth2jwt.dto.AdminLoginRequest;
import com.gorany.oauth2jwt.dto.HttpServletRequestDto;
import com.gorany.oauth2jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminOauth2Service adminOauth2Service;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AdminLoginRequest request,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse) {

        TokenDto tokenDto = adminOauth2Service.login(request, HttpServletRequestDto.from(httpRequest));

        httpResponse.addCookie(CookiesUtils.getRefreshTokenCookie(tokenDto.refreshToken()));

        return ResponseEntity.ok(tokenDto.jwtToken());
    }

    @Operation(summary = "Authorization")
    @GetMapping("/authorization")
    public ResponseEntity<Long> authorize(@RequestHeader("Authorization") String authorization,
                                          @RequestHeader("X-Method") String method,
                                          @RequestHeader("X-Path") String path) {

        JwtPayload payload = adminOauth2Service.authorize(authorization, HttpMethod.valueOf(method.toUpperCase()), path);

        return ResponseEntity.ok(payload.userId());
    }

    @Operation(summary = "JWT 재발급")
    @PostMapping("/auth/refresh")
    public ResponseEntity<String> refresh(@CookieValue(value = TokenConstants.REFRESH_TOKEN, defaultValue = StringUtils.EMPTY) String refreshToken,
                                          HttpServletResponse httpResponse) {

        TokenDto tokenDto = adminOauth2Service.refresh(refreshToken);

        httpResponse.addCookie(CookiesUtils.getRefreshTokenCookie(tokenDto.refreshToken()));

        return ResponseEntity.ok(tokenDto.jwtToken());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization,
                                       @CookieValue(value = TokenConstants.REFRESH_TOKEN, required = false) String refreshToken,
                                       HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) {

        adminOauth2Service.logout(authorization, refreshToken, HttpServletRequestDto.from(httpRequest));

        httpResponse.addCookie(CookiesUtils.getExtinctedRefreshTokenCookie());

        return ResponseEntity.noContent().build();
    }
}
