package com.quitto.server.infrastructure.security.Filter.Token;

import java.util.Optional;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.services.Auth.Cookies.HttpCookieService;

import jakarta.servlet.http.Cookie;

public class CookieTokenResolver implements TokenResolver {
    private final HttpCookieService service;

    public CookieTokenResolver(HttpCookieService service){
        this.service = service;
    }

    @Override
    public Optional<String> resolver(TokenRequestContext request) {
        CookieDomain CookieDomain = request.getCookie("acess_token");
        return Optional.of("");
    }
}
