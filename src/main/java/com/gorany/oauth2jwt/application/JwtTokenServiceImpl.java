package com.gorany.oauth2jwt.application;

import com.gorany.oauth2jwt.application.strategy.jwt.JwtPayloadStrategy;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import com.gorany.oauth2jwt.authorization.JwtTokenProvider;
import com.gorany.oauth2jwt.common.utils.TokenUtils;
import com.gorany.oauth2jwt.exception.JwtExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final List<JwtPayloadStrategy> jwtPayloadStrategies;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtPayload parse(String token) {
        JwtPayload payload = jwtTokenProvider.parseToken(TokenUtils.extractJwtToken(token));

        if (payload.isExpired()) {
            throw new JwtExpiredException();
        }

        return payload;
    }

    @Override
    public String generateJwtToken(JwtPayloadCreate.Request request) {
        JwtPayload jwtPayload = jwtPayloadStrategies.stream()
                                                    .filter(it -> it.match(request.role()))
                                                    .findFirst()
                                                    .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 JWT Payload Role 타입입니다."))
                                                    .create(request);

        return jwtTokenProvider.generateToken(jwtPayload);
    }
}
