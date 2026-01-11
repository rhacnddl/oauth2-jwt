package com.gorany.oauth2jwt.domain;

import com.gorany.oauth2jwt.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@DynamicUpdate
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private Role role;

    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(Role.fromValue(role.getId()).name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void recordAccessTime() {
        this.accessedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
