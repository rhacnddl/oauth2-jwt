package com.gorany.oauth2jwt.dto;

import lombok.Builder;

@Builder
public record TokenDto(String jwtToken, String refreshToken) {

}
