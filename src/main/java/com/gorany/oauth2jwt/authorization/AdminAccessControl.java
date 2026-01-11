package com.gorany.oauth2jwt.authorization;

import com.gorany.oauth2jwt.authorization.AdminAction.Spot;
import com.gorany.oauth2jwt.authorization.AdminAction.SpotDeepResource.OperationTime;
import com.gorany.oauth2jwt.authorization.AdminAction.SpotExactResource.Settings;
import com.gorany.oauth2jwt.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminAccessControl {

    public static boolean isAuthorized(Role role, HttpMethod method, String path) {
        if (role == Role.MASTER) {
            return true;
        }

        List<AdminAction> rights = switch (role) {
            case DOCK_MASTER -> DOCK_MASTER_RIGHTS;
            case CENTER_MASTER -> CENTER_MASTER_RIGHTS;
            default -> List.of();
        };

        return rights.stream()
                     .anyMatch(it -> it.matches(method, path));
    }

    private static final List<AdminAction> DOCK_MASTER_RIGHTS = getDockMasterRights();
    private static final List<AdminAction> CENTER_MASTER_RIGHTS = getAllRights();

    private static List<AdminAction> getDockMasterRights() {
        return List.of(OperationTime.READ,
                       Settings.READ,
                       Spot.READ);
    }

    public static List<AdminAction> getAllRights() {
        return Stream.of(OperationTime.values(),
                         Settings.values(),
                         Spot.values())
                     .flatMap(Arrays::stream)
                     .collect(Collectors.toList());
    }
}
