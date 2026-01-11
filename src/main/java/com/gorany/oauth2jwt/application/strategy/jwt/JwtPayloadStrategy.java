package com.gorany.oauth2jwt.application.strategy.jwt;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import com.gorany.oauth2jwt.enums.Role;

public interface JwtPayloadStrategy {

    boolean match(Role role);

    JwtPayload create(JwtPayloadCreate.Request request);
}
