package com.gorany.oauth2jwt.common.utils;

import com.gorany.oauth2jwt.exception.JwtInvalidTokenException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

    public static String extractJwtToken(String token) {
        if (token == null || !token.startsWith(TokenConstants.BEARER)) {
            throw new JwtInvalidTokenException();
        }

        return token.substring(TokenConstants.BEARER_INDEX);
    }
}
