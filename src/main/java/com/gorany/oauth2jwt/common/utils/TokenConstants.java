package com.gorany.oauth2jwt.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenConstants {

    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String BEARER = "Bearer ";
    public static final int BEARER_INDEX = 7;
}
