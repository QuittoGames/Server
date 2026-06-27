package com.quitto.server.infrastructure.external.google;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class GoogleAuthService {
    private final OAuth2AuthorizedClientService clientService;

    public GoogleAuthService(OAuth2AuthorizedClientService clientService){
        this.clientService = clientService;
    }

    public OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken auth) {
        return clientService.loadAuthorizedClient(
            auth.getAuthorizedClientRegistrationId(),
            auth.getName()
        );
    }

    public String getToken(OAuth2AuthorizedClient client){
        if (client == null) {
            throw new IllegalStateException(
                "Usuário não autenticado com Google"
            );
        }

        return client.getAccessToken().getTokenValue();
    }

}
