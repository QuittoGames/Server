# Cookies no Spring Boot

> Baseado na documentação oficial:
> - Jakarta Servlet 6.0 API (`jakarta.servlet.http.Cookie`)
> - Spring Framework 6.x (`org.springframework.http.ResponseCookie`)
> - Spring Boot 4.x (`CookieSameSiteSupplier`, `server.servlet.session.cookie.*`)

---

## 1. Duas APIs, Dois Contextos

No ecossistema Spring existem **duas formas** de trabalhar com cookies,
cada uma com seu propósito:

| API | Pacote | Contexto |
|---|---|---|
| **Servlet Cookie** | `jakarta.servlet.http.Cookie` | Spring MVC tradicional (Servlet stack) |
| **ResponseCookie** | `org.springframework.http.ResponseCookie` | Spring WebFlux + MVC (recomendado) |

O seu projeto usa **Spring MVC** (Servlet stack), então ambas funcionam.
A diferença principal:

- `jakarta.servlet.http.Cookie` → API mais antiga, não suporta `SameSite`
- `ResponseCookie` → API mais moderna, suporta `SameSite` + builder pattern

---

## 2. `jakarta.servlet.http.Cookie` (Servlet API)

### Criar e enviar

```java
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@PostMapping("/login")
public ResponseEntity<?> login(HttpServletResponse response) {
    Cookie cookie = new Cookie("access_token", "meu-jwt-aqui");

    cookie.setHttpOnly(true);           // não acessível via JS
    cookie.setSecure(true);             // só enviado por HTTPS
    cookie.setPath("/");                // disponível em todo o site
    cookie.setMaxAge(3600);             // 1 hora em segundos
    cookie.setDomain("meudominio.com"); // escopo do cookie

    response.addCookie(cookie);

    return ResponseEntity.ok().build();
}
```

### Métodos principais

| Método | Descrição |
|---|---|
| `new Cookie(name, value)` | Construtor. Name não pode conter `;` ou espaço |
| `setValue(String)` | Altera o valor depois de criado |
| `setMaxAge(int)` | Idade máxima **em segundos**. Negativo = sessão, Zero = deletar |
| `setPath(String)` | Escopo de path. Ex: `"/"`, `"/auth"` |
| `setDomain(String)` | Escopo de domínio. Ex: `".exemplo.com"` |
| `setSecure(boolean)` | Só envia por HTTPS |
| `setHttpOnly(boolean)` | Bloqueia acesso via JavaScript (`document.cookie`) |
| `setAttribute(name, value)` | Atributos extras (adicionado no Servlet 6.0) |

### Limitação crítica

`jakarta.servlet.http.Cookie` **não tem** `setSameSite()` como método nativo.
Para definir `SameSite` com essa API, você precisa usar `setAttribute()`:

```java
cookie.setAttribute("SameSite", "Lax");   // Servlet 6.0+
```

Ou configurar globalmente via `CookieSameSiteSupplier` (mais abaixo).

---

## 3. `ResponseCookie` (Spring Web — recomendado)

### Criar e enviar

```java
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@PostMapping("/login")
public ResponseEntity<?> login() {
    ResponseCookie cookie = ResponseCookie.from("access_token", "meu-jwt-aqui")
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")            // ← SameSite nativo
        .path("/")
        .maxAge(Duration.ofHours(1))
        .domain("meudominio.com")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
}
```

### Vantagens sobre `jakarta.servlet.http.Cookie`

1. **SameSite nativo** — sem precisar de `setAttribute`
2. **Builder pattern** — encadeamento legível
3. **`maxAge` em `Duration`** — mais expressivo que segundos brutos
4. **Funciona em MVC e WebFlux** — mesma API nos dois stacks

### Como adicionar ao response

Diferente da Servlet API, `ResponseCookie` não tem um `response.addCookie()`.
Você precisa adicionar manualmente ao header `Set-Cookie`:

```java
// Opção 1: ResponseEntity com header
return ResponseEntity.ok()
    .header(HttpHeaders.SET_COOKIE, cookie.toString())
    .body(resposta);

// Opção 2: HttpServletResponse.addHeader
response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
```

> **Atenção:** se você chamar `response.addHeader("Set-Cookie", ...)` múltiplas vezes,
> cada chamada adiciona um header separado. Para múltiplos cookies no mesmo response,
> chame uma vez para cada cookie.

### Builder completo

```java
ResponseCookie cookie = ResponseCookie.from("nome", "valor")
    .httpOnly(true)                    // bloquear acesso JS
    .secure(true)                      // só HTTPS
    .sameSite("Strict")                // "Lax" | "Strict" | "None"
    .path("/api")                      // escopo de path
    .domain(".exemplo.com")            // escopo de domínio
    .maxAge(Duration.ofDays(30))       // expiração
    .build();
```

---

## 4. Como Ler Cookies (Request)

### 4.1 `@CookieValue` — jeito mais simples

```java
import org.springframework.web.bind.annotation.CookieValue;

@GetMapping("/perfil")
public ResponseEntity<?> perfil(
        @CookieValue("access_token") String token) {

    // Se o cookie não existir, lança exceção
    return ResponseEntity.ok(servico.processar(token));
}
```

Com valor padrão (evita erro se o cookie não existir):

```java
@GetMapping("/perfil")
public ResponseEntity<?> perfil(
        @CookieValue(value = "access_token", defaultValue = "") String token) {

    if (token.isBlank()) {
        return ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(servico.processar(token));
}
```

### 4.2 `HttpServletRequest.getCookies()` — acesso manual

```java
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@GetMapping("/perfil")
public ResponseEntity<?> perfil(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
        return ResponseEntity.status(401).build();
    }

    String token = Arrays.stream(cookies)
        .filter(c -> "access_token".equals(c.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Token não encontrado"));

    return ResponseEntity.ok(servico.processar(token));
}
```

### 4.3 Extrair em um Filter (seu caso)

Em filters (`OncePerRequestFilter`), você **não tem** `@CookieValue`.
Use `HttpServletRequest.getCookies()`:

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        String token = recoverToken(request);
        // ...
    }

    private String recoverToken(HttpServletRequest request) {
        // Opção 1: Header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank()) {
            return authHeader.replace("Bearer ", "");
        }

        // Opção 2: Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        throw new IllegalArgumentException("Token não encontrado");
    }
}
```

---

## 5. Atributos de Segurança (Obrigatórios)

Atributo | Função | Recomendação
---|---|---
**`Secure`** | Só envia o cookie por HTTPS | `true` em produção
**`HttpOnly`** | Bloqueia acesso via JS (`document.cookie`) | `true` sempre
**`SameSite`** | Controla envio em requisições cross-site | `Lax` (padrão moderno) ou `Strict`

### SameSite — os três modos

| Modo | Comportamento |
|---|---|
| `Lax` | Cookie enviado em navegação top-level (link normal). **Não** enviado em requests embutidos (iframe, img, fetch) |
| `Strict` | Cookie **nunca** enviado em requisições de terceiros. Mais seguro, mas pode quebrar links externos |
| `None` | Cookie enviado em **todas** as requisições. Requer `Secure=true`. Usado para SSO/third-party |

### Por que SameSite é importante

Sem `SameSite`, um site malicioso pode fazer o navegador do usuário
enviar o cookie de autenticação do seu site em requisições disfarçadas
(CSRF — Cross-Site Request Forgery).

---

## 6. Configuração Global com `CookieSameSiteSupplier`

Se você quer aplicar `SameSite` para **todos** os cookies sem modificar
cada ponto do código, use o `CookieSameSiteSupplier` do Spring Boot:

```java
import org.springframework.boot.web.server.servlet.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        // Aplica Lax em todos os cookies cujo nome começa com "myapp"
        return CookieSameSiteSupplier.ofLax()
            .whenHasNameMatching("myapp.*");
    }
}
```

### Factory methods disponíveis

| Método | Retorno |
|---|---|
| `CookieSameSiteSupplier.ofLax()` | Sempre retorna `Lax` |
| `CookieSameSiteSupplier.ofStrict()` | Sempre retorna `Strict` |
| `CookieSameSiteSupplier.ofNone()` | Sempre retorna `None` |
| `CookieSameSiteSupplier.of(SameSite.LAX)` | Retorna o valor especificado |

### Métodos de filtro

| Método | Filtra por |
|---|---|
| `.whenHasName("nome")` | Nome exato do cookie |
| `.whenHasNameMatching("regex")` | Regex no nome do cookie |
| `.when(predicate)` | Predicate customizado (`Cookie → boolean`) |

### Configuração para Session Cookie

```properties
# application.properties
server.servlet.session.cookie.same-site=lax
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

---

## 7. Remover Cookies

### Remover via Servlet API

```java
Cookie cookie = new Cookie("access_token", null);
cookie.setMaxAge(0);    // zero = deleta imediatamente
cookie.setPath("/");
response.addCookie(cookie);
```

### Remover via ResponseCookie

```java
ResponseCookie cookie = ResponseCookie.from("access_token", "")
    .maxAge(0)        // zero = deleta
    .path("/")
    .build();

response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
```

> O `maxAge(0)` instrui o navegador a remover o cookie.
> O path **precisa** ser o mesmo usado quando o cookie foi criado.

---

## 8. Erros Comuns

### 8.1 Cookie criado mas não aparece no navegador

- `Secure=true` mas você está testando em HTTP (não HTTPS)
- `SameSite=None` sem `Secure=true` (navegadores rejeitam)
- Path diferente do esperado

### 8.2 `response.addCookie()` vs `response.addHeader()`

- `response.addCookie(cookie)` → adiciona ao header `Set-Cookie` automaticamente
- `response.addHeader("Set-Cookie", cookie.toString())` → manual (necessário para `ResponseCookie`)

Os dois são válidos. **Não use ambos** para o mesmo cookie.

### 8.3 Perigos de cookie sem HttpOnly

```java
// ❌ PERIGOSO — qualquer script XSS rouba o token
Cookie cookie = new Cookie("access_token", jwt);

// ✅ SEGURO — JS não consegue ler
Cookie cookie = new Cookie("access_token", jwt);
cookie.setHttpOnly(true);
```

### 8.4 Cookies com SameSite=None precisam de Secure

```java
// ❌ Navegador rejeita
ResponseCookie.from("token", jwt)
    .sameSite("None")
    .build();

// ✅ Correto
ResponseCookie.from("token", jwt)
    .sameSite("None")
    .secure(true)    // obrigatório com None
    .build();
```

---

## 9. Comparação Rápida

| Cenário | `jakarta.servlet.http.Cookie` | `ResponseCookie` |
|---|---|---|
| Spring MVC | ✅ Sim | ✅ Sim |
| Spring WebFlux | ❌ Não | ✅ Sim |
| SameSite nativo | ❌ Não (precisa `setAttribute`) | ✅ Sim |
| Builder pattern | ❌ Não | ✅ Sim |
| `response.addCookie()` | ✅ Sim | ❌ Precisa header manual |
| `maxAge` em `Duration` | ❌ Segundos (`int`) | ✅ `Duration` |
| Recomendado para | Código legado | **Projetos novos** |

---

## 10. Resumo p/ seu Projeto

No seu `JwtAuthenticationFilter`, a abordagem com `ResponseCookie`
é a mais alinhada com Spring moderno:

```java
// Enviar (no controller/login)
ResponseCookie cookie = ResponseCookie.from("access_token", token)
    .httpOnly(true)
    .secure(true)
    .sameSite("Lax")
    .path("/")
    .maxAge(Duration.ofHours(1))
    .build();

response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

// Ler (no filter)
private Optional<String> recoverToken(HttpServletRequest request) {
    // 1. Tenta header Authorization
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && !authHeader.isBlank()) {
        return Optional.of(authHeader.replace("Bearer ", ""));
    }

    // 2. Tenta cookie
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        return Arrays.stream(cookies)
            .filter(c -> "access_token".equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst();
    }

    return Optional.empty();
}
```

---

## Referências

- [Jakarta Servlet 6.0 Cookie API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/cookie)
- [Spring Framework ResponseCookie (6.x)](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/http/ResponseCookie.html)
- [Spring Boot CookieSameSiteSupplier](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/web/server/servlet/CookieSameSiteSupplier.html)
- [Spring Boot Servlet Web (SameSection)](https://docs.spring.io/spring-boot/reference/web/servlet.html)
