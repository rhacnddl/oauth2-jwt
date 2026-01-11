package com.gorany.oauth2jwt.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_CREDENTIALS(400, "AUTH-009", "이메일 또는 비밀번호가 일치하지 않는 경우"),
    JWT_INVALID_TOKEN(401, "AUTH-015", "유효하지 않은 JWT 토큰인 경우"),
    JWT_EXPIRED(401, "AUTH-016", "만료된 JWT 토큰인 경우"),
    JWT_PARSING_FAILED(401, "AUTH-017", "JWT 토큰 파싱에 실패한 경우"),
    JWT_GENERATION_FAILED(500, "AUTH-018", "JWT 토큰 생성에 실패한 경우"),
    FORBIDDEN(403, "AUTH-019", "권한이 없는 경우"),
    INVALID_REFRESH_TOKEN(400, "AUTH-020", "유효하지 않은 리프레시 토큰이거나 비어있는 경우"),
    REFRESH_TOKEN_NOT_FOUND(404, "AUTH-022", "리프레시 토큰을 찾을 수 없는 경우"),
    USER_NOT_FOUND(404, "AUTH-024", "사용자를 찾을 수 없는 경우"),
    ;

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
