package com.gorany.oauth2jwt.domain.repository;

import com.gorany.oauth2jwt.domain.Staff;
import com.gorany.oauth2jwt.enums.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findAllByUserIdAndRoleIn(Long userId, Collection<StaffRole> roles);
}
