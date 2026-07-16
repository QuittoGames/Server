package com.quitto.server.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginReponseDTO;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.adpter.Auth.JwtTokenAdapter;

import javax.validation.Valid;

import java.util.Date;

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
    public ResponseEntity<LoginReponseDTO> login(@RequestBody @Valid LoginDTO data) {
        String token = service.login(data.name(), data.passoword());

        LoginReponseDTO reponse = new LoginReponseDTO(
            token,
            new Date()
        );

        return ResponseEntity.ok(reponse);
    }
}
