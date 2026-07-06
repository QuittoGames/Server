package com.quitto.server.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.quitto.server.infrastructure.services.OAuth.OAuth2UserProvisioningService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuth2UserProvisioningService oauthService;

    public SecurityConfig(OAuth2UserProvisioningService oauthService) {
        this.oauthService = oauthService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/oauth2/**",
                "/login/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
            ).permitAll()
            .requestMatchers("/mcp/**").hasAuthority("MCP")
            .requestMatchers("/admin/**").hasAuthority("ADMIN")
            // .requestMatchers("/api/**").hasAuthority("API")
            .anyRequest().authenticated()
        )

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, authException) -> {
                res.setStatus(401);
            })
        )

        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(userInfo ->
                userInfo.userService(oauthService)
            )
        );

        return http.build();
    }

}
