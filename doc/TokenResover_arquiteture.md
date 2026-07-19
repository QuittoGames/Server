# Token Resolution Architecture

## Objective

The authentication system should **not depend on where the token comes from**.

A JWT may arrive through:

- Authorization Header (`Bearer <token>`)
- HTTP Cookie (`access_token`)
- API Key (future)
- WebSocket handshake (future)
- Any other transport mechanism

The authentication filter should only receive a token, validate it, and authenticate the user.

---

# Architecture

```
                 Http Request
                      │
                      ▼
         JwtAuthenticationFilter
                      │
                      ▼
             TokenResolver
                      │
          CompositeTokenResolver
          ┌───────────┴───────────┐
          ▼                       ▼
BearerTokenResolver      CookieTokenResolver
          │                       │
          └───────────┬───────────┘
                      ▼
              Optional<String>
                      │
                      ▼
               JwtTokenService
                      │
        Verify / Extract Subject
                      │
                      ▼
              UserRepository
                      │
                      ▼
          Spring Security Context
```

---

# Responsibilities

## JwtAuthenticationFilter

Responsible only for authentication.

Responsibilities:

- Ask for a token.
- Validate the token.
- Extract the subject.
- Load the user.
- Populate the SecurityContext.

It **must not know**:

- Where the token came from.
- Whether it was a Cookie.
- Whether it was a Bearer Header.
- How cookies are read.

---

## TokenResolver

The abstraction responsible for obtaining a token from a request.

```java
public interface TokenResolver {

    Optional<String> resolve(TokenRequestContext request);

}
```

The filter communicates only with this interface.

---

## BearerTokenResolver

Responsible only for reading:

```
Authorization: Bearer <token>
```

Example:

```java
Optional<String> resolve(...)
```

Returns:

```
Bearer Token
```

or

```
Optional.empty()
```

---

## CookieTokenResolver

Responsible only for reading cookies.

Example:

```
Cookie:
access_token=eyJhbGc...
```

Returns:

```
JWT Token
```

or

```
Optional.empty()
```

---

## CompositeTokenResolver

Coordinates multiple TokenResolvers.

Example implementation:

```java
public class CompositeTokenResolver implements TokenResolver {

    private final List<TokenResolver> resolvers;

    @Override
    public Optional<String> resolve(TokenRequestContext request) {

        for (TokenResolver resolver : resolvers) {

            Optional<String> token = resolver.resolve(request);

            if (token.isPresent()) {
                return token;
            }

        }

        return Optional.empty();
    }

}
```

Order example:

1. Authorization Header
2. Cookie
3. API Key (future)

The first resolver that finds a token wins.

---

# TokenService

TokenService is **not responsible for reading HTTP requests**.

It only knows how to manipulate tokens.

Example:

```java
public interface TokenService<T> {

    String generateToken(T subject);

    boolean verifyToken(String token);

    T extractSubject(String token);

}
```

Implementations:

```
JwtTokenService
```

Future:

```
OpaqueTokenService
```

---

# CookieFactory

Cookie creation should be isolated.

Example:

```java
public class CookieFactory {

    public Cookie createAccessToken(String token);

    public Cookie createRefreshToken(String token);

    public Cookie deleteAccessToken();

}
```

Responsibilities:

- HttpOnly
- Secure
- SameSite
- MaxAge
- Path
- Domain

The rest of the application should never manually configure cookies.

---

# Authentication Flow

```
Client
    │
    ▼
POST /login
    │
    ▼
UserAuthenticationService
    │
Authenticate user
    │
    ▼
JwtTokenService
Generate JWT
    │
    ▼
CookieFactory
Create Cookie
    │
    ▼
Controller
Adds Cookie to HttpServletResponse
```

---

# Authorization Flow

```
Client Request
       │
       ▼
JwtAuthenticationFilter
       │
       ▼
CompositeTokenResolver
       │
       ├────────► BearerTokenResolver
       │
       └────────► CookieTokenResolver
                    │
                    ▼
               JWT Token
                    │
                    ▼
JwtTokenService
Verify Token
                    │
                    ▼
Extract Subject
                    │
                    ▼
UserRepository
                    │
                    ▼
SecurityContextHolder
```

---

# Advantages

## Single Responsibility Principle (SRP)

Each class has one responsibility.

| Class | Responsibility |
|--------|----------------|
| JwtAuthenticationFilter | Authentication |
| TokenResolver | Obtain token |
| BearerTokenResolver | Read Authorization header |
| CookieTokenResolver | Read Cookies |
| CompositeTokenResolver | Choose token source |
| JwtTokenService | Validate JWT |
| CookieFactory | Create Cookies |

---

## Open/Closed Principle (OCP)

New authentication mechanisms can be added without modifying existing code.

Example:

```
ApiKeyTokenResolver
```

or

```
WebSocketTokenResolver
```

Only a new implementation of TokenResolver is needed.

The filter remains unchanged.

---

## Low Coupling

The authentication filter has no dependency on HTTP headers or cookies.

It depends only on:

```
TokenResolver
```

This keeps infrastructure details isolated.

---

# Future Extensions

Possible new TokenResolvers:

- BearerTokenResolver
- CookieTokenResolver
- ApiKeyTokenResolver
- QueryParameterTokenResolver
- WebSocketTokenResolver

No changes are required inside JwtAuthenticationFilter.

---

# Design Philosophy

The authentication filter should answer only one question:

> "Give me a token if one exists."

It should never care **how** that token was transported.

This separation keeps the authentication pipeline clean, extensible, and aligned with the Dependency Inversion Principle (DIP).
