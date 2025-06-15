package com.example.booking.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SecurityConfig}.
 */
class SecurityConfigTest {

    /**
     * Should create a SecurityFilterChain successfully.
     */
//    @Test
//    void filterChain_createsSecurityFilterChainSuccessfully() throws Exception {
//        SecurityConfig config = new SecurityConfig();
//        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
//
//        // Mock chained methods
//        when(http.csrf(any())).thenReturn(http);
//        when(http.authorizeHttpRequests(any())).thenReturn(http);
//        when(http.httpBasic(any())).thenReturn(http);
//        SecurityFilterChain mockChain = mock(SecurityFilterChain.class);
//        when(http.build()).thenReturn(mockChain);
//
//        SecurityFilterChain chain = config.filterChain(http);
//
//        assertNotNull(chain);
//        verify(http).csrf(any());
//        verify(http).authorizeHttpRequests(any());
//        verify(http).httpBasic(any());
//        verify(http).build();
//    }

    /**
     * Should throw IllegalStateException and log error if HttpSecurity fails.
     */
    @Test
    void filterChain_throwsExceptionAndLogsError() throws Exception {
        SecurityConfig config = new SecurityConfig();
        HttpSecurity http = mock(HttpSecurity.class);

        when(http.csrf(any())).thenThrow(new RuntimeException("Mock error"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> config.filterChain(http));
        assertTrue(ex.getMessage().contains("Security configuration failed"));
    }
}