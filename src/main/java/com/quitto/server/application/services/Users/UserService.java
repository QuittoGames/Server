package com.quitto.server.application.services.Users;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.users.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }
}
