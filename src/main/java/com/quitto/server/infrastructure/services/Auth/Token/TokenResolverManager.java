package com.quitto.server.infrastructure.services.Auth.Token;

import java.util.List;
import java.util.Optional;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;
import org.springframework.stereotype.Service;

@Service
public class TokenResolverManager {

    private final List<TokenResolver> resolvers;

    public TokenResolverManager(List<TokenResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public Optional<String> resolve(TokenRequestContext context) {
        for (TokenResolver resolver : resolvers) {
            Optional<String> token = resolver.resolver(context);

            if (token.isPresent()) {
                return token;
            }
        }

        return Optional.empty();
    }
}
