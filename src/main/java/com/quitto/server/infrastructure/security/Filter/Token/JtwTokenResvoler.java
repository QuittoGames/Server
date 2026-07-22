package com.quitto.server.infrastructure.security.Filter.Token;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;

@Component
public class JtwTokenResvoler implements TokenResolver {

    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return request.getHeader("Authorization")
            .filter(v -> !v.isBlank())
            .map(v -> v.replace("Bearer ", ""));
    }
}
