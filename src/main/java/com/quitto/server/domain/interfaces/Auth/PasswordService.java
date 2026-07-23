package com.quitto.server.domain.interfaces.Auth;

public interface PasswordService {
    String encode(String raw);

    boolean matches(String raw , String passwordHash);
}
