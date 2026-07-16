package com.quitto.server.infrastructure.adpter.Auth;

import com.quitto.server.infrastructure.services.Auth.JwtTokenService;

@org.springframework.stereotype.Component
public class JwtTokenAdapter{
    private final JwtTokenService service;

    public JwtTokenAdapter(JwtTokenService service){
        this.service = service;
    }

    public String genereteToken(Long id){
        return service.genareteToken(id);
    }

    public boolean verifyToken(String token){
        return service.verifyToken(token);
    }

    public Long extractIdSubject(String token){
        return service.extractIdSubject(token);
    }

}
