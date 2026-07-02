package com.quitto.server.infrastructure.db.User.Adapter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Mapper.UserMapper;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    @Autowired
    private JpaUserRepository repository;

    @Override
    public User save(User user){
        var entity = UserMapper.toInfra(user);
        UserEntity saved = repository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);

        if (userEntity.isEmpty()) {

            return Optional.empty();
        }

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public Optional<User> findByName(String name) {
        Optional<UserEntity> userEntity = repository.findByName(name);

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public boolean existsByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByEmail'");
    }

    @Override
    public boolean existsByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByName'");
    }
}
