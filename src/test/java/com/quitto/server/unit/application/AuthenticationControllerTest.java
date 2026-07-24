package com.quitto.server.unit.application;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.controllers.AuthenticationController;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.Adpter.out.CookieManagerAdapter;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserAuthenticationService userAuthService;

    @Mock
    private CookieManagerAdapter cookieManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
            new AuthenticationController(userAuthService, cookieManager)
        ).build();
    }

    @Test
    void login_withValidCredentials_returns200AndToken() throws Exception {
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token"))
            .thenReturn(CookieDomain.of("access_token", "jwt-token"));

        LoginDTO login = new LoginDTO("quitto", "senha123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    void login_withInvalidCredentials_returns401() throws Exception {
        when(userAuthService.login("quitto", "wrong"))
            .thenReturn("");

        LoginDTO login = new LoginDTO("quitto", "wrong");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void register_withValidData_returns200AndToken() throws Exception {
        when(userAuthService.register("novo", "senha123", "novo@test.com"))
            .thenReturn("new-jwt-token");

        RegisterDTO register = new RegisterDTO("novo", "senha123", "novo@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("new-jwt-token"))
            .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    void register_withEmptyPassword_returns401() throws Exception {
        RegisterDTO register = new RegisterDTO("novo", "", "novo@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void register_withDuplicateUsername_returns401() throws Exception {
        when(userAuthService.register("existente", "senha", "email@test.com"))
            .thenReturn("");

        RegisterDTO register = new RegisterDTO("existente", "senha", "email@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_setsCookieOnResponse() throws Exception {
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token"))
            .thenReturn(CookieDomain.of("access_token", "jwt-token"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginDTO("quitto", "senha123"))))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("access_token"))
            .andExpect(cookie().httpOnly("access_token", true))
            .andExpect(cookie().path("access_token", "/"))
            .andExpect(cookie().secure("access_token", true));
    }
}
