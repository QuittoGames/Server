package com.quitto.server.mcp.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;
import com.quitto.server.mcp.services.GoogleCalendarService;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/api/calendar")

public class CalendarController {

    private final GoogleCalendarService service;

    public CalendarController(GoogleCalendarService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("🔥 CONTROLLER CHAMADO");
        return "ok";
    }

    @GetMapping("/debug/auth")
    public Object debug() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/events")
    public List<Event> listEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient oauthClient) {
        try{
            System.out.println("AUTH = " + SecurityContextHolder.getContext().getAuthentication());
            return service.listEvents(oauthClient);
        }catch(Exception e){
            System.err.println("Erro ao listar eventos do Google Calendar: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
