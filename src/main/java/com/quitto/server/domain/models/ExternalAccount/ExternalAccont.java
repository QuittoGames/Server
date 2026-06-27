package com.quitto.server.domain.models.ExternalAccount;

import java.time.LocalDateTime;

import com.quitto.server.domain.enums.Provaider;

public class ExternalAccont {
    private Long id;
    private Long user_id;
    private Provaider provaider;
    private String external_client;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresAt;

    public ExternalAccont() {

    }

    public ExternalAccont(Long id, Long user_id, Provaider provaider, String external_client, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        this.id = id;
        this.user_id = user_id;
        this.provaider = provaider;
        this.external_client = external_client;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }
    public Long getUser_id() {
        return user_id;
    }
    public Provaider getProvaider() {
        return provaider;
    }
    public String getExternal_client() {
        return external_client;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

}
