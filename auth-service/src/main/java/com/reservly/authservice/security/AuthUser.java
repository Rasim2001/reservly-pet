package com.reservly.authservice.security;

import com.reservly.authservice.domain.UserRole;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private final Long id;
    private final UserRole role;

    public AuthUser(
            Long id,
            UserRole userRole,
            String username,
            @Nullable String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.id = id;
        this.role = userRole;
    }
}
