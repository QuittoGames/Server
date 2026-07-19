package com.quitto.server.application.services.Auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.models.User.User;

import jakarta.servlet.http.Cookie;

@Service
public class UserAuthenticationService {

    private final AuthenticationService authenticationService;
    private final TokenService<Long> tokenService;

    public UserAuthenticationService(AuthenticationService authenticationService, TokenService<Long> tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    public String login(String name, String password) throws AuthenticationException {
        User user = authenticationService.authenticate(name, password);
        String token = tokenService.genareteToken(user.getId());
        Cookie cookie = new Cookie("acess_token", token);

        return token;
    }
}
