package com.gorany.oauth2jwt.enums;

import lombok.Getter;

@Getter
public enum StaffRole {

    DOCK_MASTER(1),
    CENTER_MASTER(11),
    ;

    private final int code;

    StaffRole(int code) {
        this.code = code;
    }

    public static StaffRole from(Role userRole) {
        if (userRole == null) {
            throw new IllegalArgumentException("User role must not be null");
        }

        try {
            return StaffRole.valueOf(userRole.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported Role: " + userRole);
        }
    }
}
