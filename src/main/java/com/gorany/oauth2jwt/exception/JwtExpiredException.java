package com.gorany.oauth2jwt.exception;

public class JwtExpiredException extends BusinessException {

    public JwtExpiredException() {
        super(ErrorCode.JWT_EXPIRED, ErrorCode.JWT_EXPIRED.getDescription());
    }

    public JwtExpiredException(Throwable cause) {
        super(ErrorCode.JWT_EXPIRED, ErrorCode.JWT_EXPIRED.getDescription(), cause);
    }
}
