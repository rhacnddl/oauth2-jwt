package com.gorany.oauth2jwt.domain;

import com.gorany.oauth2jwt.dto.UserActionCreateCommand;
import com.gorany.oauth2jwt.dto.UserActionCreateCommand.LoginSuccess;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Getter
@Entity
@Table(name = "user_actions")
@DynamicUpdate
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserAction extends BaseEntity {

    private static final String LOGIN_SUCCESS_DEFAULT_ACTION = "로그인 성공";
    private static final String LOGOUT_DEFAULT_ACTION = "로그아웃";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "session_uid", nullable = false)
    private String sessionUid;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "referer")
    private String referer;

    @Column(name = "user_agent")
    private String userAgent;

    @Builder
    public UserAction(String sessionUid, Long userId, String action, String referer, String userAgent) {
        this.sessionUid = sessionUid;
        this.userId = userId;
        this.action = action;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public static UserAction createAuthLog(UserActionCreateCommand command) {

        String sessionUid = command.getSessionUid() == null ? UUID.randomUUID().toString() : command.getSessionUid();

        String action = command instanceof LoginSuccess ? LOGIN_SUCCESS_DEFAULT_ACTION : LOGOUT_DEFAULT_ACTION;

        return UserAction.builder()
                .sessionUid(sessionUid)
                .userId(command.getUserId())
                .referer(command.getReferer())
                .userAgent(command.getUserAgent())
                .action(action)
                .build();
    }
}
