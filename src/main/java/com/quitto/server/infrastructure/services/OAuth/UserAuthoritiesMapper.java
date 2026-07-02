package com.quitto.server.infrastructure.services.OAuth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.quitto.server.infrastructure.db.User.Entity.UserEntity;

@Component
public class UserAuthoritiesMapper {

    public List<GrantedAuthority> map(UserEntity user) {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }
}
