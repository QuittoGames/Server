package com.quitto.server.domain.interfaces.Auth.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

public interface CookieService {

    CookieDomain createCookie(String name, String value);

    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);

    <T> T toFrameworkCookie(CookieDomain domainCookie);
}
