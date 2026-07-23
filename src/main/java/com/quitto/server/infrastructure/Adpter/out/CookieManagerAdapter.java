package com.quitto.server.infrastructure.Adpter.out;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Auth.CookieManager;
import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.quitto.server.domain.interfaces.Cookies.CookieService;

@Component
public class CookieManagerAdapter implements CookieManager {

    private final CookieService cookieService;

    public CookieManagerAdapter(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public CookieDomain createAccessTokenCookie(String value) {
        return cookieService.createCookie("access_token", value);
    }

    public Cookie toFrameworkCookie(CookieDomain domainCookie) {
        return cookieService.toFrameworkCookie(domainCookie);
    }

    public void writeCookie(HttpServletResponse response, Cookie cookie){
        Objects.requireNonNull(response, "response cannot be null");
        Objects.requireNonNull(cookie, "cookieDomain cannot be null");

        response.addCookie(cookie);
    }
}
