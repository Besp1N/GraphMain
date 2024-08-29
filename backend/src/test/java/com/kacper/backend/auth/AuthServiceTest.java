package com.kacper.backend.auth;

import com.kacper.backend.exception.InvalidCredentialsException;
import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.User;
import com.kacper.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        // Arrange
        AuthLoginRequest authLoginRequest = new AuthLoginRequest("test@example.com", "password");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        // Act
        AuthLoginResponse response = authService.login(authLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test@example.com", response.email());
        assertEquals("USER", response.role());
        assertEquals("jwtToken", response.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        AuthLoginRequest authLoginRequest = new AuthLoginRequest("test@example.com", "wrongPassword");

        when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(authLoginRequest));
    }

    @Test
    void login_BadCredentials() {
        // Arrange
        AuthLoginRequest authLoginRequest = new AuthLoginRequest("test@example.com", "wrongPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.of(user));
        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(authLoginRequest));
    }
}