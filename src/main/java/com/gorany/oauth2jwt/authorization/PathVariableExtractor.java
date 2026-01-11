package com.gorany.oauth2jwt.authorization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathVariableExtractor {

    private static final String SPOT_ID = "spotId";
    private static final String SPOT_ID_PATTERN = "/api/admin/spots/{spotId}/**";

    public static Optional<Long> extractSpotId(String path) {
        if (StringUtils.isEmpty(path)) {
            return Optional.empty();
        }

        String pathWithoutQuery = path.split("\\?")[0];

        if (AdminPathMatchers.ANT.match(SPOT_ID_PATTERN, pathWithoutQuery)) {
            Map<String, String> variables = AdminPathMatchers.ANT.extractUriTemplateVariables(SPOT_ID_PATTERN, pathWithoutQuery);
            return parseLong(variables.get(SPOT_ID));
        }

        return Optional.empty();
    }

    private static Optional<Long> parseLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
