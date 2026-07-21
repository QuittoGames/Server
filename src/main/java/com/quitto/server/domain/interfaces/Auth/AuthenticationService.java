package com.quitto.server.domain.interfaces.Auth;

import com.quitto.server.domain.exception.AuthenticationException;
import com.quitto.server.domain.models.User.User;

public interface AuthenticationService{
    User authenticate(String username, String password) throws AuthenticationException;

    User register(String name, String password , String email);
}
