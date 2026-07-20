package com.quitto.server.infrastructure.services.Auth.Token.Cookies;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.Auth.Cookies.CookieService;
import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.Cookie;

@Service
public class HttpCookieService implements CookieService{

    @Override
    public CookieDomain createCookie(String name, String value) {
        return CookieDomain.of(name, value);
    }

    @Override
    public CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds) {
        return CookieDomain.of(name, value, path, maxAgeInSeconds);
    }

    @Override
    public Cookie toFrameworkCookie(CookieDomain domainCookie) {
        Cookie cookie = new Cookie(domainCookie.name(), domainCookie.value());

        cookie.setHttpOnly(domainCookie.httpOnly());
        cookie.setSecure(domainCookie.secure());
        cookie.setPath(domainCookie.path());
        cookie.setDomain("coffe_server");

        if (domainCookie.maxAge() != null) {
            cookie.setMaxAge(domainCookie.maxAge());
        }

        return cookie;
    }
}
