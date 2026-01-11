package com.gorany.oauth2jwt.exception;

public class JwtGenerationFailedException extends BusinessException {

    public JwtGenerationFailedException() {
        super(ErrorCode.JWT_GENERATION_FAILED, ErrorCode.JWT_GENERATION_FAILED.getDescription());
    }
    
    public JwtGenerationFailedException(Throwable cause) {
        super(ErrorCode.JWT_GENERATION_FAILED, ErrorCode.JWT_GENERATION_FAILED.getDescription(), cause);
    }
}
