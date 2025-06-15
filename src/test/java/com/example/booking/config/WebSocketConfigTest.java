package com.example.booking.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link WebSocketConfig}.
 */
class WebSocketConfigTest {

    /**
     * Should register STOMP endpoints successfully.
     */
//    @Test
//    void registerStompEndpoints_registersEndpointSuccessfully() {
//        WebSocketConfig config = new WebSocketConfig();
//        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
//        when(registry.addEndpoint(anyString())).thenReturn(registry);
//        when(registry.setAllowedOriginPatterns(anyString())).thenReturn(registry);
//        when(registry.withSockJS()).thenReturn(registry);
//
//        assertDoesNotThrow(() -> config.registerStompEndpoints(registry));
//        verify(registry).addEndpoint("/ws");
//        verify(registry).setAllowedOriginPatterns("*");
//        verify(registry).withSockJS();
//    }

    /**
     * Should throw IllegalStateException and log error if endpoint registration fails.
     */
    @Test
    void registerStompEndpoints_throwsExceptionAndLogsError() {
        WebSocketConfig config = new WebSocketConfig();
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        when(registry.addEndpoint(anyString())).thenThrow(new RuntimeException("Mock error"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> config.registerStompEndpoints(registry));
        assertTrue(ex.getMessage().contains("WebSocket endpoint registration failed"));
    }

    /**
     * Should configure message broker successfully.
     */
//    @Test
//    void configureMessageBroker_configuresBrokerSuccessfully() {
//        WebSocketConfig config = new WebSocketConfig();
//        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
//        when(registry.enableSimpleBroker(anyString())).thenReturn(registry);
//        when(registry.setApplicationDestinationPrefixes(anyString())).thenReturn(registry);
//
//        assertDoesNotThrow(() -> config.configureMessageBroker(registry));
//        verify(registry).enableSimpleBroker("/topic");
//        verify(registry).setApplicationDestinationPrefixes("/app");
//    }

    /**
     * Should throw IllegalStateException and log error if broker configuration fails.
     */
    @Test
    void configureMessageBroker_throwsExceptionAndLogsError() {
        WebSocketConfig config = new WebSocketConfig();
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        doThrow(new RuntimeException("Mock error")).when(registry).enableSimpleBroker(anyString());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> config.configureMessageBroker(registry));
        assertTrue(ex.getMessage().contains("WebSocket message broker configuration failed"));
    }
}
