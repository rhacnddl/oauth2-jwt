package com.gorany.oauth2jwt.application.event;

import com.gorany.oauth2jwt.application.UserActionCommandService;
import com.gorany.oauth2jwt.dto.UserActionCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LoginEventListener {

    private final UserActionCommandService userActionCommandService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLoginSuccessEvent(LoginEvent.Success event) {
        UserActionCreateCommand.LoginSuccess command = UserActionCreateCommand.LoginSuccess.builder()
                                                                                           .userId(event.userId())
                                                                                           .sessionUid(event.httpRequest().sessionUid())
                                                                                           .ip(event.httpRequest().clientIp())
                                                                                           .referer(event.httpRequest().referer())
                                                                                           .userAgent(event.httpRequest().userAgent())
                                                                                           .build();

        userActionCommandService.logLoginSuccess(command);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLogoutEvent(LoginEvent.Logout event) {
        UserActionCreateCommand.Logout command = UserActionCreateCommand.Logout.builder()
                                                                               .userId(event.userId())
                                                                               .sessionUid(event.httpRequest().sessionUid())
                                                                               .ip(event.httpRequest().clientIp())
                                                                               .referer(event.httpRequest().referer())
                                                                               .userAgent(event.httpRequest().userAgent())
                                                                               .build();

        userActionCommandService.logLogout(command);
    }
}
