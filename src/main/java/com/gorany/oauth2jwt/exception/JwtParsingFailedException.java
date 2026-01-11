package com.gorany.oauth2jwt.exception;

public class JwtParsingFailedException extends BusinessException {

    public JwtParsingFailedException() {
        super(ErrorCode.JWT_PARSING_FAILED, ErrorCode.JWT_PARSING_FAILED.getDescription());
    }

    public JwtParsingFailedException(Throwable cause) {
        super(ErrorCode.JWT_PARSING_FAILED, ErrorCode.JWT_PARSING_FAILED.getDescription(), cause);
    }
}
