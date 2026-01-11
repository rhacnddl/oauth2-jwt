package com.gorany.oauth2jwt.application.strategy.jwt.dto;

import com.gorany.oauth2jwt.enums.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

public sealed interface JwtPayloadCreate {

    record Request(Long userId, Role role) implements JwtPayloadCreate {

    }

    @Builder
    record Command(Long userId,
                   Role role,
                   Set<Long> authorizedSpots,
                   LocalDateTime issuedAt,
                   LocalDateTime expiredAt) implements JwtPayloadCreate {

    }

}
