package com.kacper.backend.config;

import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.CustomUserDetailsService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Custom interceptor for WebSocket
 *
 * @Author Kacper Karabinowski
 */
public class WebSocketCustomInterceptor implements HandshakeInterceptor
{
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * @param jwtService service for handling jwt tokens
     * @param customUserDetailsService service for handling user details
     */
    public WebSocketCustomInterceptor(
            JWTService jwtService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Authenticate user before handshake
     *
     * @param request request for handshake
     * @param response response for handshake
     * @param wsHandler WebSocket handler
     * @param attributes attributes for handshake
     * @return true if handshake is successful
     * @throws Exception if something goes wrong
     */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        String token = request.getURI().getQuery().split("=")[1];
        String username = jwtService.extractUsername(token);

        return (jwtService.isTokenValid(token, customUserDetailsService.loadUserByUsername(username)));
    }

    /**
     * Do nothing after handshake (default)
     *
     * @param request request for handshake
     * @param response response for handshake
     * @param wsHandler WebSocket handler
     * @param exception exception for handshake
     */
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

    }

}
