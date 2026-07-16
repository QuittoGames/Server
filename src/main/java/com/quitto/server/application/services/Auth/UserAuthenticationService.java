package com.quitto.server.application.services.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.adpter.Auth.JwtTokenAdapter;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenAdapter tokenService;
    private final UserRepository userRepository;

    public UserAuthenticationService(AuthenticationManager authenticationManager, JwtTokenAdapter tokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public String login(String name, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name, password);
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        String username = auth.getName();
        User user = userRepository.findByName(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return tokenService.genereteToken(user.getId());
    }
}
