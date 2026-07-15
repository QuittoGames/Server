package com.quitto.server.infrastructure.services.Auth;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtValidationException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class JwtTokenService implements TokenService {

    private final JpaUserRepository repository;

    @Value("${api.security.key}") // Get ENV value
    private String KEY;

    public JwtTokenService(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public String genareteToken(@NotNull Long id) throws EntityNotFoundException ,IllegalStateException, JWTCreationException{
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);

        Optional<UserEntity> userLocal = repository.findById(id);

        if (userLocal.isEmpty()){
            throw new EntityNotFoundException("User not afound");
        }

        UserEntity userEntity = userLocal.get();

        if (userEntity.getId() == null){
            throw new NullPointerException("User without id");
        }

        String token = JWT.create()
            .withIssuer("coffe-api")
            .withSubject(String.valueOf(userEntity.getId()))
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
            .sign(algorithm)
        ;

        return token;
    }

    @Override
    public boolean verifyToken(String token) throws JWTVerificationException{
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);

        String JwtSubject = JWT.require(algorithm)
            .withIssuer("coffe-api")
            .build()
            .verify(token)
            .getSubject();

        if (!JwtSubject.isBlank()){
            return true;
        }

        return false; // I retrun false for deafalt beacause when use token use de bad cause is more security
    }

    @Override
    public Long extractIdSubject(String token)throws NullPointerException,JwtValidationException,ConversionException{
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);

        String JwtSubject = JWT.require(algorithm)
            .withIssuer("coffe-api")
            .build()
            .verify(token)
            .getSubject();

        if (!JwtSubject.isBlank()){
            return Long.parseLong(JwtSubject);

        }

        return null;
    }
}
