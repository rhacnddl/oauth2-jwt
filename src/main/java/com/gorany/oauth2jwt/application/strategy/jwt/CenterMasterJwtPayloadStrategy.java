package com.gorany.oauth2jwt.application.strategy.jwt;

import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayload;
import com.gorany.oauth2jwt.application.strategy.jwt.dto.JwtPayloadCreate;
import com.gorany.oauth2jwt.domain.Staff;
import com.gorany.oauth2jwt.domain.repository.StaffRepository;
import com.gorany.oauth2jwt.enums.Role;
import com.gorany.oauth2jwt.enums.StaffRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CenterMasterJwtPayloadStrategy implements JwtPayloadStrategy {

    @Value("${jwt.expiration-mins}")
    private Integer expirationMins;

    private final StaffRepository staffRepository;

    @Override
    public boolean match(Role role) {
        return role == Role.CENTER_MASTER;
    }

    @Override
    public JwtPayload create(JwtPayloadCreate.Request request) {

        Set<Long> authorizedSpots = staffRepository.findAllByUserIdAndRoleIn(request.userId(), List.of(StaffRole.from(request.role())))
                                                   .stream()
                                                   .map(Staff::getSpotId)
                                                   .collect(Collectors.toSet());

        JwtPayloadCreate.Command command = JwtPayloadMapper.toCommand(request, authorizedSpots, expirationMins);

        return JwtPayload.from(command);
    }
}
