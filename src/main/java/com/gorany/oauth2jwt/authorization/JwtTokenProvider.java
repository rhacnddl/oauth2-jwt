package com.gorany.oauth2jwt.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.exception.JwtExpiredException;
import com.gorany.oauth2jwt.exception.JwtGenerationFailedException;
import com.gorany.oauth2jwt.exception.JwtInvalidTokenException;
import com.gorany.oauth2jwt.exception.JwtParsingFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String PAYLOAD = "payload";

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final ObjectMapper objectMapper;

    public String generateToken(JwtPayload payload) {
        try {
            SecretKey key = getSigningKey();

            String payloadJson = convertPayloadToJson(payload);

            return Jwts.builder()
                       .id(UUID.randomUUID().toString())
                       .subject(payload.userId().toString())
                       .claim(PAYLOAD, payloadJson)
                       .issuedAt(convertToDate(payload.issuedAt()))
                       .expiration(convertToDate(payload.expiredAt()))
                       .signWith(key)
                       .compact();
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize JWT payload", e);
            throw new JwtGenerationFailedException(e);
        } catch (Exception e) {
            log.error("Failed to generate JWT token", e);
            throw new JwtGenerationFailedException(e);
        }
    }

    public JwtPayload parseToken(String token) {
        try {
            Claims claims = parseAndValidateClaims(token);
            String payloadJson = claims.get(PAYLOAD, String.class);

            return convertJsonToPayload(payloadJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JWT payload", e);
            throw new JwtParsingFailedException(e);
        }
    }

    private Claims parseAndValidateClaims(String token) {
        try {
            SecretKey key = getSigningKey();

            return Jwts.parser()
                       .verifyWith(key)
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {}", e.getMessage());
            throw new JwtExpiredException(e);
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new JwtInvalidTokenException(e);
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtInvalidTokenException(e);
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtInvalidTokenException(e);
        }
    }

    /**
     * Secret Key 생성
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String convertPayloadToJson(JwtPayload payload) throws JsonProcessingException {
        return objectMapper.writeValueAsString(payload);
    }

    private JwtPayload convertJsonToPayload(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, JwtPayload.class);
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
    }
}
