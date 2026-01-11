package com.gorany.oauth2jwt.domain.repository;

import com.gorany.oauth2jwt.domain.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {

}
