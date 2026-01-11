package com.gorany.oauth2jwt.exception;

public class RefreshTokenNotFoundException extends BusinessException {

    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND, ErrorCode.REFRESH_TOKEN_NOT_FOUND.getDescription());
    }
}
