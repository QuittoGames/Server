package com.quitto.server.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing a Cookie in the domain layer.
 * This isolates the domain from framework-specific Cookie implementations
 * (like jakarta.servlet.http.Cookie or org.springframework.http.ResponseCookie).
 */
public record CookieDomain(
    String name,
    String value,
    boolean httpOnly,
    boolean secure,
    String path,
    Integer maxAge
) {
    public CookieDomain {
        Objects.requireNonNull(name, "Cookie name must not be null");
        Objects.requireNonNull(value, "Cookie value must not be null");

        // Basic validation - name cannot contain certain characters per RFC 6265
        if (name.isEmpty() || name.contains("=") || name.contains(";") || name.contains(",")) {
            throw new IllegalArgumentException("Invalid cookie name: " + name);
        }
    }

    /**
     * Creates a CookieDomain with default secure settings (httpOnly=true, secure=true)
     * @param name cookie name
     * @param value cookie value
     * @return new CookieDomain instance
     */
    public static CookieDomain of(String name, String value) {
        return new CookieDomain(name, value, true, true, "/", null);
    }

    /**
     * Creates a CookieDomain with specified path and maxAge
     * @param name cookie name
     * @param value cookie value
     * @param path cookie path (e.g., "/", "/api")
     * @param maxAgeInSeconds max age in seconds (null for session cookie)
     * @return new CookieDomain instance
     */
    public static CookieDomain of(String name, String value, String path, Integer maxAgeInSeconds) {
        return new CookieDomain(name, value, true, true, path, maxAgeInSeconds);
    }
}
