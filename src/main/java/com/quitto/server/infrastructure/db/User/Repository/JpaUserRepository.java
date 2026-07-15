package com.quitto.server.infrastructure.db.User.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quitto.server.infrastructure.db.User.Entity.UserEntity;


public interface JpaUserRepository
    extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<UserEntity> exexistsById(Long id);
}
