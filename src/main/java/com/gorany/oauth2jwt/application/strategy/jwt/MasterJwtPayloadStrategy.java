package com.gorany.oauth2jwt.application.strategy.jwt;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import com.gorany.oauth2jwt.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MasterJwtPayloadStrategy implements JwtPayloadStrategy {

    @Value("${jwt.expiration-mins}")
    private Integer expirationMins;

    @Override
    public boolean match(Role role) {
        return role == Role.MASTER;
    }

    @Override
    public JwtPayload create(JwtPayloadCreate.Request request) {

        JwtPayloadCreate.Command command = JwtPayloadMapper.toCommand(request, Set.of(), expirationMins);

        return JwtPayload.from(command);
    }
}
