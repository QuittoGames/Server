package com.quitto.server.infrastructure.services.Auth.Jtw;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.quitto.server.domain.interfaces.Token.TokenService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class JwtTokenService implements TokenService {

    @Value("${api.security.key}") // Get ENV value
    private String KEY;

    @Override
    public String genareteToken(@NotNull Long id) throws EntityNotFoundException ,IllegalStateException, JWTCreationException{
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);

        String token = JWT.create()
            .withIssuer("coffe-api")
            .withSubject(String.valueOf(id))
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
    public Long extractIdSubject(String token) throws NullPointerException,JWTVerificationException,ConversionException{
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
