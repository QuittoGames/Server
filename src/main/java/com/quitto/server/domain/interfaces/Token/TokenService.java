package com.quitto.server.domain.interfaces.Token;

import java.util.Optional;

public interface TokenService<ID extends Number>{
    String generateToken(Long id);
    boolean verifyToken(String token);
    Optional<ID> extractIdSubject(String token);
}
