package com.gorany.oauth2jwt.exception;

public class ForbiddenException extends BusinessException {

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getDescription());
    }
}
