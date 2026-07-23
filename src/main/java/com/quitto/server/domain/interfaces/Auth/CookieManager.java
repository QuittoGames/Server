package com.quitto.server.domain.interfaces.Auth;

import com.quitto.server.domain.valueobject.CookieDomain;

public interface CookieManager {
    CookieDomain createAccessTokenCookie(String value);
}
