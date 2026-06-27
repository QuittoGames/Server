package com.quitto.server.mcp.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.google.api.services.calendar.model.Event;
import com.quitto.server.infrastructure.external.google.GoogleAuthService;
import com.quitto.server.mcp.services.GoogleCalenderService;

public class GoogelCalenderTools {

    @Autowired
    public GoogleCalenderService service;

    @Autowired
    public GoogleAuthService authService;

    @Tool
    public List<Event> listEvents(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (!(auth instanceof OAuth2AuthenticationToken oauth)) {
               throw new IllegalStateException("Not OAuth2 login");
            }

            OAuth2AuthorizedClient client = authService.getAuthorizedClient(oauth);
                
            return service.listEvents(client);
        }catch(IllegalStateException e){
            System.err.println("Estado inválido de autenticação: " + e.getMessage());
        }catch(Exception E){
            System.err.println("Erro ao listar eventos do Google Calendar: " + E.getMessage());
        }
        return new ArrayList<>();
    }
    
}
