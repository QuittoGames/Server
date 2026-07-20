package com.quitto.server.infrastructure.services.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.exception.AuthenticationException;
import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.domain.models.User.User;

@Service
public class SpringAuthenticationService implements AuthenticationService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public SpringAuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password )throws AuthenticationException{
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            return userRepository.findByName(auth.getName()).orElseThrow();
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Authentication failed", e);
        }
    }
}
