package com.example.booking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

/**
 * WebSocket configuration for enabling STOMP messaging with SockJS fallback.
 * Configures endpoints and message broker for real-time communication.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    /**
     * Registers STOMP endpoints for WebSocket connections.
     * Adds SockJS fallback and allows all origins.
     *
     * @param registry the {@link StompEndpointRegistry} to register endpoints with
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        try {
            logger.info("Registering STOMP endpoint '/ws' with SockJS and allowed origins '*'.");
            registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
            logger.debug("STOMP endpoint '/ws' registered successfully.");
        } catch (Exception ex) {
            logger.error("Failed to register STOMP endpoints.", ex);
            throw new IllegalStateException("WebSocket endpoint registration failed", ex);
        }
    }

    /**
     * Configures the message broker for handling messages.
     * Enables a simple broker and sets application destination prefixes.
     *
     * @param registry the {@link MessageBrokerRegistry} to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        try {
            logger.info("Configuring message broker with '/topic' and application prefix '/app'.");
            registry.enableSimpleBroker("/topic");
            registry.setApplicationDestinationPrefixes("/app");
            logger.debug("Message broker configured successfully.");
        } catch (Exception ex) {
            logger.error("Failed to configure message broker.", ex);
            throw new IllegalStateException("WebSocket message broker configuration failed", ex);
        }
    }
}