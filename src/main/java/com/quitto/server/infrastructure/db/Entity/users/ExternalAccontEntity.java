package com.quitto.server.infrastructure.db.Entity.users;

import java.time.LocalDateTime;

import com.quitto.server.domain.enums.Provaider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ExternalAccont")
public class ExternalAccontEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Provaider provaider;

    @Column(nullable = false)
    private String external_client;

    @Column(nullable = false)
    private String acsessToken;

    private String refreshToken;

    private LocalDateTime expiresAt;

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Provaider getProvaider() {
        return provaider;
    }

    public void setProvaider(Provaider provaider) {
        this.provaider = provaider;
    }

    public String getExternal_client() {
        return external_client;
    }

    public void setExternal_client(String external_client) {
        this.external_client = external_client;
    }

    public String getAcsessToken() {
        return acsessToken;
    }

    public void setAcsessToken(String acsessToken) {
        this.acsessToken = acsessToken;
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
