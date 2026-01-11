package com.gorany.oauth2jwt.dto;

import com.gorany.oauth2jwt.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract sealed class UserActionCreateCommand permits UserActionCreateCommand.LoginSuccess, UserActionCreateCommand.Logout {

    private final Long userId;
    private final String ip;
    private final String sessionUid;
    private final String referer;
    private final String userAgent;

    protected UserActionCreateCommand(Long userId, String ip, String sessionUid, String referer, String userAgent) {
        this.userId = userId;
        this.ip = ip;
        this.sessionUid = sessionUid;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public static final class LoginSuccess extends UserActionCreateCommand {

        @Builder
        public LoginSuccess(Long userId, String ip, String sessionUid, String referer, String userAgent) {
            super(userId, ip, sessionUid, referer, userAgent);
        }
    }

    public static final class Logout extends UserActionCreateCommand {

        @Builder
        public Logout(Long userId, String ip, String sessionUid, String referer, String userAgent) {
            super(userId, ip, sessionUid, referer, userAgent);
        }
    }
}
