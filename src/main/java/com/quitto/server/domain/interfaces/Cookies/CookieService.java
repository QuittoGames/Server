package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.Cookie;

public interface CookieService {

    CookieDomain createCookie(String name, String value);

    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);

    Cookie toFrameworkCookie(CookieDomain domainCookie);
}
