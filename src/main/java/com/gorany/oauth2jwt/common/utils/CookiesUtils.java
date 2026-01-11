package com.gorany.oauth2jwt.common.utils;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookiesUtils {

    private static final int ONE_WEEK = 60 * 60 * 24 * 7;
    private static final String ROOT_PATH = "/";

    public static Cookie getRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(TokenConstants.REFRESH_TOKEN, refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(ROOT_PATH);
        cookie.setMaxAge(ONE_WEEK);

        return cookie;
    }

    public static Cookie getExtinctedRefreshTokenCookie() {
        Cookie cookie = new Cookie(TokenConstants.REFRESH_TOKEN, null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(ROOT_PATH);
        cookie.setMaxAge(0);

        return cookie;
    }
}
