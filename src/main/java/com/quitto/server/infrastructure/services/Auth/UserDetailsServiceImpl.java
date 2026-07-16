package com.quitto.server.infrastructure.services.Auth;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository repository;

    public UserDetailsServiceImpl(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserEntity user = repository.findByName(name)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + name));

        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPasswordHash(),
            authorities
        );
    }
}
