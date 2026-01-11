package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.dto.UserActionCreateCommand;

public interface UserActionCommandService {

    void logLoginSuccess(UserActionCreateCommand.LoginSuccess command);

    void logLogout(UserActionCreateCommand.Logout command);
}
