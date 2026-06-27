package com.quitto.server.infrastructure.db.mapper;

import com.quitto.server.domain.models.User;
import com.quitto.server.infrastructure.db.Entity.users.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity userEntity) throws IllegalArgumentException{
        if (userEntity == null){
            throw new IllegalArgumentException("Paramter of Entity JPA is null");
        }

        User userDomain = new User(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getPasswordHash(),
            userEntity.getEmail(),
            userEntity.getRole()
        );

        return userDomain;
    }

    public static UserEntity toInfra(User userDomain) throws IllegalArgumentException{
        if (userDomain == null){
            throw new IllegalArgumentException("Paramter of User Domain is null");
        }

        UserEntity userEntity = new UserEntity(
            userDomain.getId(),
            userDomain.getName(),
            userDomain.getPassowrdHash(),
            userDomain.getEmail(),
            userDomain.getRole()
        );
        return userEntity;
    }
    
}
