package com.gorany.oauth2jwt.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

/**
 * Admin API에 대한 액션(권한 단위) 정의 각 리소스별로 수행 가능한 작업(READ, WRITE, UPDATE, DELETE)을 정의
 */
public sealed interface AdminAction permits
        AdminAction.SpotDeepResource,
        AdminAction.SpotExactResource,
        AdminAction.Spot {

    String PREFIX = "/api/admin";
    String SPOT_PREFIX = PREFIX + "/spots/{spotId}";

    HttpMethod getMethod();

    String getPathPattern();

    default boolean matches(HttpMethod method, String path) {
        if (!getMethod().equals(method)) {
            return false;
        }

        return AdminPathMatchers.ANT.match(getPathPattern(), path);
    }

    sealed interface SpotDeepResource extends AdminAction {

        String getResourcePath();

        @Override
        default String getPathPattern() {
            return SPOT_PREFIX + "/" + getResourcePath() + "/**";
        }

        @Getter
        @RequiredArgsConstructor
        enum OperationTime implements SpotDeepResource {
            READ(HttpMethod.GET),
            WRITE(HttpMethod.POST),
            ;

            private final HttpMethod method;

            @Override
            public String getResourcePath() {
                return "operation-times";
            }
        }
    }

    sealed interface SpotExactResource extends AdminAction {

        String getResourcePath();

        @Override
        default String getPathPattern() {
            return SPOT_PREFIX + "/" + getResourcePath();
        }

        @Getter
        @RequiredArgsConstructor
        enum Settings implements SpotExactResource {
            READ(HttpMethod.GET),
            UPDATE(HttpMethod.PUT),
            ;

            private final HttpMethod method;

            @Override
            public String getResourcePath() {
                return "settings";
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum Spot implements AdminAction {
        READ(HttpMethod.GET),
        UPDATE(HttpMethod.PUT),
        ;

        private final HttpMethod method;
        private final String pathPattern = SPOT_PREFIX;
    }
}
