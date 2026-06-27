package com.quitto.server.infrastructure.external;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.Calendar;
import com.quitto.server.infrastructure.external.google.GoogleAuthService;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarClient {

    private final GoogleAuthService authService;

    public GoogleCalendarClient(GoogleAuthService authService){
        this.authService = authService;
    }
    
    public Calendar getCalendar(OAuth2AuthorizedClient auth)
        throws Exception,IllegalStateException {

        String accessToken = authService.getToken(auth);

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("Access token não encontrado");
        }

        HttpRequestInitializer credential = request ->
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        )
        .setApplicationName("QuittoServer")
        .build();
    }
}