package com.gorany.oauth2jwt.application.strategy.jwt;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtPayloadMapper {

    public static JwtPayloadCreate.Command toCommand(JwtPayloadCreate.Request request,
                                                     Set<Long> authorizedSpots,
                                                     Integer expirationMins) {

        LocalDateTime issuedAt = LocalDateTime.now();
        return JwtPayloadCreate.Command.builder()
                                       .userId(request.userId())
                                       .role(request.role())
                                       .authorizedSpots(authorizedSpots)
                                       .issuedAt(issuedAt)
                                       .expiredAt(issuedAt.plusMinutes(expirationMins))
                                       .build();
    }
}
