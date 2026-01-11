package com.gorany.oauth2jwt.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getDescription());
    }
}
