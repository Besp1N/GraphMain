package com.kacper.backend.auth;

import com.kacper.backend.exception.InvalidCredentialsException;
import com.kacper.backend.exception.ResourceAlreadyExistException;
import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.User;
import com.kacper.backend.mail.MailService;
import com.kacper.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for AuthService
 *
 * @Author Kacper Karabinowski
 */
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Set up before each test
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for login method
     */
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

    /**
     * Test for login method with invalid credentials
     */
    @Test
    void login_InvalidCredentials() {
        // Arrange
        AuthLoginRequest authLoginRequest = new AuthLoginRequest("test@example.com", "wrongPassword");

        when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(authLoginRequest));
    }

    /**
     * Test for login method with bad credentials
     */
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



    @Test
    void register_alreadyExists() {

        AuthRegistrationRequest authRegistrationRequest = new AuthRegistrationRequest("sobaka@example.com", "password", "So", "Baka", "user");
        when(userRepository.save(any(User.class))).thenThrow(new ResourceAlreadyExistException("User with email: sobaka@example.com already exists"));

        assertThrows(ResourceAlreadyExistException.class, () -> authService.register(authRegistrationRequest));


        verify(userRepository).save(any(User.class));
        verify(mailService, never()).sendMail(anyString());
    }


    @Test
    void registerCapitalizes() {
        AuthRegistrationRequest authRegistrationRequest = new AuthRegistrationRequest(
                "test678@example.com", "password", "soba", "kao", "user");

        User user = User.builder()
                .email("test678@example.com")
                .password("encodedPassword")
                .name("Soba")
                .lastName("Kao")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(authRegistrationRequest.password())).thenReturn("encodedPassword");
        AuthRegistrationResponse response = authService.register(authRegistrationRequest);

        assertNotNull(response);
        assertEquals("Soba", response.name());
        assertEquals("Kao", response.lastName());
        assertEquals("test678@example.com", response.email());
        assertEquals("USER", response.role());

        verify(userRepository).save(any(User.class));
        verify(mailService).sendMail("test678@example.com");
    }


}