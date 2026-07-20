package com.quitto.server.domain.interfaces.Token;

import com.quitto.server.domain.valueobject.CookieDomain;
import java.util.Optional;

public interface TokenRequestContext {

    Optional<String> getHeader(String name);

    Optional<CookieDomain> getCookie(String name);
}
