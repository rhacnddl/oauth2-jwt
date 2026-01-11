package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.event.LoginEvent;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.RefreshToken;
import com.gorany.oauth2jwt.authorization.AdminAccessControl;
import com.gorany.oauth2jwt.authorization.PathVariableExtractor;
import com.gorany.oauth2jwt.common.utils.TokenUtils;
import com.gorany.oauth2jwt.common.utils.SecureStringUtils;
import com.gorany.oauth2jwt.domain.User;
import com.gorany.oauth2jwt.domain.repository.UserRepository;
import com.gorany.oauth2jwt.dto.AdminLoginRequest;
import com.gorany.oauth2jwt.dto.HttpServletRequestDto;
import com.gorany.oauth2jwt.dto.TokenDto;
import com.gorany.oauth2jwt.enums.Role;
import com.gorany.oauth2jwt.exception.ForbiddenException;
import com.gorany.oauth2jwt.exception.InvalidCredentialsException;
import com.gorany.oauth2jwt.exception.InvalidRefreshTokenException;
import com.gorany.oauth2jwt.exception.JwtInvalidTokenException;
import com.gorany.oauth2jwt.exception.RefreshTokenNotFoundException;
import com.gorany.oauth2jwt.exception.UserNotFoundException;
import com.gorany.oauth2jwt.infrastructure.redis.AdminAuthBlackListRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminOauth2ServiceImpl implements AdminOauth2Service {

    private final UserRepository userRepository;

    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    private final AdminAuthBlackListRedisRepository adminAuthBlackListRedisRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public TokenDto login(AdminLoginRequest request, HttpServletRequestDto httpRequest) {
        User user = userRepository.findByEmail(request.email())
                                  .orElseThrow(InvalidCredentialsException::new);

        if (!SecureStringUtils.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        user.recordAccessTime();

        Long userId = user.getId();

        String refreshToken = refreshTokenService.create(userId);

        String jwtToken = jwtTokenService.generateJwtToken(new JwtPayloadCreate.Request(userId, user.getRole()));

        eventPublisher.publishEvent(new LoginEvent.Success(userId, user.getRole(), httpRequest));

        return TokenDto.builder()
                       .jwtToken(jwtToken)
                       .refreshToken(refreshToken)
                       .build();
    }

    @Override
    public JwtPayload authorize(String authorization, HttpMethod method, String path) {
        boolean isBlacklisted = adminAuthBlackListRedisRepository.isBlacklisted(TokenUtils.extractJwtToken(authorization));

        if (isBlacklisted) {
            throw new JwtInvalidTokenException();
        }

        JwtPayload payload = jwtTokenService.parse(authorization);

        if (payload.role() == Role.MASTER) {
            return payload;
        }

        validateRolePermission(payload.role(), method, path);

        validateSpotAccess(payload, path);

        return payload;
    }

    @Override
    public TokenDto refresh(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken newRefreshToken = refreshTokenService.rotate(refreshToken);

        User user = userRepository.findById(newRefreshToken.userId())
                                  .orElseThrow(UserNotFoundException::new);

        JwtPayloadCreate.Request jwtPayloadCreateRequest = new JwtPayloadCreate.Request(user.getId(), user.getRole());

        String jwtToken = jwtTokenService.generateJwtToken(jwtPayloadCreateRequest);

        return TokenDto.builder()
                       .jwtToken(jwtToken)
                       .refreshToken(newRefreshToken.token())
                       .build();
    }

    @Override
    public void logout(String authorization, String refreshToken, HttpServletRequestDto httpRequest) {
        try {
            JwtPayload payload = jwtTokenService.parse(authorization);
            String jwtToken = TokenUtils.extractJwtToken(authorization);

            adminAuthBlackListRedisRepository.save(jwtToken, payload.getRemainingSeconds());

            eventPublisher.publishEvent(new LoginEvent.Logout(payload.userId(), payload.role(), httpRequest));
        } catch (Exception e) {
            log.warn("Failed to parse JWT during logout: {}", e.getMessage());
        }

        try {
            refreshTokenService.delete(refreshToken);
        } catch (RefreshTokenNotFoundException e) {
            log.warn("RefreshToken not found during logout: refreshToken={}", refreshToken);
        }
    }

    private void validateRolePermission(Role role, HttpMethod method, String path) {
        if (!AdminAccessControl.isAuthorized(role, method, path)) {
            log.warn("Forbidden access attempt: role={}, method={}, path={}", role, method.name(), path);

            throw new ForbiddenException();
        }
    }

    private void validateSpotAccess(JwtPayload payload, String path) {
        PathVariableExtractor.extractSpotId(path)
                             .ifPresent(spotId -> {
                                 if (!payload.authorizedSpots().contains(spotId)) {
                                     throw new ForbiddenException();
                                 }
                             });
    }
}
