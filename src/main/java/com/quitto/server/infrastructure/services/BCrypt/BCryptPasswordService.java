package com.quitto.server.infrastructure.services.BCrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.Auth.PasswordService;

@Service
public class BCryptPasswordService implements PasswordService{
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String raw){
        return this.encoder.encode(raw);
    }

    public boolean matches(String raw, String hash) {
        return encoder.matches(raw, hash);
    }
}
