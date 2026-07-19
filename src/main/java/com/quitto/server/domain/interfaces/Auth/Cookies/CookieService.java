package com.quitto.server.domain.interfaces.Auth.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

/**
 * Domain service interface for Cookie operations.
 * This interface defines the contract for creating and manipulating cookies
 * without depending on framework-specific implementations.
 */
public interface CookieService {

    /**
     * Creates a domain Cookie from the given name and value with secure defaults.
     * @param name cookie name
     * @param value cookie value
     * @return domain CookieDomain instance
     */
    CookieDomain createCookie(String name, String value);

    /**
     * Creates a domain Cookie with specified parameters.
     * @param name cookie name
     * @param value cookie value
     * @param path cookie path
     * @param maxAgeInSeconds max age in seconds (null for session)
     * @return domain CookieDomain instance
     */
    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);

    /**
     * Converts a domain Cookie to a framework-specific Cookie implementation.
     * This method should be implemented in the infrastructure layer.
     * @param domainCookie domain CookieDomain to convert
     * @return framework-specific Cookie instance
     */
    Object toFrameworkCookie(CookieDomain domainCookie);
}
