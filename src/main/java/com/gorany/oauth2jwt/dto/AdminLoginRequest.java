package com.gorany.oauth2jwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminLoginRequest(@Email @NotEmpty String email, @NotEmpty String password) {

}
