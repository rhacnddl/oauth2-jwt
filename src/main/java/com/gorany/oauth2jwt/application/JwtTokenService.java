package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;

public interface JwtTokenService {

    JwtPayload parse(String token);

    String generateJwtToken(JwtPayloadCreate.Request request);
}
