package com.quitto.server.domain.Repository.users;

import java.util.Optional;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}
