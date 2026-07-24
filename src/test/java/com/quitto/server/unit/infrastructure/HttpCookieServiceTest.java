package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieService;

import jakarta.servlet.http.Cookie;

class HttpCookieServiceTest {

    private final HttpCookieService service = new HttpCookieService();

    @Test
    void createsCookie() {
        CookieDomain cookie = service.createCookie("access_token", "jwt-value");
        assertEquals("access_token", cookie.name());
        assertEquals("jwt-value", cookie.value());
        assertTrue(cookie.httpOnly());
        assertTrue(cookie.secure());
        assertEquals("/", cookie.path());
        assertNull(cookie.maxAge());
    }

    @Test
    void createsCookieWithPathAndMaxAge() {
        CookieDomain cookie = service.createCookie("refresh_token", "val", "/auth/refresh", 604800);
        assertEquals("refresh_token", cookie.name());
        assertEquals("/auth/refresh", cookie.path());
        assertEquals(604800, cookie.maxAge());
    }

    @Test
    void convertsDomainToFrameworkCookie() {
        CookieDomain domain = CookieDomain.of("test-cookie", "test-value", "/app", 7200);

        Cookie cookie = service.toFrameworkCookie(domain);

        assertEquals("test-cookie", cookie.getName());
        assertEquals("test-value", cookie.getValue());
        assertEquals("/app", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.getSecure());
        assertEquals(7200, cookie.getMaxAge());
        assertEquals("coffe_server", cookie.getDomain());
    }

    @Test
    void frameworkCookieUsesDomainDefaults() {
        CookieDomain domain = CookieDomain.of("session", "abc123");

        Cookie cookie = service.toFrameworkCookie(domain);
        assertEquals("/", cookie.getPath());
        assertEquals(-1, cookie.getMaxAge(), "Session cookie should have maxAge -1");
    }

    @Test
    void frameworkCookieWithNullMaxAge_hasNegativeMaxAge() {
        CookieDomain domain = CookieDomain.of("test", "val");
        Cookie cookie = service.toFrameworkCookie(domain);
        assertEquals(-1, cookie.getMaxAge());
    }

}
