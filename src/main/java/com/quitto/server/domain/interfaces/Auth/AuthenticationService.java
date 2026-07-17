package com.quitto.server.domain.interfaces.Auth;

import org.springframework.security.core.AuthenticationException;

import com.quitto.server.domain.models.User.User;

public interface AuthenticationService{
    User authenticate(String username, String password) throws AuthenticationException;
}
