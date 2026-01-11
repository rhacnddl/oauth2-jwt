package com.gorany.oauth2jwt.application.event;

import com.gorany.oauth2jwt.dto.HttpServletRequestDto;
import com.gorany.oauth2jwt.enums.Role;
import lombok.Builder;

public sealed interface LoginEvent permits LoginEvent.Success, LoginEvent.Logout {

    @Builder
    record Success(Long userId, Role role, HttpServletRequestDto httpRequest) implements LoginEvent {

    }

    @Builder
    record Logout(Long userId, Role role, HttpServletRequestDto httpRequest) implements LoginEvent {

    }
}
