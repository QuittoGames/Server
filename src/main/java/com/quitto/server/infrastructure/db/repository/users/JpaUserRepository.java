package com.quitto.server.infrastructure.db.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quitto.server.infrastructure.db.Entity.users.UserEntity;

public interface JpaUserRepository
    extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}