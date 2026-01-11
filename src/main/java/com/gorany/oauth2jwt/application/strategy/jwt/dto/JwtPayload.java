package com.gorany.oauth2jwt.application.strategy.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gorany.oauth2jwt.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Builder(access = AccessLevel.PROTECTED)
public record JwtPayload(
    Long userId,
    Role role,

    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    LocalDateTime issuedAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    LocalDateTime expiredAt,

    Set<Long> authorizedSpots
) {

    public static JwtPayload from(JwtPayloadCreate.Command command) {
        return JwtPayload.builder()
                         .userId(command.userId())
                         .role(command.role())
                         .issuedAt(command.issuedAt())
                         .expiredAt(command.expiredAt())
                         .authorizedSpots(command.authorizedSpots())
                         .build();
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    @JsonIgnore
    public Long getRemainingSeconds() {
        long seconds = Duration.between(LocalDateTime.now(), expiredAt)
                               .getSeconds();

        return Math.max(seconds, 0L);
    }
}
