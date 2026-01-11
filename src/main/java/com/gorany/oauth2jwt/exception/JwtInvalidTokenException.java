package com.gorany.oauth2jwt.exception;

public class JwtInvalidTokenException extends BusinessException {

    public JwtInvalidTokenException() {
        super(ErrorCode.JWT_INVALID_TOKEN, ErrorCode.JWT_INVALID_TOKEN.getDescription());
    }

    public JwtInvalidTokenException(Throwable cause) {
        super(ErrorCode.JWT_INVALID_TOKEN, ErrorCode.JWT_INVALID_TOKEN.getDescription(), cause);
    }
}
