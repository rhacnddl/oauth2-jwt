package com.gorany.oauth2jwt.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

@Getter
public enum Role implements GrantedAuthority {
    DOCK_MASTER(11, "DOCK_MASTER"),
    CENTER_MASTER(12, "CENTER_MASTER"),
    EMPLOYEE(90, "EMPLOYEE"),
    MASTER(99, "MASTER");

    private final int id;
    private final String name;

    Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role fromValue(int id) {
        return Arrays.stream(Role.values())
                     .filter(role -> role.id == id)
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("No matching role for id " + id));
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
