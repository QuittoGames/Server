package com.quitto.server.domain.interfaces.Token;

public interface TokenService<ID extends Number>{
    String genareteToken(Long id);
    boolean verifyToken(String token);
    ID extractIdSubject(String token);
}
