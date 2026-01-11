package com.gorany.oauth2jwt.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

public record HttpServletRequestDto(String sessionUid, String clientIp, String referer, String userAgent) {

    private static final String REFERER = "Referer";
    private static final String USER_AGENT = "User-Agent";

    public static HttpServletRequestDto from(HttpServletRequest request) {
        String sessionUid = WebUtils.getSessionId(request);
        String clientIp = request.getRemoteAddr();
        String referer = request.getHeader(REFERER);
        String userAgent = request.getHeader(USER_AGENT);

        return new HttpServletRequestDto(sessionUid, clientIp, referer, userAgent);
    }
}
