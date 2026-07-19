package com.quitto.server.domain.interfaces.Token;

import java.util.Optional;

public interface TokenResolver {
    Optional<String> resolver(TokenRequestContext request);
}
