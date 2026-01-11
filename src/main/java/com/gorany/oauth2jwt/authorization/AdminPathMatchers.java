package com.gorany.oauth2jwt.authorization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AdminPathMatchers {

    static final AntPathMatcher ANT = new AntPathMatcher();
}
