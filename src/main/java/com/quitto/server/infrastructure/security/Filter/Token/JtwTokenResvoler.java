package com.quitto.server.infrastructure.security.Filter.Token;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;

public class JtwTokenResvoler implements TokenResolver {

    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        return Stream.<Supplier<String>>of(
            () -> request.getHeader("Authorization")
        )
        .map(Supplier::get)
        .filter(v -> v != null && !v.isBlank())
        .findFirst()
        .map(v -> v.replace("Bearer ", ""));
    }
}
