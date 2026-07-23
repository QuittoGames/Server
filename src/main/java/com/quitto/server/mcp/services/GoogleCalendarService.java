package com.quitto.server.mcp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.Calendar;
import com.quitto.server.infrastructure.external.GoogleCalendarClient;

@Service
public class GoogleCalendarService {

    @Autowired
    public GoogleCalendarClient client;

    public String createEvent(String title , String descr){
        return "";
    }

    public List<Event>listEvents(OAuth2AuthorizedClient auth) throws Exception,IllegalStateException {
        try{
            Calendar calendar = client.getCalendar(auth);

            return calendar.events().list("primary").execute().getItems();
        }catch(IllegalStateException ISE){
            throw ISE;
        }catch(Exception E){
            throw E;
        }
    }
}
