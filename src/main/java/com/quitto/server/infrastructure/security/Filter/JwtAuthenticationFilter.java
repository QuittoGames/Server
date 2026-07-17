package com.quitto.server.infrastructure.security.Filter;

import com.quitto.server.shared.exception.InvalidTokenException;

import java.io.IOException;
import java.util.List;

import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.security.SecurityUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final TokenService<Long> tokenService;
    private final UserRepository repository;

    public JwtAuthenticationFilter(TokenService<Long> tokenService,UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    // Validate the JWT sent in the Authorization header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException{
        try{
            String token = recoverToken(request); // The token cannot be null because recoverToken() validates it before.
            boolean isValidToken = tokenService.verifyToken(token);

            if(!isValidToken) throw new InvalidTokenException("The provided JWT token is invalid or expired");

            Long id = tokenService.extractIdSubject(token);

            User user_domain = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            var user = new SecurityUser(id, user_domain.getName(), user_domain.getRole());

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.role().name()));

            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }catch(NullPointerException NE){
            logger.error(NE); // Before wiil use the self loggerclass for Log4j
            SecurityContextHolder.clearContext();
        }
        catch(JWTVerificationException JTWVE){
            logger.error(JTWVE);
            SecurityContextHolder.clearContext();
        }
        catch(IllegalArgumentException IAE){
            logger.warn(IAE);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request) throws IllegalArgumentException{
        String HeaderToken = request.getHeader("Authorization");
        if (HeaderToken == null || HeaderToken.isBlank()){throw new IllegalArgumentException("JWT token is required");}
        String token = HeaderToken.replace("Bearer ", ""); // Remove the sufix of token
        return token;
    }

}
