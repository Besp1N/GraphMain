package com.kacper.backend.config;

import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.CustomUserDetailsService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketCustomInterceptor implements HandshakeInterceptor
{
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public WebSocketCustomInterceptor(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

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

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

    }

}
