package com.quitto.server.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginResponseDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.dto.Auth.RegisterResponseDTO;
import com.quitto.server.application.services.Auth.UserAuthenticationService;

import javax.validation.Valid;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final UserAuthenticationService service;

    public AuthenticationController(UserAuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        String token = service.login(data.name(), data.password());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        LoginResponseDTO response = new LoginResponseDTO(
            token,
            new Date()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO data) {

        if (data.passoword().length() <= 0 || data.passoword().length() >= 500) {
            return ResponseEntity.status(401).build();
        }

        String token = service.register(data.name(), data.passoword(), data.email());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        RegisterResponseDTO  response = new RegisterResponseDTO(
            token,
            new Date()
        );

        return ResponseEntity.ok(response);
    }

}
