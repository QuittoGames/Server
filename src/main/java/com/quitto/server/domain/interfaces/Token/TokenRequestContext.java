package com.quitto.server.domain.interfaces.Token;
import java.util.Map;

import com.quitto.server.domain.valueobject.CookieDomain;

public class TokenRequestContext {

    private final Map<String, String> headers;
    private final Map<String, CookieDomain> cookies;

    public TokenRequestContext(Map<String, String> headers,Map<String, CookieDomain> cookies){
        this.headers = headers;
        this.cookies = cookies;
    }


    public String getHeader(String name) {
        return headers.get(name);
    }

    public CookieDomain getCookie(String name) {
        return cookies.get(name);
    }
}
