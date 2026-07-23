package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;
import com.quitto.server.infrastructure.services.Auth.Token.TokenResolverManager;

class TokenResolverManagerTest {

    @Test
    void usesFirstMatchingResolver() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.of("from-first"));
        when(second.resolve(any())).thenReturn(Optional.of("from-second"));

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertEquals("from-first", result.get());
        verify(second, never()).resolve(any());
    }

    @Test
    void fallsBackToNextResolverWhenFirstEmpty() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.empty());
        when(second.resolve(any())).thenReturn(Optional.of("from-second"));

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertEquals("from-second", result.get());
        verify(first).resolve(any());
        verify(second).resolve(any());
    }

    @Test
    void returnsEmptyWhenNoResolverMatches() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.empty());
        when(second.resolve(any())).thenReturn(Optional.empty());

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void handlesEmptyResolverList() {
        TokenResolverManager manager = new TokenResolverManager(List.of());
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));
        assertTrue(result.isEmpty());
    }

    @Test
    void stopsAtFirstMatchEvenIfLaterWouldAlsoMatch() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.of("first-match"));
        when(second.resolve(any())).thenReturn(Optional.of("second-match"));

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertEquals("first-match", result.get());
        verify(second, never()).resolve(any());
    }
}
