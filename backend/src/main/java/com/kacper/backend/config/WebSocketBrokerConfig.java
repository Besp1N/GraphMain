package com.kacper.backend.config;

import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for WebSocket broker
 *
 * @Author Kacper Karabinowski
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer
{
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * @param jwtService service for handling jwt tokens
     * @param customUserDetailsService service for handling user details
     */
    public WebSocketBrokerConfig(
            JWTService jwtService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Configure message broker with simple broker and application destination prefixes
     *
     * @param registry message broker registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/notifications");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Register stomp endpoints with allowed origins and custom interceptor
     *
     * @param registry stomp endpoint registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173")
                .addInterceptors(
                        new WebSocketCustomInterceptor(
                                jwtService,
                                customUserDetailsService))
                .withSockJS();
    }
}
