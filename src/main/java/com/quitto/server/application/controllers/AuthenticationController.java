package com.quitto.server.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.LoginDTO;
import com.quitto.server.infrastructure.services.Auth.UserAuthenticationService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Authentication> login(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(service.login(data.name(), data.passoword()));
    }
}
