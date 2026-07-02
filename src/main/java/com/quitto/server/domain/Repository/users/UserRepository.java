package com.quitto.server.domain.Repository.users;

import java.util.Optional;
import com.quitto.server.domain.models.User.User;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}
