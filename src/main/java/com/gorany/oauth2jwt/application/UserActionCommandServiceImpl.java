package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.domain.UserAction;
import com.gorany.oauth2jwt.domain.repository.UserActionRepository;
import com.gorany.oauth2jwt.dto.UserActionCreateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserActionCommandServiceImpl implements UserActionCommandService {

    private final UserActionRepository userActionRepository;

    @Override
    public void logLoginSuccess(UserActionCreateCommand.LoginSuccess command) {
        logUserAction(command);
    }

    @Override
    public void logLogout(UserActionCreateCommand.Logout command) {
        logUserAction(command);
    }

    private void logUserAction(UserActionCreateCommand command) {
        try {
            UserAction userAction = UserAction.createAuthLog(command);

            userActionRepository.save(userAction);
        } catch (Exception e) {
            log.error("Failed to log user action for user_id: {}", command.getUserId(), e);
        }
    }
}
