package com.quitto.server.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginResponseDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.dto.Auth.RegisterResponseDTO;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.domain.interfaces.Auth.CookieManager;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.Adpter.out.CookieManagerAdapter;

import javax.validation.Valid;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final UserAuthenticationService service;
    private final CookieManagerAdapter cookieManager;

    public AuthenticationController(UserAuthenticationService service, CookieManagerAdapter cookieManager) {
        this.service = service;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data, HttpServletResponse response) {
        String token = service.login(data.name(), data.password());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie(token);
        Cookie cookie = cookieManager.toFrameworkCookie(cookieDomain);
        cookieManager.writeCookie(response, cookie);

        LoginResponseDTO responseBody = new LoginResponseDTO(
            token,
            new Date()
        );

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO data) {

        if (data.password().length() <= 0 || data.password().length() >= 500) {
            return ResponseEntity.status(401).build();
        }

        String token = service.register(data.name(), data.password(), data.email());

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
